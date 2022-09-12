package ua.zaiets.elevator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Floor {

    private int floorNumber;
    private List<Person> people;
}
