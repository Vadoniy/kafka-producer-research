package com.example.kafkaresearch.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka-producer.avro-producer")
@Getter
@Setter
public class AvroKafkaProducerProperties implements ProducerProperties {

    private String keySerializer;

    private String valueSerializer;

    private String groupId;

    private Map<String, String> properties;
}
