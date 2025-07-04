package ru.practicum.shareit.request.dto;

import lombok.*;

/**
 * Данные для создания запроса на вещь.
 */

@Getter
@Setter
@Builder
public class ItemRequestCreateDto {

    private String description;
}
