package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(Long userId, BookingRequestDto requestDto);
    BookingResponseDto approveBooking(Long bookingId, boolean approved, Long ownerId);
    BookingResponseDto getBookingById(Long bookingId, Long userId);
    List<BookingResponseDto> getUserBookings(Long userId, String state);  // ALL, CURRENT, PAST, FUTURE и т.д.
    List<BookingResponseDto> getOwnerBookings(Long ownerId, String state);  // Бронирования вещей владельца
}