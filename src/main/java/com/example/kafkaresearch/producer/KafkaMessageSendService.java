package com.example.kafkaresearch.producer;

import com.example.avro.TestAvroDto;
import com.example.kafkaresearch.dto.TestDto;
import com.example.kafkaresearch.enums.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageSendService {

    private final KafkaTemplate<String, TestAvroDto> avroKafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, String> customKafkaTemplate;

    public void sendStringMessage(KafkaTopic kafkaTopic, String payload) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.STRING.getTopicName());
        final var msg = new GenericMessage<>(payload, Map.of(KafkaHeaders.TOPIC, topicName, KafkaHeaders.GROUP_ID, "string-consumer-group"));
        try {
            System.out.println(stringKafkaTemplate.send(msg).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDtoMessage(KafkaTopic kafkaTopic, TestDto payload) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.CUSTOM.getTopicName());
        final var msg = new GenericMessage<>(payload, Map.of(KafkaHeaders.TOPIC, topicName, KafkaHeaders.GROUP_ID, "custom-consumer-group"));
        try {
            System.out.println(customKafkaTemplate.send(msg).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAvroDtoMessage(KafkaTopic kafkaTopic, TestAvroDto testAvroDto) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.AVRO.getTopicName());
        final var message = MessageBuilder.withPayload(testAvroDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.GROUP_ID, "avro-consumer-group")
                .setHeader(KafkaHeaders.KEY, testAvroDto.getId())
                .build();

        avroKafkaTemplate.send(message);
    }
}
