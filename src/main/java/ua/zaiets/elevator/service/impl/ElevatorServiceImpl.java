package ua.zaiets.elevator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ua.zaiets.elevator.model.BuildingData;
import ua.zaiets.elevator.model.Direction;
import ua.zaiets.elevator.model.Elevator;
import ua.zaiets.elevator.model.Floor;
import ua.zaiets.elevator.model.Person;
import ua.zaiets.elevator.service.ElevatorService;
import ua.zaiets.elevator.service.PeopleService;
import ua.zaiets.elevator.service.ReportService;

import java.util.Collections;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ElevatorServiceImpl implements ElevatorService {

    private final ReportService printerService;
    private final PeopleService peopleService;
    private final BuildingData buildingData;

    @Override
    public void runElevator() {
        var floorIterator = buildingData.getFloors().listIterator();
        buildingData.getElevator().setDirection(Direction.UP);
        moveUpElevator(floorIterator.next(), floorIterator);
    }

    @SneakyThrows
    private void moveUpElevator(Floor currentFloor, ListIterator<Floor> floorIterator) {
        var elevator = buildingData.getElevator();
        elevator.setCurrentFloor(currentFloor.getFloorNumber());

        if (floorIterator.hasNext()) {
            if (isTargetFloorUpper(currentFloor, elevator) || isExistPassengersOnUpperFloor(currentFloor)) {
                processMoveElevator(currentFloor, elevator);
                moveUpElevator(floorIterator.next(), floorIterator);
            } else {
                elevator.setDirection(Direction.DOWN);
                moveDownElevator(floorIterator.previous(), floorIterator);
            }
        } else {
            elevator.setDirection(Direction.DOWN);
            moveDownElevator(floorIterator.previous(), floorIterator);
        }
    }

    private boolean isTargetFloorUpper(Floor currentFloor, Elevator elevator) {
        if (elevator.getPassengers().isEmpty()) {
            return true;
        }
        return Collections.max(getPassengersTargetFloors(elevator)) > currentFloor.getFloorNumber();
    }

    private boolean isExistPassengersOnUpperFloor(Floor currentFloor) {
        return buildingData.getFloors()
                .stream()
                .flatMap(floor -> floor.getPeople().stream())
                .anyMatch(person -> person.getStartFloor() > currentFloor.getFloorNumber());
    }

    @SneakyThrows
    private void moveDownElevator(Floor currentFloor, ListIterator<Floor> floorIterator) {
        var elevator = buildingData.getElevator();
        elevator.setCurrentFloor(currentFloor.getFloorNumber());

        if (floorIterator.hasPrevious()) {
            if (isTargetFloorDowner(currentFloor, elevator) || isExistPassengersOnDownerFloor(currentFloor)) {
                processMoveElevator(currentFloor, elevator);
                moveDownElevator(floorIterator.previous(), floorIterator);
            } else {
                elevator.setDirection(Direction.UP);
                moveUpElevator(floorIterator.next(), floorIterator);
            }
        } else {
            elevator.setDirection(Direction.UP);
            moveUpElevator(floorIterator.next(), floorIterator);
        }
    }

    private boolean isTargetFloorDowner(Floor currentFloor, Elevator elevator) {
        if (elevator.getPassengers().isEmpty()) {
            return true;
        }
        return Collections.min(getPassengersTargetFloors(elevator)) < currentFloor.getFloorNumber();
    }

    private Set<Integer> getPassengersTargetFloors(Elevator elevator) {
        return elevator.getPassengers()
                .stream()
                .map(Person::getTargetFloor)
                .collect(Collectors.toSet());
    }

    private boolean isExistPassengersOnDownerFloor(Floor currentFloor) {
        return buildingData.getFloors()
                .stream()
                .flatMap(floor -> floor.getPeople().stream())
                .anyMatch(person -> person.getStartFloor() < currentFloor.getFloorNumber());
    }

    private void processMoveElevator(Floor currentFloor, Elevator elevator) throws InterruptedException {
        Thread.sleep(elevator.getWaitingInterval());
        peopleService.putPassengersOutElevator(currentFloor, buildingData);
        peopleService.putPassengersToElevator(currentFloor, buildingData);
        printerService.printStep(buildingData);
    }
}
