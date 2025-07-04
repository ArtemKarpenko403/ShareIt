package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;
}
