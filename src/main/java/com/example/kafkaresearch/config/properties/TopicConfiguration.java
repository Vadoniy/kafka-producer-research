package com.example.kafkaresearch.config.properties;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Value("${kafka-topic-names.custom}")
    public String customTopicName;

    @Bean
    public NewTopic customTopicName() {
        return TopicBuilder.name(customTopicName)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
