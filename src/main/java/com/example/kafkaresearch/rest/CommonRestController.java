package com.example.kafkaresearch.rest;

import com.example.kafkaresearch.dto.TestDto;
import com.example.kafkaresearch.enums.KafkaTopic;
import com.example.kafkaresearch.producer.KafkaMessageSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommonRestController {

    private final KafkaMessageSendService kafkaMessageSendService;

    @PostMapping("/kafka/message")
    public String kafkaPayload(String payloadInput) {
        final var payload = Optional.ofNullable(payloadInput)
                .map(String::toUpperCase)
                .orElse("Empty string");
        kafkaMessageSendService.sendStringMessage(KafkaTopic.DEFAULT, payload);
        return payload;
    }

    @PostMapping("/kafka/dto")
    public TestDto kafkaPayload(@RequestBody TestDto testDto) {
        kafkaMessageSendService.sendAvroDtoMessage(KafkaTopic.AVRO, testDto);
        return testDto;
    }
}
