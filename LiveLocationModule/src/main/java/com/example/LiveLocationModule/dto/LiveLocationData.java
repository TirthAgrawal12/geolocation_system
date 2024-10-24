package com.example.LiveLocationModule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveLocationData {

    private String id;
    private double latitude;
    private double longitude;
}
