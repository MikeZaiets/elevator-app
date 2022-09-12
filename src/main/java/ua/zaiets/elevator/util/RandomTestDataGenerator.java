package ua.zaiets.elevator.util;

import lombok.experimental.UtilityClass;
import ua.zaiets.elevator.model.BuildingData;
import ua.zaiets.elevator.model.Elevator;
import ua.zaiets.elevator.model.Floor;
import ua.zaiets.elevator.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class RandomTestDataGenerator {

    private final AtomicInteger PERSON_ID = new AtomicInteger(1);
    private final SplittableRandom RANDOM = new SplittableRandom();
    private final int FIRST_FLOOR = 1;
    private final int LAST_FLOOR = generateMaxFloor();

    private int generateMaxFloor() {
        var minBuildingFloorCount = 5;
        var maxBuildingFloorCount = 21;
        return RANDOM.nextInt(minBuildingFloorCount, maxBuildingFloorCount);
    }

    public BuildingData generateRandomBuildingData() {
        return BuildingData.builder()
                .floors(generateFloorsData())
                .elevator(generateElevatorData())
                .build();
    }

    private Elevator generateElevatorData() {
        var timeWaiting = 500;
        return Elevator.builder()
                .passengers(Collections.emptyList())
                .currentFloor(FIRST_FLOOR)
                .waitingInterval(timeWaiting)
                .passengers(new ArrayList<>())
                .build();
    }

    private List<Floor> generateFloorsData() {
        return IntStream.range(FIRST_FLOOR, LAST_FLOOR + 1)
                .mapToObj(RandomTestDataGenerator::fillFloorByPeople)
                .collect(Collectors.toList());
    }

    private Floor fillFloorByPeople(int floorNumber) {
        return Floor.builder()
                .floorNumber(floorNumber)
                .people(getPersonOnFloor(floorNumber))
                .build();
    }

    private List<Person> getPersonOnFloor(int floorNumber) {
        var minPeopleCountOnFloor = 0;
        var maxPeopleCountOnFloor = 10;
        return IntStream.range(minPeopleCountOnFloor, RANDOM.nextInt(maxPeopleCountOnFloor))
                .mapToObj(i -> Person.builder()
                        .id(PERSON_ID.getAndIncrement())
                        .startFloor(floorNumber)
                        .targetFloor(getTargetFloor(floorNumber))
                        .build())
                .collect(Collectors.toList());
    }

    private int getTargetFloor(int startFloor) {
        var targetFloor = startFloor;
        while (targetFloor == startFloor) {
            targetFloor = RANDOM.nextInt(FIRST_FLOOR, LAST_FLOOR + 1);
        }
        return targetFloor;
    }

    public Person redistributePerson(Person person, int lastFloor) {
        var currentFloor = person.getTargetFloor();
        var newTargetFloor = currentFloor;
        while (newTargetFloor == currentFloor) {
            newTargetFloor = RANDOM.nextInt(FIRST_FLOOR, lastFloor);
        }
        person.setStartFloor(currentFloor);
        person.setTargetFloor(newTargetFloor);
        return person;
    }
}
