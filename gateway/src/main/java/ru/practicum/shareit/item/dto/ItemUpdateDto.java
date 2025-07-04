package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Данные для обновления вещи.
 */
@Getter
@Setter
@Builder
public class ItemUpdateDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
