package ru.panyukovnn.httpintegration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import ru.panyukovnn.httpintegration.AbstractWireMockTest;
import ru.panyukovnn.httpintegration.dto.AdminResponse;
import ru.panyukovnn.httpintegration.entity.User;
import ru.panyukovnn.httpintegration.util.TestFileUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends AbstractWireMockTest {

    @Autowired
    private UserService userService;

    @Test
    void when_findAdmins_then_success() {
        stubFindAdminUsersResponse();

        List<AdminResponse> result = userService.findAdmins("John", 10, 0, "name", "asc");

        assertNotNull(result);
        assertEquals(2, result.size());

        AdminResponse firstAdmin = result.get(0);
        assertNotNull(firstAdmin.getId());
        assertEquals("John Doe", firstAdmin.getName());
        assertEquals("john.doe@example.com", firstAdmin.getEmail());
        assertTrue(firstAdmin.getIsAdmin());

        AdminResponse secondAdmin = result.get(1);
        assertNotNull(secondAdmin.getId());
        assertEquals("Jane Doe", secondAdmin.getName());
        assertEquals("jane.doe@example.com", secondAdmin.getEmail());
        assertTrue(secondAdmin.getIsAdmin());

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
    void when_findAdmins_withNullAdmin_then_savedAsNonAdmin() {
        String name = "Test";

        String response = """
            [
              {
                "id": "user-789-ghi",
                "name": "Тестовый Пользователь",
                "email": "test.user@example.com",
                "admin": null
              }
            ]
            """;

        stubResponse(IDENTITY_USERS_URL, HttpMethod.GET, response);

        List<AdminResponse> result = userService.findAdmins(name, 10, 0, "name", "asc");

        assertEquals(1, result.size());

        Optional<User> user = userRepository.findByExternalId("user-789-ghi");
        assertTrue(user.isPresent());
        assertFalse(user.get().getIsAdmin());
    }

    @Test
    void when_findAdmins_emptyResponse_then_returnsEmptyList() {
        String name = "NonExistent";

        stubResponse(IDENTITY_USERS_URL, HttpMethod.GET, "[]");

        List<AdminResponse> result = userService.findAdmins(name, 10, 0, "name", "asc");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private void stubFindAdminUsersResponse() {
        String response = TestFileUtil.readFileFromResources("mock/response/identity/find-admin-users-response.json");

        stubResponse(IDENTITY_USERS_URL, HttpMethod.GET, response);
    }
}