package com.example.LocationCollection.serde;

import com.example.LocationCollection.dto.ProcessedLocationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class LiveLocationDataSerde implements Serializer<ProcessedLocationData> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Not needed for this example
    }

    @Override
    public byte[] serialize(String topic, ProcessedLocationData data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Data", e);
        }
    }


    @Override
    public void close() {
        // Not needed for this example

    }
}
