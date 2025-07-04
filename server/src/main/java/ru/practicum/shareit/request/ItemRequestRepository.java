package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с запросами на вещи.
 *
 * <p>Поддерживает:
 * <ul>
 *     <li>Стандартные CRUD-операции через JpaRepository</li>
 *     <li>Поиск по ID пользователя</li>
 *     <li>Сортировку по дате создания</li>
 * </ul>
 */

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(Long requesterId);
}
