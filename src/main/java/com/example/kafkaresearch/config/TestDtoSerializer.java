package com.example.kafkaresearch.config;

import com.example.kafkaresearch.dto.TestDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 8 byte integer is for dto.id
 * 4 byte integer is for dto.name length in UTF-8 encoding (0 if the name is empty)
 * N byte integer is for dto.name in UTF-8 encoding
 */

public class TestDtoSerializer implements Serializer<TestDto> {
    @Override
    public byte[] serialize(String topic, TestDto dto) {
        try {
            byte[] serializedName;
            int stringSize;

            if (dto == null) {
                return null;
            }
            if (dto.name() != null) {
                serializedName = dto.name().getBytes(StandardCharsets.UTF_8);
                stringSize = serializedName.length;
            } else {
                serializedName = new byte[0];
                stringSize = 0;
            }

            final var buffer = ByteBuffer.allocate(8 + 4 + stringSize);
            buffer.putLong(dto.id());
            buffer.putInt(stringSize);
            buffer.put(serializedName);
            return buffer.array();
        } catch (Exception exception) {
            throw new SerializationException("Error during serialization of dto with id " + dto.id() + ":" + exception);
        }
    }
}
