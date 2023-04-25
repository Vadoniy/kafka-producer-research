package com.example.kafkaresearch.config.properties;

import java.util.Map;

public interface ProducerProperties {
    String getKeySerializer();
    String getValueSerializer();
    default Map<String, String> getProperties() {
        return null;
    }
}
