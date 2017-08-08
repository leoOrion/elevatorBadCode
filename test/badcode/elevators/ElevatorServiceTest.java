package badcode.elevators;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ElevatorServiceTest {

    private Map<Integer, Integer> mapOfStartingFloorForElevator;
    private int firstElevator;
    private int secondElevator;
    private int thirdElevator;
    private int fourthElevator;
    private int fifthElevator;

    @Before
    public void setUp() throws Exception {
        mapOfStartingFloorForElevator = new HashMap<>();
        firstElevator = 1;
        secondElevator = 2;
        thirdElevator = 3;
        fourthElevator = 4;
        fifthElevator = 5;
        int firstFloor = 1;
        mapOfStartingFloorForElevator.put(firstElevator, firstFloor);
        mapOfStartingFloorForElevator.put(secondElevator, firstFloor);
        mapOfStartingFloorForElevator.put(thirdElevator, firstFloor);
        mapOfStartingFloorForElevator.put(fourthElevator, firstFloor);
        mapOfStartingFloorForElevator.put(fifthElevator, firstFloor);
    }

    @Test
    public void shouldPickUpFastElevatorForLongDistanceFloorsIfOnSameFloor() throws Exception {
        List<Integer> fastElevators = asList(firstElevator, thirdElevator);
        List<Integer> slowElevators = asList(secondElevator, fifthElevator, fourthElevator);
        List<Integer> outOfOrderElevators = asList(fourthElevator, fifthElevator, secondElevator);
        ElevatorService elevatorService = new ElevatorService(fastElevators, slowElevators, outOfOrderElevators, mapOfStartingFloorForElevator);
        int sourceFloor = 1;
        int destinationFloor = 7;

        int selectedElevator = elevatorService.pickAppropriateElevator(sourceFloor, destinationFloor);

        assertThat(selectedElevator, is(firstElevator));
    }

    @Test
    public void shouldPickUpSlowElevatorForShortDistanceFloorsIfOnSameFloor() throws Exception {
        List<Integer> fastElevators = asList(firstElevator, thirdElevator);
        List<Integer> slowElevators = asList(secondElevator, fifthElevator, fourthElevator);
        List<Integer> outOfOrderElevators = asList(fifthElevator, secondElevator);
        ElevatorService elevatorService = new ElevatorService(fastElevators, slowElevators, outOfOrderElevators, mapOfStartingFloorForElevator);
        int sourceFloor = 1;
        int destinationFloor = 3;

        int selectedElevator = elevatorService.pickAppropriateElevator(sourceFloor, destinationFloor);

        assertThat(selectedElevator, is(fourthElevator));
    }

    @Test
    public void shouldNotPickUpElevatorsThatAreOutOfOrder() throws Exception {
        List<Integer> fastElevators = asList(firstElevator, thirdElevator);
        List<Integer> slowElevators = asList(secondElevator, fifthElevator, fourthElevator);
        List<Integer> outOfOrderElevators = asList(fourthElevator, fifthElevator, firstElevator, secondElevator);
        ElevatorService elevatorService = new ElevatorService(fastElevators, slowElevators, outOfOrderElevators, mapOfStartingFloorForElevator);
        int sourceFloor = 1;
        int destinationFloor = 7;

        int selectedElevator = elevatorService.pickAppropriateElevator(sourceFloor, destinationFloor);

        assertThat(selectedElevator, is(thirdElevator));
    }

    @Test
    public void shouldPickFreeElevatorIfNoOtherElevatorIsAvailable() throws Exception {
        List<Integer> fastElevators = asList(firstElevator, thirdElevator);
        List<Integer> slowElevators = asList(secondElevator, fifthElevator, fourthElevator);
        List<Integer> outOfOrderElevators = asList(fourthElevator, thirdElevator, firstElevator, secondElevator);
        ElevatorService elevatorService = new ElevatorService(fastElevators, slowElevators, outOfOrderElevators, mapOfStartingFloorForElevator);
        int sourceFloor = 1;
        int destinationFloor = 7;

        int selectedElevator = elevatorService.pickAppropriateElevator(sourceFloor, destinationFloor);

        assertThat(selectedElevator, is(fifthElevator));
    }

    @Test
    public void shouldPickClosestElevatorIfNoElevatorOnSameFloor() throws Exception {
        List<Integer> fastElevators = asList(firstElevator, thirdElevator);
        List<Integer> slowElevators = asList(secondElevator, fifthElevator, fourthElevator);
        List<Integer> outOfOrderElevators = Collections.emptyList();

        setupElevators();

        ElevatorService elevatorService = new ElevatorService(fastElevators, slowElevators, outOfOrderElevators, mapOfStartingFloorForElevator);
        int sourceFloor = 3;
        int destinationFloor = 7;

        int selectedElevator = elevatorService.pickAppropriateElevator(sourceFloor, destinationFloor);

        assertTrue(selectedElevator == secondElevator || selectedElevator == fourthElevator);
    }

    private void setupElevators() {
        int firstFloor = 1;
        int secondFloor = 2;
        int thirdFloor = 3;
        int fourthFloor = 4;
        int fifthFloor = 5;
        int sixthFloor = 6;

        mapOfStartingFloorForElevator.put(firstElevator, firstFloor);
        mapOfStartingFloorForElevator.put(secondElevator, secondFloor);
        mapOfStartingFloorForElevator.put(thirdElevator, sixthFloor);
        mapOfStartingFloorForElevator.put(fourthElevator, fourthFloor);
        mapOfStartingFloorForElevator.put(fifthElevator, fifthFloor);
    }

}