package ua.zaiets.elevator;


import ua.zaiets.elevator.service.impl.ElevatorServiceImpl;
import ua.zaiets.elevator.service.impl.PeopleServiceImpl;
import ua.zaiets.elevator.service.impl.ReportServiceImpl;
import ua.zaiets.elevator.util.RandomTestDataGenerator;

public class ElevatorApplication {

    public static void main(String[] args) {
        var randomBuildingData = RandomTestDataGenerator.generateRandomBuildingData();
        var reportService = new ReportServiceImpl();
        var peopleService = new PeopleServiceImpl();

        new ElevatorServiceImpl(reportService, peopleService, randomBuildingData).runElevator();
    }
}
