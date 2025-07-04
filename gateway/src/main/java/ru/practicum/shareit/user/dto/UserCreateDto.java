package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Данные для регистрации нового пользователя.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;
}
