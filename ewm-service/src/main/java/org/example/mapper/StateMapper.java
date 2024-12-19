package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.model.State;

@UtilityClass
public class StateMapper {
    public State stateConvert(String state) {
        return switch (state) {
            case "PENDING" -> State.PENDING;
            case "PUBLISHED" -> State.PUBLISHED;
            case "CANCELED" -> State.CANCELED;
            case "REJECTED" -> State.REJECTED;
            case "CONFIRMED" -> State.CONFIRMED;
            default -> null;
        };
    }
}
