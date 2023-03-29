package com.example.kafkaresearch.producer;

import com.example.kafkaresearch.converter.TestDtoToAvro;
import com.example.kafkaresearch.dto.TestDto;
import com.example.avro.TestAvroDto;
import com.example.kafkaresearch.enums.KafkaTopic;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageSendService {

    private final KafkaTemplate<String, TestAvroDto> kafkaTemplate;
    private final TestDtoToAvro testDtoToAvro;

    public void sendStringMessage(KafkaTopic kafkaTopic, String payload) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.DEFAULT.getTopicName());
        final var msg = new GenericMessage<>(payload, Map.of(KafkaHeaders.TOPIC, topicName, KafkaHeaders.GROUP_ID, "default"));
        try {
            System.out.println(kafkaTemplate.send(msg).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDtoMessage(KafkaTopic kafkaTopic, TestDto payload) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.DEFAULT.getTopicName());
        final var msg = new GenericMessage<>(payload, Map.of(KafkaHeaders.TOPIC, topicName, KafkaHeaders.GROUP_ID, "default"));
        try {
            System.out.println(kafkaTemplate.send(msg).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAvroDtoMessage(KafkaTopic kafkaTopic, TestDto testDto) {
        final var topicName = Optional.ofNullable(kafkaTopic)
                .map(KafkaTopic::getTopicName)
                .orElse(KafkaTopic.DEFAULT.getTopicName());
        final var payload = testDtoToAvro.convert(testDto);
        final var message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.GROUP_ID, "default-avro")
                .setHeader(KafkaHeaders.KEY, payload.getId())
                .build();

        kafkaTemplate.send(message);
    }
}
