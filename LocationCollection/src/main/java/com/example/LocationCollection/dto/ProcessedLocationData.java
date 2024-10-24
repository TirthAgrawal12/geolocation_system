package com.example.LocationCollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedLocationData {

    private String id;
    private double latitude;
    private double longitude;
}
