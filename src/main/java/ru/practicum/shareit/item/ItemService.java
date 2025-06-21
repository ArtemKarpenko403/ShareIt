package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long ownerId, ItemDto itemDto);
    ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto);
    ItemDto getItemById(Long itemId);  // Добавляем userId для проверки прав
    List<ItemDto> getAllItemsByOwner(Long ownerId);
    List<ItemDto> searchItems(String text);
}