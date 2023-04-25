package com.example.kafkaresearch.producer;

import com.example.avro.TestAvroDto;
import com.example.kafkaresearch.dto.TestDto;
import com.example.kafkaresearch.enums.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageSendService {
    private final KafkaTemplate<Long, TestAvroDto> avroKafkaTemplate;
    private final KafkaTemplate<Long, String> stringKafkaTemplate;
    private final KafkaTemplate<Long, String> customKafkaTemplate;

    public void sendStringMessage(KafkaTopic kafkaTopic, String payload) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.STRING.getTopicName());
        final var message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, (long) payload.hashCode())
                .build();
        stringKafkaTemplate.send(message).whenComplete(resultBiConsumer());
    }

    public void sendDtoMessage(KafkaTopic kafkaTopic, TestDto payload, int partitionNumber) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.CUSTOM.getTopicName());
        final var message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, payload.id())
                .setHeader(KafkaHeaders.PARTITION, partitionNumber)
                .build();
        customKafkaTemplate.send(message).whenComplete(resultBiConsumer());
    }

    public void sendAvroDtoMessage(KafkaTopic kafkaTopic, TestAvroDto testAvroDto) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.AVRO.getTopicName());
        final var message = MessageBuilder.withPayload(testAvroDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, testAvroDto.getId())
                .build();
        avroKafkaTemplate.send(message).whenComplete(resultBiConsumer());
    }

    private <V> BiConsumer<SendResult<Long, V>, Throwable> resultBiConsumer() {
        return (result, throwable) -> {
            if (throwable == null) {
                System.out.println(result.toString());
                log.info("Message key {}, payload {} was sent to topic {}", result.getProducerRecord().key(),
                        result.getProducerRecord().value(), result.getRecordMetadata().topic());
            } else {
                log.error("Failed to send message:\n{}", Arrays.toString(throwable.getStackTrace()));
            }
        };
    }
}
