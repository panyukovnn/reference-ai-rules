package ru.panyukovnn.testcontainers.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.panyukovnn.testcontainers.dto.CreateLinkInfoRequest;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;
import ru.panyukovnn.testcontainers.mapper.LinkInfoMapper;
import ru.panyukovnn.testcontainers.model.LinkInfo;
import ru.panyukovnn.testcontainers.repository.LinkInfoRepository;

@Service
@RequiredArgsConstructor
public class LinkInfoService {

    private final LinkInfoMapper linkInfoMapper;
    private final LinkInfoRepository linkInfoRepository;

    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest request) {
        String shortLink = RandomStringUtils.randomAlphanumeric(8);

        LinkInfo linkInfo = linkInfoMapper.fromCreateRequest(request, shortLink);

        linkInfoRepository.save(linkInfo);

        return linkInfoMapper.toResponse(linkInfo);
    }
}
