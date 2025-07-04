package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * Data transfer object объекта User, используемый для ответа на запросы.
 */

@Getter
@Setter
@Builder
public class UserFullDto {
    private long id;
    private String name;
    private String email;
}
