package com.example.kafkaresearch.enums;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    AVRO ("avro-serializer-topic"),
    STRING ("string-serializer-topic"),
    CUSTOM ("custom-serializer-topic");

    private String topicName;

    KafkaTopic(String topicName) {
        this.topicName = topicName;
    }
}
