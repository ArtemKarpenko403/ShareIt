package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data transfer object объекта Comment, используемый для ответа на запросы.
 */

@Getter
@Setter
@Builder
public class CommentPartialDto {
    private long id;
    private String text;
    private LocalDateTime created;
    private String authorName;
}
