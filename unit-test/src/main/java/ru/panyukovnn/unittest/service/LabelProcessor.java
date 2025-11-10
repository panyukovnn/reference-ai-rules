package ru.panyukovnn.unittest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.panyukovnn.unittest.dto.CoordinatorDetails;
import ru.panyukovnn.unittest.dto.Participant;
import ru.panyukovnn.unittest.dto.ParticipantRole;
import ru.panyukovnn.unittest.dto.ProcessingContext;
import ru.panyukovnn.unittest.exception.BusinessException;
import ru.panyukovnn.unittest.mapper.CoordinatorMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Демонстрационный сервис для unit тестов
 * ОБРАЗЦОВЫЙ ПРИМЕР
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelProcessor {
    
    private final CoordinatorMapper coordinatorMapper;
    private final CoordinatorService coordinatorService;

    // здесь не показаны публичные и другие методы

    protected List<Participant> computeLabelParticipants(ProcessingContext processingContext, String requester, String resourceKey, String resourceName) {
        if (!CollectionUtils.isEmpty(processingContext.getParticipants())) {
            return List.of();
        }

        List<CoordinatorDetails> coordinatorDetailsList = processingContext.getCoordinatorInputs().stream()
                .map(coordinatorMapper::fromInput)
                .map(coordinatorService::extractCoordinatorDetails)
                .toList();

        boolean anyCoordinatorOrProxyIsRequester = coordinatorDetailsList.stream()
                .anyMatch(details -> requester.equalsIgnoreCase(details.getCoordinator().getLogin())
                        || (details.getProxy() != null && requester.equalsIgnoreCase(details.getProxy().getLogin())));

        if (anyCoordinatorOrProxyIsRequester) {
            log.info("Назначение метки '{}' для ресурса '{}'. Стюард метки или его представитель является инициатором, участники не будут добавлены", resourceName, resourceKey);

            return List.of();
        }
        
        List<Participant> participants = coordinatorDetailsList.stream()
                .map(coordinator -> {
                    List<Participant> coordinatorParticipants = new ArrayList<>();

                    if (coordinator.getProxy() != null) {
                        coordinatorParticipants.add(Participant.builder()
                                .login(coordinator.getProxy().getLogin())
                                .role(ParticipantRole.PROXY)
                                .proxy(true)
                                .build());
                    }

                    coordinatorParticipants.add(Participant.builder()
                            .login(coordinator.getCoordinator().getLogin())
                            .role(ParticipantRole.COORDINATOR)
                            .proxy(false)
                            .build());

                    return coordinatorParticipants;
                })
                .flatMap(Collection::stream)
                .toList();

        if (CollectionUtils.isEmpty(participants)) {
            throw new BusinessException("abc12345", "business",
                    "Назначение метки для ресурса '%s' невозможно, у метки '%s' нет стюардов".formatted(resourceKey, resourceName));
        }

        return participants;
    }
}