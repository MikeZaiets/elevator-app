package ua.zaiets.elevator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    private long id;
    private int startFloor;
    private int targetFloor;
}
