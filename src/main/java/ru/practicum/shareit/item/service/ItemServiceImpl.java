package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private long  idCounter = 1;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto) {
        Item existingItem = items.get(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (!existingItem.getOwner().getId().equals(ownerId)) {
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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        ItemDto itemDto = ItemMapper.toItemDto(item);

        // Добавляем комментарии к DTO
        itemDto.setComments(getItemComments(itemId));

        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    dto.setComments(getItemComments(item.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchAvailableItems(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto,owner);
        // Если вещь создается по запросу
        if (itemDto.getRequestId() != null) {
            item.setRequestId(itemDto.getRequestId());
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public CommentResponseDto addComment(Long itemId, Long userId, CommentRequestDto requestDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        // Проверка, что пользователь действительно брал вещь в аренду
        boolean hasBooked = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                userId, itemId, LocalDateTime.now());
        if (!hasBooked) {
            throw new ValidationException("Нельзя оставить отзыв без бронирования");
        }

        Comment comment = CommentMapper.toComment(requestDto, item, author);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponseDto> getItemComments(Long itemId) {
        return commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}