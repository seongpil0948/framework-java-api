package com.sixplus.server.api.hotel.controller;

import com.sixplus.server.api.hotel.dto.HotelCreateRequest;
import com.sixplus.server.api.hotel.dto.HotelCreateResponse;
import com.sixplus.server.api.hotel.dto.HotelResponse;
import com.sixplus.server.api.hotel.repository.HotelRepository;
import com.sixplus.server.api.hotel.service.HotelService;
import com.sixplus.server.api.user.log.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService, HotelRepository hotelRepository) {
        this.hotelService = hotelService;
    }
    @Loggable
    @PostMapping(path = "/create")
    public ResponseEntity<HotelCreateResponse> createHotel(@RequestBody HotelCreateRequest request) {
        HotelCreateResponse response = hotelService.createHotel(request);
        return ResponseEntity.ok(response);
    }

    @Loggable
    @GetMapping(path = "/{hotelId}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable("hotelId") Long hotelId) {
        HotelResponse hotelResponse = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelResponse);
    }

}
