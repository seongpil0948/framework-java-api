package com.sixplus.server.api.service;

import com.sixplus.server.api.hotel.dto.HotelCreateRequest;
import com.sixplus.server.api.hotel.dto.HotelCreateResponse;
import com.sixplus.server.api.hotel.model.HotelEntity;
import com.sixplus.server.api.hotel.model.HotelRoomEntity;
import com.sixplus.server.api.hotel.repository.HotelRepository;
import com.sixplus.server.api.hotel.repository.HotelRoomRepository;
import com.sixplus.server.api.hotel.service.HotelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class HotelServiceTest {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @Test
    public void testCreateHotel() {
        //Given
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("test");
        request.setAddress("test address");
        request.setPhoneNumber("213-820-3642");
        request.setRoomCount(10);

        //When
        HotelCreateResponse response = hotelService.createHotel(request);
        HotelEntity hotelEntity = hotelRepository.findById(response.getHotelId()).orElse(null);
        List<HotelRoomEntity> hotelRoomEntities = hotelRoomRepository.findByHotelId(response.getHotelId());

        //Then
        Assertions.assertNotNull(hotelEntity);
        Assertions.assertEquals(request.getName(), hotelEntity.getName());
        Assertions.assertEquals(request.getAddress(), hotelEntity.getAddress());
        Assertions.assertEquals(request.getPhoneNumber(), hotelEntity.getPhoneNumber());
        Assertions.assertEquals(request.getRoomCount(), hotelEntity.getRoomCount());

        Assertions.assertEquals(request.getRoomCount(), hotelRoomEntities.size());
    }

    @Test
    public void testGetHotel(){
        hotelService.getHotelById(1L);
    }
}
