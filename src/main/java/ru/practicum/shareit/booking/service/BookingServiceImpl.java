package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto createBooking(Long userId, BookingRequestDto requestDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Владелец не может бронировать свою вещь");
        }

        Booking booking = BookingMapper.toBooking(requestDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("Подтверждать бронь может только владелец");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование уже подтверждено или отклонено");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Просмотр брони доступен только автору или владельцу");
        }

        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "CURRENT":
                return bookingRepository.findCurrentBookings(userId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "REJECTED":
                BookingStatus status = BookingStatus.valueOf(state.toUpperCase());
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, status).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            default: // ALL
                return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "CURRENT":
                return bookingRepository.findCurrentOwnerBookings(ownerId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "REJECTED":
                BookingStatus status = BookingStatus.valueOf(state.toUpperCase());
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, status).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
            default: // ALL
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId).stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .collect(Collectors.toList());
        }
    }

}