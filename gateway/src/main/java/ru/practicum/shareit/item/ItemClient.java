package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

/**
 * Клиент для работы с вещами (Item) через REST API.
 * Обращается к эндпоинтам /items микросервиса ShareIt.
 * Поддерживает операции:
 * <ul>
 *     <li>Добавление, обновление, получение вещей</li>
 *     <li>Поиск вещей</li>
 *     <li>Добавление комментариев</li>
 * </ul>
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    /**
     * Конструктор с настройкой REST-клиента.
     *
     * @param serverUrl URL сервера (из конфига ${shareit-server.url})
     * @param builder   билдер для RestTemplate
     */
    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Добавляет новую вещь.
     *
     * @param userId        ID владельца вещи
     * @param itemCreateDto данные для создания вещи
     * @return ответ сервера
     */
    public ResponseEntity<Object> addItem(Long userId, ItemCreateDto itemCreateDto) {
        return post("", userId, itemCreateDto);
    }

    /**
     * Обновляет существующую вещь.
     *
     * @param userId        ID владельца вещи
     * @param itemId        ID вещи
     * @param itemUpdateDto данные для обновления
     * @return ответ сервера
     */
    public ResponseEntity<Object> updateItem(long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        return patch("/" + itemId, userId, itemUpdateDto);
    }

    /**
     * Получает вещь по ID.
     *
     * @param userId ID пользователя, запрашивающего данные
     * @param itemId ID вещи
     * @return ответ сервера
     */
    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    /**
     * Получает все вещи владельца.
     *
     * @param ownerId ID владельца вещей
     * @return ответ сервера
     */
    public ResponseEntity<Object> getAllItemsByOwnerId(Long ownerId) {
        return get("", ownerId);
    }

    /**
     * Ищет вещи по текстовому запросу.
     *
     * @param userId ID пользователя
     * @param text   текст для поиска
     * @return ответ сервера
     */
    public ResponseEntity<Object> getItemsByTextQuery(Long userId, String text) {
        Map<String, Object> params = Map.of("text", text);
        return get("/search" + "?text={text}", userId, params);
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId           ID вещи
     * @param authorId         ID автора комментария
     * @param commentCreateDto данные комментария
     * @return ответ сервера
     */
    public ResponseEntity<Object> addComment(Long itemId, Long authorId, CommentCreateDto commentCreateDto) {
        return post("/" + itemId + "/comment", authorId, commentCreateDto);
    }
}
