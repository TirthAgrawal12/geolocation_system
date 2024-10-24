package com.example.ExternalDeviceModule.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExternalDeviceConsumer {

    @KafkaListener(topics = "poc-update-external-device", groupId = "poc-live-location-collector-${spring.kafka.carid}")
    public void consume(String message) {
        try {
            System.out.println("Message received.!");
            System.out.println(message);
            System.out.println("poc-live-location-collector-${spring.kafka.carid}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
