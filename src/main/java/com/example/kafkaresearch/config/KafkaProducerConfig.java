package com.example.kafkaresearch.config;

import com.example.avro.TestAvroDto;
import com.example.kafkaresearch.config.properties.AvroKafkaProducerProperties;
import com.example.kafkaresearch.config.properties.CustomProducerProperties;
import com.example.kafkaresearch.config.properties.StringProducerProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final AvroKafkaProducerProperties avroKafkaProducerProperties;
    private final CustomProducerProperties customProducerProperties;
    private final StringProducerProperties stringProducerProperties;
    private final KafkaProperties kafkaProperties;

    //1. Send Avro data to Kafka
    @Bean
    public ProducerFactory<String, TestAvroDto> avroProducerFactory() throws ClassNotFoundException {
        final var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(avroKafkaProducerProperties.getKeySerializer()));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(avroKafkaProducerProperties.getValueSerializer()));
        props.putAll(avroKafkaProducerProperties.getProperties());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, TestAvroDto> avroKafkaTemplate() throws ClassNotFoundException {
        return new KafkaTemplate<>(avroProducerFactory());
    }

    //2. Send custom data to Kafka
    @Bean
    public ProducerFactory<String, String> customProducerFactory() throws ClassNotFoundException {
        final var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(customProducerProperties.getKeySerializer()));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(customProducerProperties.getValueSerializer()));
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> customKafkaTemplate() throws ClassNotFoundException {
        return new KafkaTemplate<>(customProducerFactory());
    }

    //3. Send String data to Kafka
    @Bean
    public ProducerFactory<String, String> stringProducerFactory() throws ClassNotFoundException {
        final var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(stringProducerProperties.getKeySerializer()));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(stringProducerProperties.getValueSerializer()));
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() throws ClassNotFoundException {
        return new KafkaTemplate<>(stringProducerFactory());
    }
}
