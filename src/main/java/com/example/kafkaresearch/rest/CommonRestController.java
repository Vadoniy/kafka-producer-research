package com.example.kafkaresearch.rest;

import com.example.avro.TestAvroDto;
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
        kafkaMessageSendService.sendStringMessage(KafkaTopic.STRING, payload);
        return payload;
    }

    @PostMapping("/kafka/avro/test-dto")
    public TestAvroDto kafkaPayload(@RequestBody TestAvroDto testAvroDto) {
        kafkaMessageSendService.sendAvroDtoMessage(KafkaTopic.AVRO, testAvroDto);
        return testAvroDto;
    }

    @PostMapping("/kafka/test-dto-partition-1")
    public TestDto kafkaPayload(@RequestBody TestDto testDto) {
        kafkaMessageSendService.sendDtoMessage(KafkaTopic.CUSTOM, testDto, 0);
        return testDto;
    }

    @PostMapping("/kafka/test-dto-partition-2")
    public TestDto kafkaPayload2(@RequestBody TestDto testDto) {
        kafkaMessageSendService.sendDtoMessage(KafkaTopic.CUSTOM, testDto, 1);
        return testDto;
    }
}
