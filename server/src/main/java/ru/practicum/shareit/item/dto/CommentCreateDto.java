package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Данные для создания комментария к вещи.
 */

@Getter
@Setter
@Builder
public class CommentCreateDto {
    private String text;
}
