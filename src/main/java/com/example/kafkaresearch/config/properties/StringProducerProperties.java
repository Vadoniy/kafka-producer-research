package com.example.kafkaresearch.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-producer.string-producer")
@Getter
@Setter
public class StringProducerProperties implements ProducerProperties {

    private String keySerializer;

    private String valueSerializer;

    private String groupId;
}
