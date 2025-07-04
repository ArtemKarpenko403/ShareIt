package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.util.Collection;

/**
 * Сервис для работы с запросами на вещи.
 *
 * <p>Основные функции:
 * <ul>
 *     <li>Создание запросов на вещи</li>
 *     <li>Получение запросов пользователя</li>
 *     <li>Просмотр запросов других пользователей</li>
 *     <li>Поиск запроса по ID</li>
 * </ul>
 */

public interface ItemRequestService {

    ItemRequestShortDto addItemRequest(Long userId, ItemRequestCreateDto itemRequest);

    Collection<ItemRequestDto> getAllItemRequestsByOwnerId(Long userId);

    Collection<ItemRequestShortDto> getAllItemRequestsOfOtherUsers(Long userId);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);
}
