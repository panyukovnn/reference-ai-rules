package ru.panyukovnn.testcontainers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LinkInfo extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Полная ссылка
     */
    private String link;
    /**
     * Короткая ссылка
     */
    private String shortLink;
    /**
     * Время окончания действия короткой ссылки
     */
    private LocalDateTime endTime;
    /**
     * Описание
     */
    private String description;
    /**
     * Количество открытий короткой ссылки
     */
    private Long openingCount;
    /**
     * Признак активности
     */
    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkInfo linkInfo = (LinkInfo) o;
        return Objects.equals(id, linkInfo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
