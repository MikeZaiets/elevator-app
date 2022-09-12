package ua.zaiets.elevator.service;

import ua.zaiets.elevator.model.BuildingData;
import ua.zaiets.elevator.model.Floor;

public interface PeopleService {

    void putPassengersToElevator(Floor floor, BuildingData buildingData);

    void putPassengersOutElevator(Floor currentFloor, BuildingData buildingData);

}
