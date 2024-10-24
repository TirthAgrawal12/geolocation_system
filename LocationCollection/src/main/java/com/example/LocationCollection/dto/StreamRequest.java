package com.example.LocationCollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamRequest {
    private String id;
    private double latitude;
    private double longitude;
    private double centerLatitude;
    private double centerLongitude;
    private double radius;
    private double x;
    private double y;
}
