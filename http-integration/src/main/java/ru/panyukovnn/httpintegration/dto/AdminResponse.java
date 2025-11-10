package ru.panyukovnn.httpintegration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа контроллера
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

    private Long id;

    private String name;

    private String email;

    private Boolean isAdmin;
}
