package ru.panyukovnn.httpintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Конфигурация для RestClient
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Создать RestClient bean с настройками timeout
     */
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
            .requestFactory(getRequestFactory())
            .build();
    }

    private ClientHttpRequestFactory getRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setReadTimeout(1000);
        factory.setConnectTimeout(1000);

        return factory;
    }
}
