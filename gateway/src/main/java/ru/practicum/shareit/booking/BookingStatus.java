package ru.practicum.shareit.booking;

/**
 * Статусы бронирования:
 * <ul>
 *     <li>WAITING — ожидает подтверждения</li>
 *     <li>APPROVED — подтверждено владельцем</li>
 *     <li>REJECTED — отклонено владельцем</li>
 *     <li>CANCELED — отменено автором бронирования</li>
 * </ul>
 */
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
