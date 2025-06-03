package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);      // Создание пользователя

    UserDto updateUser(Long userId, UserDto userDto);  // Обновление

    UserDto getUserById(Long userId);         // Получение по ID

    List<UserDto> getAllUsers();              // Список всех

    void deleteUser(Long userId);             // Удаление
}