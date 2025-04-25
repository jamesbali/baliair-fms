package com.bali.baliairfms.mapper;


import com.bali.baliairfms.dto.responsedto.BookingResponseDto;
import com.bali.baliairfms.model.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { PassengerMapper.class, FlightMapper.class })
public interface BookingMapper {

    BookingResponseDto toDto(Booking booking);

    List<BookingResponseDto> toDtoList(List<Booking> bookings);

}
