package com.example.LiveLocationModule.configuration;

import com.example.LiveLocationModule.dto.LiveLocationData;
import com.example.LiveLocationModule.dto.SendLiveLocationAlert;
import com.example.LiveLocationModule.serde.LiveLocationDataDeSerde;
import com.example.LiveLocationModule.serde.LiveLocationDataSerde;
import com.example.LiveLocationModule.serde.LiveLocationSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
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
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@EnableKafka
@EnableKafkaStreams
@Configuration
@RequiredArgsConstructor
public class LiveLocationConsumerConfig {

    @Value(value = "${spring.kafka.incoming-topic}")
    private String pocLocationDataCollection;

    @Value(value = "${spring.kafka.group-id}")
    private String groupId;

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.outgoing-topic}")
    private String processedLocationData;

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
    public KStream<String, SendLiveLocationAlert> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, SendLiveLocationAlert> stream;
        try{
            stream = kStreamBuilder.stream(pocLocationDataCollection, Consumed.with(Serdes.String(), new LiveLocationDataSerde()));

            stream.mapValues(data -> {

                System.out.println(data);

                return SendLiveLocationAlert.builder()
                        .longitude(data.getLongitude())
                        .latitude(data.getLatitude())
                        .message("Car is running out of fence.")
                        .id(data.getId())
                        .build();
            }).to(processedLocationData, Produced.with(Serdes.String(), getSreamRequestSerde()));

            System.out.println(stream);
        }catch (Exception e){
            throw new RuntimeException("Error serializing Data", e);
        }
        return stream;
    }

    private Serde<SendLiveLocationAlert> getSreamRequestSerde() {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<SendLiveLocationAlert> streamRequestSerializer = new LiveLocationSerde();
        serdeProps.put("LiveLocationPOJOClass", LiveLocationData.class);
        streamRequestSerializer.configure(serdeProps, false);

        final Deserializer<SendLiveLocationAlert> streamRequestDeserializer = new LiveLocationDataDeSerde();
        serdeProps.put("LiveLocationPOJOClass", LiveLocationData.class);
        streamRequestDeserializer.configure(serdeProps, false);
        return Serdes.serdeFrom(streamRequestSerializer, streamRequestDeserializer);
    }

}
