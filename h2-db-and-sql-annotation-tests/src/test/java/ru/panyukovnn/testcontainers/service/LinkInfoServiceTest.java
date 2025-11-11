package ru.panyukovnn.testcontainers.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.panyukovnn.testcontainers.AbstractTest;
import ru.panyukovnn.testcontainers.dto.FilterLinkInfoRequest;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Демонстрационный тест использования аннотации @Sql
 * ОБРАЗЦОВЫЙ ПРИМЕР
 */
class LinkInfoServiceTest extends AbstractTest {

    @Test
    @Transactional
    @Sql("classpath:sql/service/linkinfo/findbyfilter/link_info.sql")
    void when_findByFilter_filterByLinkPart_then_success() {
        FilterLinkInfoRequest filterLinkInfoRequest = FilterLinkInfoRequest.builder()
            .linkPart("google")
            .build();

        List<LinkInfoResponse> responses = linkInfoService.findByFilter(filterLinkInfoRequest);

        assertThat(responses).hasSize(1);
        LinkInfoResponse linkInfoResponse = responses.get(0);

        assertEquals(UUID.fromString("b0e2d11b-4a8f-419f-93c5-4cd83c42639d"), linkInfoResponse.getId());
        assertEquals("abcd1234", linkInfoResponse.getShortLink());
        assertEquals("https://google.com", linkInfoResponse.getLink());
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0), linkInfoResponse.getEndTime());
        assertEquals("Google", linkInfoResponse.getDescription());
        assertEquals(true, linkInfoResponse.getActive());
        assertEquals(5L, linkInfoResponse.getOpeningCount());
    }
}