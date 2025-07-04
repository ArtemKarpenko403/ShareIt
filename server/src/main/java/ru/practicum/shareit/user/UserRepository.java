package ru.practicum.shareit.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с пользователями.
 *
 * <p>Предоставляет CRUD-операции для сущности {@link User}:
 * <ul>
 *     <li>Стандартные операции сохранения, поиска и удаления</li>
 *     <li>Поддержка пагинации и сортировки</li>
 *     <li>Базовые методы работы со списками</li>
 * </ul>
 *
 * <p>Наследует функциональность {@link ListCrudRepository}.
 */

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {
}
