package ua.zaiets.elevator.service.impl;

import ua.zaiets.elevator.model.BuildingData;
import ua.zaiets.elevator.model.Direction;
import ua.zaiets.elevator.model.Elevator;
import ua.zaiets.elevator.model.Floor;
import ua.zaiets.elevator.model.Person;
import ua.zaiets.elevator.service.PeopleService;
import ua.zaiets.elevator.util.RandomTestDataGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ua.zaiets.elevator.model.Elevator.PASSENGERS_LIMIT;

public class PeopleServiceImpl implements PeopleService {

    public void putPassengersToElevator(Floor floor, BuildingData buildingData) {
        var elevator = buildingData.getElevator();
        var passengers = elevator.getPassengers();
        var floorPeople = floor.getPeople();
        var newPassengers = floorPeople
                .stream()
                .filter(person -> compareDirections(person, elevator))
                .limit((PASSENGERS_LIMIT - passengers.size()))
                .collect(Collectors.toList());

        passengers.addAll(newPassengers);
        floorPeople.removeAll(newPassengers);
    }

    private boolean compareDirections(Person person, Elevator elevator) {
        var startFloor = person.getStartFloor();
        var targetFloor = person.getTargetFloor();
        var personDirection = Direction.UP;

        if (targetFloor < startFloor) {
            personDirection = Direction.DOWN;
        }
        if (elevator.getPassengers().isEmpty()) {
            elevator.setDirection(personDirection);
        }
        return Objects.equals(personDirection, elevator.getDirection());
    }

    public void putPassengersOutElevator(Floor floor, BuildingData buildingData) {
        var elevatorPassengers = buildingData.getElevator().getPassengers();
        var outPassengers = elevatorPassengers
                .stream()
                .filter(person -> person.getTargetFloor() == floor.getFloorNumber())
                .collect(Collectors.toList());
        elevatorPassengers.removeAll(outPassengers);
        redistributePeople(outPassengers, buildingData);
    }

    private void redistributePeople(List<Person> outPassengers, BuildingData buildingData) {
        outPassengers
                .stream()
                .map(person -> RandomTestDataGenerator.redistributePerson(person, buildingData.getFloors().size()))
                .forEach(person -> buildingData.getFloors().get(person.getStartFloor() - 1).getPeople().add(person));
    }
}
