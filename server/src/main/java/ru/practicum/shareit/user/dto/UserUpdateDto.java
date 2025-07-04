package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * Data transfer object, используемый для обновления объекта User.
 */

@Getter
@Setter
@Builder
public class UserUpdateDto {
    private Long id;
    private String name;
    private String email;
}
