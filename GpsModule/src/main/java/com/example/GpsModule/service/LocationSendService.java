package com.example.GpsModule.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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

    @Value(value = "${spring.kafka.center.latitude}")
    private int centerLatitude;
    @Value(value = "${spring.kafka.center.longitude}")
    private int centerLongitude;
    @Value(value = "${spring.kafka.center.radius}")
    private int radius;
    @Value(value = "${spring.kafka.center.x}")
    private int x;
    @Value(value = "${spring.kafka.center.y}")
    private int y;
    @Value(value = "${spring.kafka.carid}")
    private int carid;

    @Value(value = "${spring.kafka.latitude}")
    private List<Integer> latitude;
    @Value(value = "${spring.kafka.longitude}")
    private List<Integer> longitude;


    @Bean
    CommandLineRunner publishHealthData() throws InterruptedException {

        return args -> {

            for (int index=0; index < latitude.size(); index++) {

                JSONObject payload = new JSONObject();
                payload.put("Time",dateFormat.format(new Date()));
                payload.put("latitude",latitude.get(index));
                payload.put("longitude",longitude.get(index));
                payload.put("centerLatitude",centerLatitude);
                payload.put("centerLongitude",centerLongitude);
                payload.put("radius",radius);
                payload.put("x",x);
                payload.put("y",y);
                payload.put("id",carid);

                var message = MessageBuilder.withPayload(payload.toJSONString())
                        .setHeader(KafkaHeaders.TOPIC, TOPIC)
                        .setHeader(KafkaHeaders.KEY, String.valueOf(carid))
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
