package com.example.project.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("region_1depth_name") // 지역 1 Depth, 시도 단위
    private String depth1;

    @JsonProperty("region_2depth_name") // 지역 2 Depth, 구 단위
    private String depth2;

    @JsonProperty("region_3depth_name") // 지역 3 Depth, 동 단위
    private String depth3;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;

    @JsonProperty("distance")
    private double distance;
}
