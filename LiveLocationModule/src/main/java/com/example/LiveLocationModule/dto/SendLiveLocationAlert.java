package com.example.LiveLocationModule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendLiveLocationAlert {
    private String id;
    private String message;
    private double latitude;
    private double longitude;
}
