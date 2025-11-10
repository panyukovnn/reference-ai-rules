package ru.panyukovnn.httpintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа от Identity API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityUserResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("admin")
    private Boolean admin;
}
