package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

/**
 * Клиент для работы с бронированиями через REST API.
 * Обращается к эндпоинтам /bookings микросервиса ShareIt.
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    /**
     * Конструктор с настройкой REST-клиента.
     *
     * @param serverUrl URL сервера (из конфига ${shareit-server.url})
     * @param builder   билдер для RestTemplate
     */
    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    /**
     * Создает новое бронирование.
     *
     * @param userId           ID пользователя
     * @param bookingCreateDto данные бронирования
     * @return ответ сервера
     */
    public ResponseEntity<Object> createBooking(Long userId, BookingCreateDto bookingCreateDto) {
        return post("", userId, bookingCreateDto);
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId    ID пользователя
     * @param bookingId ID бронирования
     * @param approved  true — подтвердить, false — отклонить
     * @return ответ сервера
     */
    public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByUser(Long userId, BookingState state) {
        Map<String, Object> parameters = Map.of("state", state.name());
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsForUserItems(Long userId, BookingState state) {
        Map<String, Object> params = Map.of("state", state.name());
        return get("/owner", userId, params);
    }
}
