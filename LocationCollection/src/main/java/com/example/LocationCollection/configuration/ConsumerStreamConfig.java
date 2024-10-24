package com.example.LocationCollection.configuration;

import com.example.LocationCollection.dto.StreamRequest;
import com.example.LocationCollection.serde.LocationDataSerde;
import com.example.LocationCollection.service.LocationCollectionService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@EnableKafka
@EnableKafkaStreams
@Configuration
@RequiredArgsConstructor
public class ConsumerStreamConfig {

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.incoming-topic}")
    private String pocLocationDataCollection;

    @Value(value = "${spring.kafka.group-id}")
    private String groupId;

    @Autowired
    private LocationCollectionService locationCollectionService;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, groupId);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaStreamsConfiguration(props);
    }


    @Bean
    public KStream<String, StreamRequest> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, StreamRequest> stream;
        try {
             stream = kStreamBuilder.stream(pocLocationDataCollection, Consumed.with(Serdes.String(), new LocationDataSerde()));

            this.locationCollectionService.process(stream);
        }catch (Exception e){
            throw new RuntimeException("Error serializing Data", e);
        }
        return stream;

    }


}
