package com.example.LocationCollection.service;

import com.example.LocationCollection.dto.ProcessedLocationData;
import com.example.LocationCollection.dto.StreamRequest;
import com.example.LocationCollection.serde.LiveLocationDataDeSerde;
import com.example.LocationCollection.serde.LiveLocationDataSerde;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationCollectionService {

    @Value(value = "${spring.kafka.outgoing-topic}")
    private String processedLocationData;


    public void process(KStream<String, StreamRequest> stream) {

        stream.filter((key,data) ->
                (!(Math.abs(data.getLatitude() - data.getCenterLatitude()) > data.getRadius()) ||
                        (Math.abs(data.getLongitude() - data.getCenterLongitude()) > data.getRadius()))

        )
                .mapValues(data -> {

                    System.out.println(data);

                    return ProcessedLocationData.builder()
                            .longitude(data.getLongitude())
                            .latitude(data.getLatitude())
                            .id(data.getId())
                            .build();
                })
                .to(processedLocationData, Produced.with(Serdes.String(), getSreamRequestSerde()));

    }

    private Serde<ProcessedLocationData> getSreamRequestSerde() {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<ProcessedLocationData> streamRequestSerializer = new LiveLocationDataSerde();
        serdeProps.put("LiveLocationPOJOClass", ProcessedLocationData.class);
        streamRequestSerializer.configure(serdeProps, false);

        final Deserializer<ProcessedLocationData> streamRequestDeserializer = new LiveLocationDataDeSerde();
        serdeProps.put("LiveLocationPOJOClass", ProcessedLocationData.class);
        streamRequestDeserializer.configure(serdeProps, false);
        return Serdes.serdeFrom(streamRequestSerializer, streamRequestDeserializer);
    }

}
