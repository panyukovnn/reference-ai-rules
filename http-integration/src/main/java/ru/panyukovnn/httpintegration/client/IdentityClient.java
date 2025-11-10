package ru.panyukovnn.httpintegration.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import ru.panyukovnn.httpintegration.dto.AdminSearchRequest;
import ru.panyukovnn.httpintegration.dto.IdentityUserResponse;
import ru.panyukovnn.httpintegration.exception.IntegrationException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Клиент для интеграции с внешним Identity API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityClient {

    private final RestClient restClient;

    @Value("${reference-ai-rules.integration.identity.host}")
    private String identityApiUrl;

    /**
     * Получить список администраторов из Identity API
     *
     * @param name    часть имени для поиска
     * @param limit   количество результатов
     * @param offset  смещение для пагинации
     * @param orderBy поле для сортировки
     * @param sortBy  направление сортировки
     * @return список пользователей администраторов
     * @throws IntegrationException при ошибках взаимодействия с Identity API
     */
    public List<IdentityUserResponse> findAdmins(String name, Integer limit, Integer offset,
                                                 String orderBy, String sortBy) {
        try {
            String url = identityApiUrl + "/identity/api/v1/users";

            AdminSearchRequest request = AdminSearchRequest.builder()
                .name(name)
                .limit(limit)
                .offset(offset)
                .orderBy(orderBy)
                .sortBy(sortBy)
                .build();

            log.info("Calling Identity API: {} with request body: {}", url, request);

            ResponseEntity<List<IdentityUserResponse>> response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<IdentityUserResponse>>() {
                });

            log.info("Identity API response: {} users found", response.getBody() != null ? response.getBody().size() : 0);
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (RestClientException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new IntegrationException("Таймаут обращения к внешнему сервису: " + e.getMessage(), e);
            }

            if (e instanceof RestClientResponseException responseException) {
                if (responseException.getStatusCode().is4xxClientError()) {
                    throw new IntegrationException("Клиентская ошибка при обращении к внешнему сервису: " + e.getMessage(), e);
                }

                if (responseException.getStatusCode().is5xxServerError()) {
                    throw new IntegrationException("Серверная ошибка при обращении к внешнему сервису: " + e.getMessage(), e);
                }
            }

            throw new IntegrationException("Ошибка при обращении к внешнему сервису: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IntegrationException("Критическое исключение при обращении к внешнему сервису: " + e.getMessage(), e);
        }
    }
}
