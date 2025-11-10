package ru.panyukovnn.unittest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingContext {

    private List<Participant> participants;
    private List<CoordinatorInput> coordinatorInputs;
}
