package ru.panyukovnn.unittest.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panyukovnn.unittest.dto.Coordinator;
import ru.panyukovnn.unittest.dto.CoordinatorDetails;
import ru.panyukovnn.unittest.dto.CoordinatorInput;
import ru.panyukovnn.unittest.dto.Participant;
import ru.panyukovnn.unittest.dto.ParticipantRole;
import ru.panyukovnn.unittest.dto.ProcessingContext;
import ru.panyukovnn.unittest.exception.BusinessException;
import ru.panyukovnn.unittest.mapper.CoordinatorMapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ОБРАЗЦОВЫЙ ПРИМЕР
 */
@ExtendWith(MockitoExtension.class)
class LabelProcessorUnitTest {

    private final CoordinatorMapper coordinatorMapper = Mappers.getMapper(CoordinatorMapper.class);
    private final CoordinatorService coordinatorService = mock(CoordinatorService.class);

    private final LabelProcessor labelProcessor = new LabelProcessor(coordinatorMapper, coordinatorService);

    @Nested
    class ComputeLabelParticipants {

        @Test
        void when_computeLabelParticipants_withNonEmptyParticipants_then_returnsEmptyList() {
            Participant participant = new Participant();
            participant.setLogin("participantLogin");

            ProcessingContext processingContext = new ProcessingContext();
            processingContext.setParticipants(List.of(participant));

            String requester = "requester";
            String resourceKey = "resourceKey";
            String resourceName = "resourceName";

            List<Participant> participants = labelProcessor.computeLabelParticipants(processingContext, requester, resourceKey, resourceName);

            assertThat(participants, empty());
        }

        @Test
        void when_computeLabelCoordinatorParticipants_withEmptyCoordinators_then_throwsException() {
            ProcessingContext processingContext = new ProcessingContext();
            processingContext.setParticipants(List.of());
            processingContext.setCoordinatorInputs(List.of());

            String requester = "requester";
            String resourceKey = "resourceKey";
            String resourceName = "resourceName";

            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> labelProcessor.computeLabelParticipants(processingContext, requester, resourceKey, resourceName)
            );

            assertEquals(
                "Назначение метки для ресурса '%s' невозможно, у метки '%s' нет стюардов".formatted(resourceKey, resourceName),
                exception.getMessage()
            );
        }

        @Test
        void when_computeLabelCoordinatorParticipants_withCoordinatorIsParticipants_then_throwsException() {
            String requester = "requester";

            CoordinatorInput coordinatorInput = new CoordinatorInput();
            coordinatorInput.setLogin(requester);

            ProcessingContext processingContext = new ProcessingContext();
            processingContext.setParticipants(List.of());
            processingContext.setCoordinatorInputs(List.of(coordinatorInput));

            String resourceKey = "resourceKey";
            String resourceName = "resourceName";

            Coordinator coordinator = new Coordinator();
            coordinator.setLogin(requester);

            CoordinatorDetails coordinatorDetails = new CoordinatorDetails(coordinator, null);
            when(coordinatorService.extractCoordinatorDetails(coordinator)).thenReturn(coordinatorDetails);

            List<Participant> participants = labelProcessor.computeLabelParticipants(processingContext, requester, resourceKey, resourceName);

            assertThat(participants, empty());
        }

        @Test
        void when_computeLabelParticipants_withValidCoordinatorsAndWithoutProxy_then_success() {
            String coordinatorLogin = "coordinatorLogin";
            String requester = "requester";

            CoordinatorInput coordinatorInput = new CoordinatorInput();
            coordinatorInput.setLogin(coordinatorLogin);

            ProcessingContext processingContext = new ProcessingContext();
            processingContext.setParticipants(List.of());
            processingContext.setCoordinatorInputs(List.of(coordinatorInput));

            String resourceKey = "resourceKey";
            String resourceName = "resourceName";

            Coordinator coordinator = new Coordinator();
            coordinator.setLogin(coordinatorLogin);

            CoordinatorDetails coordinatorDetails = new CoordinatorDetails(coordinator, null);
            when(coordinatorService.extractCoordinatorDetails(coordinator)).thenReturn(coordinatorDetails);

            List<Participant> participants = labelProcessor.computeLabelParticipants(processingContext, requester, resourceKey, resourceName);

            assertThat(participants, hasSize(1));
            assertEquals(new Participant(coordinatorLogin, ParticipantRole.COORDINATOR, false), participants.get(0));
        }

        @Test
        void when_computeLabelParticipants_withValidCoordinatorAndProxy_then_success() {
            String coordinatorLogin = "coordinatorLogin";
            String proxyLogin = "proxyLogin";
            String requester = "requester";

            CoordinatorInput coordinatorInput = new CoordinatorInput();
            coordinatorInput.setLogin(coordinatorLogin);

            ProcessingContext processingContext = new ProcessingContext();
            processingContext.setParticipants(List.of());
            processingContext.setCoordinatorInputs(List.of(coordinatorInput));

            String resourceKey = "resourceKey";
            String resourceName = "resourceName";

            Coordinator coordinator = new Coordinator();
            coordinator.setLogin(coordinatorLogin);

            Coordinator proxy = new Coordinator();
            proxy.setLogin(proxyLogin);

            CoordinatorDetails coordinatorDetails = new CoordinatorDetails(coordinator, proxy);
            when(coordinatorService.extractCoordinatorDetails(coordinator)).thenReturn(coordinatorDetails);

            List<Participant> participants = labelProcessor.computeLabelParticipants(processingContext, requester, resourceKey, resourceName);

            assertThat(participants, hasSize(2));
            assertEquals(new Participant(proxyLogin, ParticipantRole.PROXY, true), participants.get(0));
            assertEquals(new Participant(coordinatorLogin, ParticipantRole.COORDINATOR, false), participants.get(1));
        }
    }
}