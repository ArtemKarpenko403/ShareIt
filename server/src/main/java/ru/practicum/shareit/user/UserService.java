package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

/**
 * Сервис для управления пользователями.
 *
 * <p>Определяет операции для работы с пользователями:
 * <ul>
 *     <li>Создание и обновление учетных записей</li>
 *     <li>Удаление пользователей</li>
 *     <li>Получение информации о пользователях</li>
 * </ul>
 *
 * <p>Связывает контроллер с репозиторием, инкапсулируя бизнес-логику.
 */

public interface UserService {

    UserFullDto addUser(UserCreateDto user);

    UserFullDto updateUser(Long userId, UserUpdateDto user);

    void removeUserById(Long userId);

    UserFullDto getUserById(Long userId);

    Collection<UserFullDto> getAllUsers();
}
