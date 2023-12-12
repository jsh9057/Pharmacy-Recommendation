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
    /*
    address_name	String	전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨
    address_type	String	address_name의 값의 타입(Type)
                            다음 중 하나:
                            REGION(지명)
                            ROAD(도로명)
                            REGION_ADDR(지번 주소)
                            ROAD_ADDR(도로명 주소)
    x	String	X 좌표값, 경위도인 경우 경도(longitude)
    y	String	Y 좌표값, 경위도인 경우 위도(latitude)
    address	Address	지번 주소 상세 정보
    road_address	RoadAddress	도로명 주소 상세 정보
     */
    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;

}
