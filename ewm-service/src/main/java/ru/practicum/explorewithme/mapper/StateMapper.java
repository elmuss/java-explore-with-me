package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.model.State;

@UtilityClass
public class StateMapper {
    public State stateConvertFromString(String state) {
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
