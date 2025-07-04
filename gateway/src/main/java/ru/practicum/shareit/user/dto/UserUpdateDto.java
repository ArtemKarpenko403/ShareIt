package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

/**
 * Данные для обновления пользователя.
 */
@Getter
@Setter
@Builder
public class UserUpdateDto {
    private Long id;
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;
}