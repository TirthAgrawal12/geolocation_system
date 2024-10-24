package com.example.LiveLocationModule.serde;


import com.example.LiveLocationModule.dto.SendLiveLocationAlert;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class LiveLocationDataSerde extends Serdes.WrapperSerde<SendLiveLocationAlert>{

    public LiveLocationDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(SendLiveLocationAlert.class));
    }
}
