package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * Data transfer object, используемый для создания объекта User.
 */

@Getter
@Setter
@Builder
public class UserCreateDto {
    private String name;
    private String email;
}
