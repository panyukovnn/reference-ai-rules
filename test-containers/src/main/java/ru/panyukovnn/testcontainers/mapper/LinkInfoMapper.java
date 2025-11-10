package ru.panyukovnn.testcontainers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.panyukovnn.testcontainers.dto.CreateLinkInfoRequest;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;
import ru.panyukovnn.testcontainers.model.LinkInfo;

@Mapper(componentModel = "spring")
public interface LinkInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingCount", constant = "0L")
    LinkInfo fromCreateRequest(CreateLinkInfoRequest request, String shortLink);

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
