package com.example.GpsModule.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class LocationSendService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static final String TOPIC = "poc-location-data-collection";
    private final KafkaTemplate<String, String> kafkaTemplate;
    AtomicInteger counter = new AtomicInteger();


    @Bean
    CommandLineRunner publishHealthData() throws InterruptedException {

        return args -> {

            final List<Integer> latitude = List.of(100, 90, 80, 70);
            final List<Integer> longitude = List.of(100, 90, 80, 70);

            for (int index=0; index < latitude.size(); index++) {

                JSONObject payload = new JSONObject();
                payload.put("Time",dateFormat.format(new Date()));
                payload.put("latitude",latitude.get(index));
                payload.put("longitude",longitude.get(index));

                var message = MessageBuilder.withPayload(payload.toJSONString())
                        .setHeader(KafkaHeaders.TOPIC, TOPIC)
                        .setHeader(KafkaHeaders.KEY, String.valueOf(counter.get()))
                        .setHeader("system", "poc-location-data-collection")
                        .build();

                var future = kafkaTemplate.send(message);

                try {
                    var result = future.get();
                    System.out.println("Message sent successfully: " + result.getProducerRecord().value());
                    System.out.println("Partition: " + result.getRecordMetadata().partition());


                } catch (ExecutionException | InterruptedException e) {
                    System.err.println("Failed to send message: " + e.getCause().getMessage());
                }

                Thread.sleep(2000);
            }
        };
    }

}
