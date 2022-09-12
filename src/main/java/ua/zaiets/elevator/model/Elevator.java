package ua.zaiets.elevator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Elevator {

    public static final int PASSENGERS_LIMIT = 5;

    private int currentFloor;
    private long waitingInterval;
    private List<Person> passengers;
    private Direction direction;
}
