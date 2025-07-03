package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;  // Краткая информация о вещи
import ru.practicum.shareit.user.dto.UserShortDto;  // Краткая информация о пользователе

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;  // WAITING, APPROVED, REJECTED, CANCELED
    private ItemShortDto item;     // id + название вещи
    private UserShortDto booker;  // id + имя пользователя
}