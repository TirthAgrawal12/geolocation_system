package com.example.LocationCollection.serde;

import com.example.LocationCollection.dto.StreamRequest;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class LocationDataSerde extends Serdes.WrapperSerde<StreamRequest>{

    public LocationDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(StreamRequest.class));
    }
}
