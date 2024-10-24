package com.example.LiveLocationModule.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableKafka
@EnableKafkaStreams
@Configuration
@RequiredArgsConstructor
public class LiveLocationConsumerConfig {

    @Value(value = "${spring.kafka.incoming-topic}")
    private String pocLocationDataCollection;

    @Value(value = "${spring.kafka.group-id}")
    private String groupId;
}
