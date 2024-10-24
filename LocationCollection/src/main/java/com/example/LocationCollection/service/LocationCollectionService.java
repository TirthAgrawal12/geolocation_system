package com.example.LocationCollection.service;

import com.example.LocationCollection.model.StreamRequest;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocationCollectionService {

    @Value(value = "${spring.kafka.outgoing-topic}")
    private String pocBatteryDataProcessed;

    public void process(KStream<String, StreamRequest> stream) {

        stream.mapValues(data -> {

            System.out.println(data.getLatitude());
            return stream;
        });
//                .to(pocBatteryDataProcessed, Produced.with(Serdes.String(), new ProcessedBatteryHealthDataSerde()));

    }

}
