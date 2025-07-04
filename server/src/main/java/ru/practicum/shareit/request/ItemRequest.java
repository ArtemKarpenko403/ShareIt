package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Сущность запроса на вещь.
 *
 * <p>Содержит информацию о:
 * <ul>
 *     <li>Описании запрашиваемой вещи</li>
 *     <li>Дате и времени создания запроса</li>
 *     <li>Пользователе, создавшем запрос</li>
 * </ul>
 *
 * <p>Связана с таблицей 'requests' в базе данных.
 */

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(
            name = "created_date_time",
            nullable = false
    )
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "requester_id",
            nullable = false
    )
    private User requester;
}
