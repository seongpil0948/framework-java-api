package com.sixplus.server.api.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelCreateResponse {

    private Long hotelId;

    private HotelCreateResponse() {

    }

    public static HotelCreateResponse of(Long hotelId) {
        HotelCreateResponse response = new HotelCreateResponse();
        response.hotelId = hotelId;
        return response;
    }
}
