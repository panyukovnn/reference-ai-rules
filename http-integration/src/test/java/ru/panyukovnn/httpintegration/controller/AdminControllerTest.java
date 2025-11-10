package ru.panyukovnn.httpintegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import ru.panyukovnn.httpintegration.AbstractWireMockTest;
import ru.panyukovnn.httpintegration.entity.User;
import ru.panyukovnn.httpintegration.util.TestFileUtil;

import java.util.Optional;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Демонстрационный тест с использованием wiremock
 * ОБРАЗЦОВЫЙ ПРИМЕР
 */
class AdminControllerTest extends AbstractWireMockTest {

    @Test
    void when_getAdmins_then_success() throws Exception {
        stubFindAdminUsersResponse();

        mockMvc.perform(get("/api/v1/admins")
                .queryParam("name", "John")
                .queryParam("limit", "10")
                .queryParam("offset", "0")
                .queryParam("orderBy", "name")
                .queryParam("sortBy", "ASC"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.body.length()").value(2))
            .andExpect(jsonPath("$.body[0].id").isNotEmpty())
            .andExpect(jsonPath("$.body[0].name").value("John Doe"))
            .andExpect(jsonPath("$.body[0].email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.body[0].isAdmin").value(true))
            .andExpect(jsonPath("$.body[1].id").isNotEmpty())
            .andExpect(jsonPath("$.body[1].name").value("Jane Doe"))
            .andExpect(jsonPath("$.body[1].email").value("jane.doe@example.com"))
            .andExpect(jsonPath("$.body[1].isAdmin").value(true));

        Optional<User> firstUser = userRepository.findByExternalId("user-123-abc");
        assertTrue(firstUser.isPresent());
        assertEquals("John Doe", firstUser.get().getName());
        assertEquals("john.doe@example.com", firstUser.get().getEmail());
        assertTrue(firstUser.get().getIsAdmin());

        Optional<User> secondUser = userRepository.findByExternalId("user-456-def");
        assertTrue(secondUser.isPresent());
        assertEquals("Jane Doe", secondUser.get().getName());
        assertEquals("jane.doe@example.com", secondUser.get().getEmail());
        assertTrue(secondUser.get().getIsAdmin());
    }

    @Test
    void when_getAdmins_identityReturnsEmptyList_then_emptyResponse() throws Exception {
        stubResponse(IDENTITY_USERS_URL, HttpMethod.POST, "[]");

        mockMvc.perform(get("/api/v1/admins")
                .queryParam("name", "NonExistent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.body.length()").value(0));
    }

    @Test
    void when_getAdmins_identityReturns400status_then_badRequest() throws Exception {
        stub400Response(IDENTITY_USERS_URL, HttpMethod.POST);

        mockMvc.perform(get("/api/v1/admins")
                .queryParam("name", "John"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.body").doesNotExist())
            .andExpect(jsonPath("$.errorMessage").value("Бизнес исключение: Клиентская ошибка при обращении к внешнему сервису: 400 Bad Request: [no body]"));
    }

    @Test
    void when_getAdmins_identityReturns500status_then_badRequest() throws Exception {
        stub500Response(IDENTITY_USERS_URL, HttpMethod.POST);

        mockMvc.perform(get("/api/v1/admins")
                .queryParam("name", "John"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.body").doesNotExist())
            .andExpect(jsonPath("$.errorMessage").value("Бизнес исключение: Серверная ошибка при обращении к внешнему сервису: 500 Server Error: [no body]"));
    }

    @Test
    void when_getAdmins_identityTimeout_then_badRequest() throws Exception {
        String requestBody = TestFileUtil.readFileFromResources("mock/controller/identity/getadmins/find-admin-users-request.json");
        String responseBody = TestFileUtil.readFileFromResources("mock/controller/identity/getadmins/find-admin-users-response.json");

        stubResponseWithDelay(IDENTITY_USERS_URL, HttpMethod.POST, responseBody, requestBody, 2001);

        mockMvc.perform(get("/api/v1/admins")
                .queryParam("name", "John"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.body").doesNotExist())
            .andExpect(jsonPath("$.errorMessage", containsStringIgnoringCase("Бизнес исключение: Таймаут обращения к внешнему сервису:")));
    }

    private void stubFindAdminUsersResponse() {
        String requestBody = TestFileUtil.readFileFromResources("mock/controller/identity/getadmins/find-admin-users-request.json");
        String responseBody = TestFileUtil.readFileFromResources("mock/controller/identity/getadmins/find-admin-users-response.json");

        stubResponseWithRequestBody(IDENTITY_USERS_URL, HttpMethod.POST, responseBody, requestBody);
    }
}