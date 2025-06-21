package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingRequestDto dto, User booker, Item item) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserShortDto(booking.getBooker().getId(), booking.getBooker().getName()))
                .build();
    }
}