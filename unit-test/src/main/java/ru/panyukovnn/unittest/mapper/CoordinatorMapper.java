package ru.panyukovnn.unittest.mapper;

import org.mapstruct.Mapper;
import ru.panyukovnn.unittest.dto.Coordinator;
import ru.panyukovnn.unittest.dto.CoordinatorInput;

@Mapper(componentModel = "spring")
public interface CoordinatorMapper {

    Coordinator fromInput(CoordinatorInput coordinatorInput);
}
