package com.example.kafkaresearch.converter;

import com.example.kafkaresearch.dto.TestDto;
import com.example.avro.TestAvroDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class TestDtoToAvro implements Converter<TestDto, TestAvroDto> {
    @Override
    public TestAvroDto convert(TestDto source) {
        return new TestAvroDto(source.getId(), source.getName());
    }
}
