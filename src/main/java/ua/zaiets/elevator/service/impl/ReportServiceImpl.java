package ua.zaiets.elevator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ua.zaiets.elevator.model.BuildingData;
import ua.zaiets.elevator.model.Direction;
import ua.zaiets.elevator.model.Elevator;
import ua.zaiets.elevator.model.Floor;
import ua.zaiets.elevator.model.Person;
import ua.zaiets.elevator.service.ReportService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final AtomicInteger STEP_COUNT = new AtomicInteger(1);
    private static final int ELEVATOR_SHAFT_WIDTH = 16;

    private static final String STEP_TEMPLATE = "*** Step %d ***%n";
    private static final String ELEVATOR_SHAFT_TEMPLATE = "||%s|%-3d|%s%n";
    private static final String UP_STR_TEMPLATE = "^%14s^";
    private static final String DOWN_STR_TEMPLATE = "v%14sv";
    private static final String DELIMITER_COMA = ",";
    private static final String EMPTY_FILLER = " ";

    @Override
    @SneakyThrows
    public void printStep(BuildingData buildingData) {
        var elevator = buildingData.getElevator();

        System.out.printf(STEP_TEMPLATE, STEP_COUNT.getAndIncrement());
        for (int i = buildingData.getFloors().size() - 1; i >= 0; i--) {
            var floor = buildingData.getFloors().get(i);
            if (elevator.getCurrentFloor() == floor.getFloorNumber()) {
                printElevatorShaft(floor, formatElevatorForPrint(elevator));
            } else {
                printElevatorShaft(floor, EMPTY_FILLER.repeat(ELEVATOR_SHAFT_WIDTH));
            }
        }
    }

    private String formatElevatorForPrint(Elevator elevator) {
        var passengersString = passengersInElevatorToString(elevator.getPassengers());
        if (elevator.getDirection() == Direction.UP) {
            return String.format(UP_STR_TEMPLATE, passengersString);
        } else {
            return String.format(DOWN_STR_TEMPLATE, passengersString);
        }
    }

    private void printElevatorShaft(Floor floor, String elevatorShaft) {
        System.out.printf(ELEVATOR_SHAFT_TEMPLATE, elevatorShaft,
                floor.getFloorNumber(), passengersInElevatorToString(floor.getPeople()));
    }

    private String passengersInElevatorToString(List<Person> people) {
        return people
                .stream()
                .map(Person::getTargetFloor)
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER_COMA));
    }
}
