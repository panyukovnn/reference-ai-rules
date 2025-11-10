package ru.panyukovnn.unittest.service;

import org.springframework.stereotype.Service;
import ru.panyukovnn.unittest.dto.Coordinator;
import ru.panyukovnn.unittest.dto.CoordinatorDetails;

@Service
public class CoordinatorService {

    public CoordinatorDetails extractCoordinatorDetails(Coordinator coordinator) {
        return CoordinatorDetails.builder()
            .coordinator(coordinator)
            .build();
    }
}
