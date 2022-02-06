import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

public class Algorithm {
    private int width = Integer.MAX_VALUE;
    private List<Disk> disks = new ArrayList<>();
    private List<Pillar> pillars = new LinkedList<>();
    private double maxPossibleDistance = Double.MIN_VALUE;
    private Stack<Pillar> possibleStartingPillars = new Stack<>();

    public void start() {
        readSystemInput();
        findAllNeighboursOfPillars();

        int lowestCost = findLowestCostToDestination();
        if (lowestCost < Integer.MAX_VALUE) {
            System.out.println(lowestCost);
        } else {
            System.out.println("impossible");
        }
    }

    private int findLowestCostToDestination() {
        Pillar southRim = new Pillar(new Disk(0,0),0, 0);
        List<Pillar> neighbours = new ArrayList<>();

        while (!possibleStartingPillars.isEmpty()) {
            neighbours.add(possibleStartingPillars.pop());
        }

        southRim.setAdjacentPillars(neighbours);

        return dijkstra(southRim);
    }

    private int dijkstra(Pillar sourcePillar) {
        sourcePillar.setCost(sourcePillar.getDisk().getCost());
        Queue<Pillar> toBeEvaluatedPillars = new PriorityQueue<>();
        toBeEvaluatedPillars.add(sourcePillar);

        while (toBeEvaluatedPillars.size() != 0) {
            final Pillar currentPillar = toBeEvaluatedPillars.poll();
            if (currentPillar.getY() + currentPillar.getDisk().getRadius() >= width) {
                return currentPillar.getCost();
            }

            for (Pillar neighbour : currentPillar.getAdjacentPillars()) {
                int totalCost = currentPillar.getCost() + neighbour.getDisk().getCost();

                if (totalCost < neighbour.getCost()) {
                    neighbour.setCost(totalCost);
                    toBeEvaluatedPillars.add(neighbour);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private void findAllNeighboursOfPillars() {
        Stack<Pillar> pillars = new Stack<>();
        pillars.addAll(this.pillars);
        Pillar lastPillar = null;
        List<Pillar> reachablePillars = new ArrayList<>();

        while (!pillars.isEmpty()) {
            Pillar currentPillar = pillars.pop();
            final boolean isSamePillar = lastPillar != null && currentPillar.getX() == lastPillar.getX() && currentPillar.getY() == lastPillar.getY();

            if (isSamePillar) {
                final boolean currentPillarAndDiskCombinationIsWorse = lastPillar.getDisk().getRadius() >= currentPillar.getDisk().getRadius() && currentPillar.getDisk().getCost() >= lastPillar.getDisk().getCost();
                if (currentPillarAndDiskCombinationIsWorse) {
                    continue;
                }
            } else {
                reachablePillars = computeReachablePillars(currentPillar);
            }

            final boolean canReachStartingPoint = currentPillar.getY() - currentPillar.getDisk().getRadius() <= 0;
            if (canReachStartingPoint) {
                possibleStartingPillars.add(currentPillar);
            }

            findNeighbours(reachablePillars, currentPillar);

            lastPillar = currentPillar;
        }
    }

    private List<Pillar> computeReachablePillars(Pillar currentPillar) {
        List<Pillar> reachableVertices;
        reachableVertices = this.pillars.stream()
                .filter(pillar -> pillar.distanceToPillar(
                        currentPillar) <= maxPossibleDistance &&
                        !(currentPillar.getX() == pillar.getX() && currentPillar.getY() == pillar.getY()))
                .collect(Collectors.toList());
        return reachableVertices;
    }

    private void findNeighbours(List<Pillar> reachablePillars, Pillar currentPillar) {
        for (Pillar pillar : reachablePillars) {
            double distanceBetweenTwoVertices = currentPillar.distanceToPillar(pillar);

            if (distanceBetweenTwoVertices <= currentPillar.getDisk().getRadius() + pillar.getDisk().getRadius()) {
                currentPillar.getAdjacentPillars().add(pillar);
            }
        }
    }

    private void readSystemInput() {
        Scanner scanner = new Scanner(System.in);
        String[] parameters = scanner.nextLine().split(" ");
        int numberOfCoordinates = Integer.parseInt(parameters[0]);
        int numberOfDisks = Integer.parseInt(parameters[1]);
        List<String[]> coordinates = new ArrayList<>();
        this.width = Integer.parseInt(parameters[2]);

        parseCoordinates(scanner, numberOfCoordinates, coordinates);
        parseDisksAndSort(scanner, numberOfDisks);
        generatePillars(coordinates);
    }

    private void generatePillars(List<String[]> coordinates) {
        for (String[] coordinate : coordinates) {
            for (Disk disk : disks) {
                this.pillars.add(new Pillar(disk, Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1])));
            }
        }
    }

    private void parseDisksAndSort(Scanner scanner, int numberOfDisks) {
        for (int i = 0; i < numberOfDisks; i++) {
            String[] disk = scanner.nextLine().split(" ");
            int radius = Integer.parseInt(disk[0]);
            int cost = Integer.parseInt(disk[1]);
            if (radius * 2 > maxPossibleDistance) {
                maxPossibleDistance = radius * 2;
            }
            this.disks.add(new Disk(radius, cost));
        }

        this.disks.sort((o1, o2) -> {
                    int score = o1.getRadius() - o2.getRadius();
                    if (score != 0) {
                        return score;
                    }

                    score = o1.getCost() - o2.getCost();
                    if (score == 0) {
                        return score;
                    }

                    return score;
                }
        );
    }

    private void parseCoordinates(Scanner scanner, int numberOfCoordinates, List<String[]> coordinates) {
        for (int i = 0; i < numberOfCoordinates; i++) {
            String[] coordinate = scanner.nextLine().split(" ");
            coordinates.add(coordinate);
        }
    }
}
