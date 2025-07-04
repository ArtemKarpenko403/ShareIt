package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Статусы бронирования.
 *
 * <p>Доступные значения:
 * <ul>
 *     <li>ALL - все бронирования</li>
 *     <li>CURRENT - текущие</li>
 *     <li>PAST - завершенные</li>
 *     <li>FUTURE - будущие</li>
 *     <li>WAITING - ожидающие подтверждения</li>
 *     <li>REJECTED - отклоненные</li>
 * </ul>
 */
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> getBookingState(String stateParam) {
        try {
            return Optional.of(BookingState.valueOf(stateParam.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
