package com.sixplus.server.api.controller;

import com.sixplus.server.api.domain.HotelEntity;
import com.sixplus.server.api.domain.HotelStatus;
import lombok.Getter;

@Getter
public class HotelResponse {

    public static final HotelResponse EMPTY = new HotelResponse();

    private Long hotelId;
    private HotelStatus hotelStatus;
    private String name;
    private String phoneNumber;
    private String address;

    private HotelResponse() {

    }

    public static HotelResponse of(HotelEntity hotelEntity) {

        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.hotelId = hotelEntity.getHotelId();
        hotelResponse.hotelStatus = hotelEntity.getStatus();
        hotelResponse.name = hotelEntity.getName();
        hotelResponse.address = hotelEntity.getAddress();
        hotelResponse.phoneNumber = hotelEntity.getPhoneNumber();

        return hotelResponse;
    }
}
