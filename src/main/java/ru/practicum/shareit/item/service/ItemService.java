package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long ownerId, ItemDto itemDto);
    ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto);
    ItemDto getItemById(Long itemId);
    List<ItemDto> getAllItemsByOwner(Long ownerId);
    List<ItemDto> searchItems(String text);
    CommentResponseDto addComment(Long itemId, Long userId, CommentRequestDto requestDto);
    List<CommentResponseDto> getItemComments(Long itemId);
}