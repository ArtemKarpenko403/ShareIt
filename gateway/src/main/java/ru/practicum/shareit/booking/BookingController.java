package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

/**
 * Контроллер для работы с бронированиями через REST API.
 * Обрабатывает HTTP-запросы по пути /bookings.
 */

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/bookings",
        produces = "application/json"
)
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    /**
     * Создает новое бронирование.
     *
     * @param booking данные бронирования
     * @param userId  ID пользователя (из заголовка X-Sharer-User-Id)
     * @return ответ сервера
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingCreateDto booking,
                                                @RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("Запрос на добавление Booking: {}, user ID: {}", booking, userId);
        ResponseEntity<Object> re = bookingClient.createBooking(userId, booking);
        log.info("Успешно добавлен Booking: {}, user ID: {}", re.getBody(), userId);
        return re;
    }

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param bookingId ID бронирования
     * @param approved  true — подтвердить, false — отклонить
     * @param userId    ID пользователя (из заголовка X-Sharer-User-Id)
     * @return ответ сервера
     */
    @PatchMapping(path = "/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
                                                 @RequestParam(name = "approved") Boolean approved,
                                                 @RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("Запрос на обновление Booking по ID: {}, user ID: {}", bookingId, userId);
        ResponseEntity<Object> re = bookingClient.approveBooking(userId, bookingId, approved);
        log.info("Успешно обновлен Booking: {}, user ID: {}", re.getBody(), userId);
        return re;
    }

    @GetMapping(path = "/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
                                                 @RequestHeader(name = USER_ID_HEADER) long userId) {
        log.info("Запрос на получение Booking по ID: {}, user ID: {}", bookingId, userId);
        ResponseEntity<Object> re = bookingClient.getBookingById(userId, bookingId);
        log.info("Успешно получен Booking: {}, user ID: {}", re.getBody(), userId);
        return re;
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @RequestHeader(name = USER_ID_HEADER) long userId) {
        BookingState state = BookingState.getBookingState(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Bad booking state param: " + stateParam));
        log.info("Запрос на получение Bookings пользователя с ID: {}, и параметром state: {}", userId, state);
        ResponseEntity<Object> re = bookingClient.getBookingsByUser(userId, state);
        log.info("Успешно получены Bookings пользователя с ID: {}, и параметром state: {}", userId, state);
        return re;
    }

    @GetMapping(path = "/owner")
    public ResponseEntity<Object> getOwnerItemBookings(
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @RequestHeader(name = USER_ID_HEADER) long userId) {
        BookingState state = BookingState.getBookingState(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Bad booking state param: " + stateParam));
        log.info("Запрос на получение Bookings вещей пользователя с ID: {}, и параметром state: {}", userId, state);
        ResponseEntity<Object> re = bookingClient.getBookingsForUserItems(userId, state);
        log.info("Успешно получены Bookings вещей пользователя с ID: {}, и параметром state: {}", userId, state);
        return re;
    }
}
