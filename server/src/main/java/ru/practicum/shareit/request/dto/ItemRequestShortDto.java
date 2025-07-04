package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Data transfer object объекта ItemRequest, используемый для ответа на запросы.
 */

@Getter
@Setter
@Builder
public class ItemRequestShortDto {
    private long id;
    private String description;
    private LocalDateTime created;
}
