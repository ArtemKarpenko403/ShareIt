package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

/**
 * Реализация сервиса для работы с запросами на вещи.
 *
 * <p>Обеспечивает полный цикл операций с запросами:
 * <ul>
 *     <li>Создание новых запросов</li>
 *     <li>Поиск по идентификатору</li>
 *     <li>Получение запросов пользователя</li>
 *     <li>Просмотр запросов других пользователей</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *     <li>Все модифицирующие операции выполняются в транзакциях</li>
 *     <li>Использует ItemRequestRepository для доступа к данным</li>
 *     <li>Интегрируется с UserService для проверки пользователей</li>
 * </ul>
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestShortDto addItemRequest(Long userId, ItemRequestCreateDto itemRequestCreateDto) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
        ItemRequest itemRequest =
                itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestCreateDto, requester));
        return ItemRequestMapper.toItemRequestShortDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsByOwnerId(Long userId) {

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();
        List<Item> items = itemRepository.findByRequestIdIn(requestIds);

        return requests.stream()
                .map(r -> ItemRequestMapper.toItemRequestDto(r, items))
                .toList();
    }

    @Override
    public Collection<ItemRequestShortDto> getAllItemRequestsOfOtherUsers(Long userId) {

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId);
        return requests.stream()
                .map(ItemRequestMapper::toItemRequestShortDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found by id: " + requestId));
        List<Item> items = itemRepository.findByRequestIdIn(List.of(requestId));
        return ItemRequestMapper.toItemRequestDto(request, items);
    }
}
