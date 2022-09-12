package ua.zaiets.elevator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BuildingData {

    private Elevator elevator;
    private List<Floor> floors;
}
