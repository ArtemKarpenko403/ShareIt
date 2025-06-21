package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Основные методы
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    // Методы для пользователя
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentBookings(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    // Методы для владельца
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentOwnerBookings(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    // Проверка завершенных бронирований
    boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end);
}