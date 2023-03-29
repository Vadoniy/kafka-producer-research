package com.example.kafkaresearch.enums;

import lombok.Getter;

@Getter
public enum KafkaTopic {
    AVRO ("mytopic-avro"),
    DEFAULT ("mytopic");

    private String topicName;

    KafkaTopic(String topicName) {
        this.topicName = topicName;
    }
}
