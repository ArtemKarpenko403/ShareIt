package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotations.ItemApi;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@ItemApi
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemDto itemDto
    ) {
        return itemService.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody ItemDto itemDto
    ) {
        return itemService.updateItem(itemId, ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam String text
    ) {
        return itemService.searchItems(text);
    }
}