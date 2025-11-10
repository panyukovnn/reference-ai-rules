package ru.panyukovnn.testcontainers.service;

import org.junit.jupiter.api.Test;
import ru.panyukovnn.testcontainers.AbstractTest;
import ru.panyukovnn.testcontainers.dto.CreateLinkInfoRequest;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;
import ru.panyukovnn.testcontainers.model.LinkInfo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class LinkInfoServiceTest extends AbstractTest {

    @Test
    void when_createLinkInfo_then_success() {
        LocalDateTime testEndTime = LocalDateTime.now();

        CreateLinkInfoRequest createShortLinkRequest = CreateLinkInfoRequest.builder()
            .link("test_link")
            .endTime(testEndTime)
            .description("test_description")
            .active(true)
            .build();

        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(createShortLinkRequest);

        assertNotNull(linkInfoResponse);
        assertNotNull(linkInfoResponse.getId());
        assertEquals("test_link", linkInfoResponse.getLink());
        assertEquals(8, linkInfoResponse.getShortLink().length());
        assertEquals(testEndTime, linkInfoResponse.getEndTime());
        assertEquals("test_description", linkInfoResponse.getDescription());
        assertEquals(0L, linkInfoResponse.getOpeningCount());
        assertTrue(linkInfoResponse.getActive());

        verify(linkInfoRepository).save(any(LinkInfo.class));
    }
}