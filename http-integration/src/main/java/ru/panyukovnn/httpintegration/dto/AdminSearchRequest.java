package ru.panyukovnn.httpintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса поиска администраторов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSearchRequest {

    private String name;

    private Integer limit;

    private Integer offset;

    private String orderBy;

    private String sortBy;
}
