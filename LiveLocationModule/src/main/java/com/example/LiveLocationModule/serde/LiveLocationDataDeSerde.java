package com.example.LiveLocationModule.serde;

import com.example.LiveLocationModule.dto.SendLiveLocationAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class LiveLocationDataDeSerde implements Deserializer<SendLiveLocationAlert> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Not needed for this example
    }

    @Override
    public SendLiveLocationAlert deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, SendLiveLocationAlert.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing User", e);
        }
    }

    @Override
    public void close() {
        // Not needed for this example

    }
}
