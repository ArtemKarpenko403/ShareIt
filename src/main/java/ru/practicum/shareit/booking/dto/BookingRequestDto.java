package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(message = "ID вещи обязательно")
    private Long itemId;

    @FutureOrPresent(message = "Дата начала должна быть в будущем или сейчас")
    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime start;

    @Future(message = "Дата окончания должна быть в будущем")
    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime end;
}