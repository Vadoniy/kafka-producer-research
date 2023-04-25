package com.example.kafkaresearch.config;

import com.example.avro.TestAvroDto;
import com.example.kafkaresearch.config.properties.AvroKafkaProducerProperties;
import com.example.kafkaresearch.config.properties.CustomProducerProperties;
import com.example.kafkaresearch.config.properties.ProducerProperties;
import com.example.kafkaresearch.config.properties.StringProducerProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final AvroKafkaProducerProperties avroKafkaProducerProperties;
    private final CustomProducerProperties customProducerProperties;
    private final StringProducerProperties stringProducerProperties;
    private final KafkaProperties kafkaProperties;

    //1. Send Avro data to Kafka
    @Bean
    public ProducerFactory<Long, TestAvroDto> avroProducerFactory() {
        final var props = createProducerProperties(avroKafkaProducerProperties);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<Long, TestAvroDto> avroKafkaTemplate() {
        return new KafkaTemplate<>(avroProducerFactory());
    }

    //2. Send custom data to Kafka
    @Bean
    public ProducerFactory<Long, String> customProducerFactory() {
        final var props = createProducerProperties(customProducerProperties);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<Long, String> customKafkaTemplate() {
        return new KafkaTemplate<>(customProducerFactory());
    }

    //3. Send String data to Kafka
    @Bean
    public ProducerFactory<Long, String> stringProducerFactory() {
        final var props = createProducerProperties(stringProducerProperties);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<Long, String> stringKafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }

    private Map<String, Object> createProducerProperties(ProducerProperties producerProperties) {
        final var props = kafkaProperties.buildProducerProperties();
        try {
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    Class.forName(producerProperties.getKeySerializer()));
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    Class.forName(producerProperties.getValueSerializer()));
        } catch (ClassNotFoundException e) {
            final var errorMessage = String.format("""
                            Key or value serializer class is not found from property class %s:
                            key-serializer: %s
                            value-serializer: %s""",
                    producerProperties.getClass().getName(),
                    producerProperties.getKeySerializer(),
                    producerProperties.getValueSerializer());
            throw new RuntimeException(errorMessage, e);
        }
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        Optional.ofNullable(producerProperties.getProperties())
                .ifPresent(props::putAll);
        return props;
    }
}
