package ru.panyukovnn.unittest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    private String login;
    private ParticipantRole role;
    private boolean proxy;
}
