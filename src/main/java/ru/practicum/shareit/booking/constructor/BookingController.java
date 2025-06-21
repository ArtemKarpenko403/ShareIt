package ru.practicum.shareit.booking.constructor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.createBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getOwnerBookings(ownerId, state);
    }
}