package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToIdMap = new HashMap<>();
    private long idCounter = 1;  // Счётчик ID

    @Override
    public UserDto createUser(UserDto userDto) {
        // Проверяем уникальность email
        if (emailToIdMap.containsKey(userDto.getEmail())) {
            throw new ConflictException("Email уже используется");
        }

        User user = UserMapper.toUser(userDto);
        user.setId(idCounter++);

        // Сохраняем в обе мапы
        users.put(user.getId(), user);
        emailToIdMap.put(user.getEmail(), user.getId());

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        // Проверка на попытку изменить email на уже существующий
        if (userDto.getEmail() != null
                && !userDto.getEmail().equals(existingUser.getEmail())
                && emailToIdMap.containsKey(userDto.getEmail())) {
            throw new ConflictException("Email уже используется другим пользователем");
        }

        // Обновляем поля
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            // Удаляем старый email из мапы
            emailToIdMap.remove(existingUser.getEmail());
            existingUser.setEmail(userDto.getEmail());
            emailToIdMap.put(userDto.getEmail(), userId);
        }

        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

}