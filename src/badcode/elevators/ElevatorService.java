package badcode.elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ElevatorService {

    private List<Integer> fastElevators;
    private List<Integer> slowElevators;
    private List<Integer> outOfOrderElevators;
    private Map<Integer, Integer> mapOfFloorToElevator;

    private List<Integer> elevatorsInUse = new ArrayList<>();

    ElevatorService(List<Integer> fastElevators, List<Integer> slowElevators, List<Integer> outOfOrderElevators,
                    Map<Integer, Integer> mapOfFloorToElevator) {
        this.fastElevators = fastElevators;
        this.slowElevators = slowElevators;
        this.outOfOrderElevators = outOfOrderElevators;
        this.mapOfFloorToElevator = mapOfFloorToElevator;
    }

    public Integer pickAppropriateElevator(int sourceFloor, int destinationFloor) {
        Integer selectedElevator;
        List<Integer> elevatorsOnSameFloorAsSource = findElevatorsOnSameFloorAsSource(sourceFloor);
        elevatorsOnSameFloorAsSource.removeAll(outOfOrderElevators);
        if (elevatorsOnSameFloorAsSource.isEmpty()) {
            selectedElevator = findClosestElevator(sourceFloor);
        } else {
            if (destinationFloor - sourceFloor > 3) {
                List<Integer> fastElevatorsOnsameFloor = getFastElevator(elevatorsOnSameFloorAsSource);
                if (fastElevatorsOnsameFloor.isEmpty()) {
                    List<Integer> slowElevatorsOnSameFloor = getSlowElevator(elevatorsOnSameFloorAsSource);
                    selectedElevator = slowElevatorsOnSameFloor.get(0);
                } else {
                    selectedElevator = fastElevatorsOnsameFloor.get(0);
                }
            } else {
                List<Integer> slowElevatorsOnSameFloor = getSlowElevator(elevatorsOnSameFloorAsSource);
                if (slowElevatorsOnSameFloor.isEmpty()) {
                    List<Integer> fastElevatorsOnsameFloor = getFastElevator(elevatorsOnSameFloorAsSource);
                    selectedElevator = fastElevatorsOnsameFloor.get(0);
                } else {
                    selectedElevator = slowElevatorsOnSameFloor.get(0);
                }
            }
        }
        updateMapOfElevators(destinationFloor, selectedElevator);
        return selectedElevator;
    }

    private void updateMapOfElevators(int destinationFloor, Integer selectedElevator) {
        mapOfFloorToElevator.put(selectedElevator, destinationFloor);
    }

    private List<Integer> getSlowElevator(List<Integer> elevatorsOnSameFloorAsSource) {
        List<Integer> slowElevatorsOnSameFloor = new ArrayList<>(elevatorsOnSameFloorAsSource);
        slowElevatorsOnSameFloor.retainAll(slowElevators);
        return slowElevatorsOnSameFloor;
    }

    private List<Integer> getFastElevator(List<Integer> elevatorsOnSameFloorAsSource) {
        List<Integer> fastElevatorsOnSameFloor = new ArrayList<>(elevatorsOnSameFloorAsSource);
        fastElevatorsOnSameFloor.retainAll(fastElevators);
        return fastElevatorsOnSameFloor;
    }

    private Integer findClosestElevator(int sourceFloor) {
        Map<Integer, Integer> distanceFromFloorMap = new HashMap<>();
        int indexOfClosestElevator = mapOfFloorToElevator.entrySet()
                .stream()
                .map(entry -> {
                    distanceFromFloorMap.put(Math.abs(entry.getValue() - sourceFloor), entry.getKey());
                    return Math.abs(entry.getValue() - sourceFloor);
                })
                .collect(toList())
                .stream()
                .min(Integer::compareTo)
                .get();

        return distanceFromFloorMap.get(indexOfClosestElevator);
    }

    private List<Integer> findElevatorsOnSameFloorAsSource(int sourceFloor) {
        List<Integer> elevatorsOnSameFloor = new ArrayList<>();
        mapOfFloorToElevator.entrySet()
                .forEach(entry -> {
                    if (entry.getValue().equals(sourceFloor)) {
                        elevatorsOnSameFloor.add(entry.getKey());
                    }
                });
        return elevatorsOnSameFloor;
    }
}
