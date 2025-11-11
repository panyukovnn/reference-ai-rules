package ru.panyukovnn.testcontainers;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.panyukovnn.testcontainers.repository.LinkInfoRepository;
import ru.panyukovnn.testcontainers.service.LinkInfoService;

/**
 * Демонстрационный тест с использованием wiremock
 * ОБРАЗЦОВЫЙ ПРИМЕР
 */
@SpringBootTest
@ActiveProfiles("test")
public class AbstractTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected LinkInfoService linkInfoService;
    @SpyBean
    protected LinkInfoRepository linkInfoRepository;
}