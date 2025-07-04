package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Данные для создания запроса на вещь.
 */
@Getter
@Setter
@Builder
public class ItemRequestCreateDto {
    @NotBlank(message = "Описание запроса обязательно")
    private String description;
}
