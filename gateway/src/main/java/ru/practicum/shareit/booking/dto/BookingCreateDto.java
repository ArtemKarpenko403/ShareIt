package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * DTO для создания нового бронирования.
 *
 * <p>Содержит:
 * <ul>
 *     <li>Даты начала и окончания (должны быть в будущем)</li>
 *     <li>ID вещи для бронирования</li>
 *     <li>Статус бронирования</li>
 * </ul>
 *
 * <p>Автоматически проверяет, что дата начала раньше даты окончания.
 */
@Getter
@Setter
@Builder
public class BookingCreateDto {
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    private BookingStatus status;

}
