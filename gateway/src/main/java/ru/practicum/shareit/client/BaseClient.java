package ru.practicum.shareit.client;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Базовый HTTP-клиент для взаимодействия с REST API.
 *
 * <p>Обеспечивает выполнение основных типов запросов:
 * <ul>
 *     <li>GET/POST - для чтения и создания данных</li>
 *     <li>PUT/PATCH - для полного и частичного обновления</li>
 *     <li>DELETE - для удаления ресурсов</li>
 * </ul>
 *
 * <p>Автоматически добавляет стандартные заголовки:
 * <ul>
 *     <li>Content-Type: application/json</li>
 *     <li>X-Sharer-User-Id для аутентификации</li>
 * </ul>
 *
 * <p>Использует Spring RestTemplate для выполнения запросов.
 */

public class BaseClient {
    protected final RestTemplate rest;


    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Отправляет GET-запрос без параметров и заголовка userId.
     *
     * @param path эндпоинт
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    /**
     * Отправляет GET-запрос с заголовком userId.
     *
     * @param path   эндпоинт
     * @param userId ID пользователя
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    /**
     * Отправляет GET-запрос с параметрами и заголовком userId.
     *
     * @param path       эндпоинт
     * @param userId     ID пользователя (может быть null)
     * @param parameters параметры запроса (может быть null)
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    /**
     * Отправляет POST-запрос с телом.
     *
     * @param path эндпоинт
     * @param body тело запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    /**
     * Отправляет POST-запрос с телом и заголовком userId.
     *
     * @param path   эндпоинт
     * @param userId ID пользователя
     * @param body   тело запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    /**
     * Отправляет POST-запрос с параметрами и телом.
     *
     * @param path       эндпоинт
     * @param userId     ID пользователя (может быть null)
     * @param parameters параметры запроса (может быть null)
     * @param body       тело запроса
     * @return ответ сервера
     */
    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String,
            Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    /**
     * Формирует и отправляет HTTP-запрос.
     *
     * @param method     HTTP-метод (GET, POST, etc.)
     * @param path       эндпоинт
     * @param userId     ID пользователя (может быть null)
     * @param parameters параметры запроса (может быть null)
     * @param body       тело запроса (может быть null)
     * @return ответ сервера
     * @throws HttpStatusCodeException в случае ошибки HTTP
     */
    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    /**
     * Создает стандартные заголовки для запроса.
     *
     * @param userId ID пользователя (может быть null)
     * @return заголовки
     */
    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    /**
     * Обрабатывает ответ от сервера.
     *
     * @param response исходный ответ
     * @return обработанный ответ
     */
    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
