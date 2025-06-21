package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCounter = 1;
    private final UserService userService;

    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        userService.getUserById(ownerId);

        Item item = ItemMapper.toItem(itemDto);
        item.setId(idCounter++);
        item.setOwnerId(ownerId);
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto) {
        Item existingItem = items.get(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("Редактировать может только владелец");
        }
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(existingItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(searchText) ||
                                item.getDescription().toLowerCase().contains(searchText)))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}