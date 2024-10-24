package com.example.LocationCollection.serde;

import com.example.LocationCollection.dto.ProcessedLocationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;


import java.util.Map;

public class LiveLocationDataDeSerde implements Deserializer<ProcessedLocationData> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Not needed for this example
    }

    @Override
    public ProcessedLocationData deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, ProcessedLocationData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing User", e);
        }
    }

    @Override
    public void close() {
        // Not needed for this example

    }
}
