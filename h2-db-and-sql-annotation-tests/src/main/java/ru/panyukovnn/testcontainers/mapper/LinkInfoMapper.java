package ru.panyukovnn.testcontainers.mapper;

import org.mapstruct.Mapper;
import ru.panyukovnn.testcontainers.dto.LinkInfoResponse;
import ru.panyukovnn.testcontainers.model.LinkInfo;

@Mapper(componentModel = "spring")
public interface LinkInfoMapper {

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
