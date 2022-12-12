package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.*;

public class Day12 implements Day {

    public record Position(int i, int j) {
        Position add(Position other) {
            return new Position(i + other.i, j + other.j);
        }
    }

    public static class Elevation {

        private final Position position;
        private final int height;
        private final Set<Elevation> siblings;

        public Elevation(Position position, int height, Set<Elevation> siblings) {
            this.position = position;
            this.height = height;
            this.siblings = siblings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Elevation elevation = (Elevation) o;
            return position.equals(elevation.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }

    public static class HeightMap {

        private final Map<Position, Elevation> elevations;
        private final Position startingPosition;
        private final Position endingPosition;

        private boolean graphComputed = false;

        public HeightMap(Position startingPosition, Position endingPosition, Map<Position, Elevation> elevations) {
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
            this.elevations = elevations;
        }

        private void computeGraph() {
            if (graphComputed) return;

            List<Position> kernel = List.of(
                    new Position(-1, 0),
                    new Position(0, -1), new Position(0, 1),
                    new Position(1, 0));

            for (Elevation e : elevations.values()) {
                // Add siblings based on kernel
                for (Position p : kernel) {
                    Position siblingPosition = e.position.add(p);

                    if (elevations.containsKey(siblingPosition)) {
                        e.siblings.add(elevations.get(siblingPosition));
                        elevations.get(siblingPosition).siblings.add(e);
                    }
                }
            }

            graphComputed = true;
        }

        public int shortestPathToEnd(Elevation start) {
            Map<Elevation, Integer> distances = new HashMap<>();
            Stack<Elevation> queue = new Stack<>();

            for (Elevation e : elevations.values()) {
                distances.put(e, Integer.MAX_VALUE);
                queue.add(e);
            }

            distances.put(elevations.get(start.position), 0);

            while (!queue.isEmpty()) {
                final var test = queue.stream().min(Comparator.comparing(distances::get)).orElseThrow();
                queue.remove(test);

                var siblings = test.siblings.stream().filter(sibling -> {
                    var heightDifference = sibling.height - test.height;
                    return heightDifference <= 1;
                }).toList();

                for (var sibling : siblings) {

                    if (queue.contains(sibling)) {
                        var alt = distances.get(test) + 1;

                        if (alt < distances.get(sibling)) {
                            distances.put(sibling, alt);
                        }
                    }
                }
            }

            // Backtrack to find distance to end node
            var node = elevations.get(endingPosition);
            return distances.get(node);
        }

        public static HeightMap fromString(String value) {
            String[] lines = value.split("\n");
            Map<Position, Elevation> elevations = new HashMap<>();
            Position startingPosition = new Position(0, 0);
            Position endingPosition = new Position(0, 0);

            for (int i = 0; i < lines.length; i++) {
                char[] characters = lines[i].strip().toCharArray();

                for (int j = 0; j < characters.length; j++) {
                    char character = characters[j];

                    if (character == 'S') {
                        startingPosition = new Position(i, j);
                        character = 'a';
                    } else if (character == 'E') {
                        endingPosition = new Position(i, j);
                        character = 'z';
                    }

                    int elevationHeight = character - 'a';
                    var elevation = new Elevation(new Position(i, j), elevationHeight, new HashSet<>());

                    elevations.put(elevation.position, elevation);
                }
            }

            var heightMap = new HeightMap(startingPosition, endingPosition, elevations);
            heightMap.computeGraph();

            return heightMap;
        }
    }

    @Override
    public String part1(Input input) {
        var heightMap = HeightMap.fromString(input.raw());
        return String.valueOf(heightMap.shortestPathToEnd(heightMap.elevations.get(heightMap.startingPosition)));
    }

    @Override
    public String part2(Input input) {
        var heightMap = HeightMap.fromString(input.raw());
        var minDistance = heightMap.elevations.values().stream()
                .filter(e -> e.height == 0)
                .parallel()
                .mapToInt(heightMap::shortestPathToEnd)
                .filter(h -> h > 0)
                .min()
                .orElseThrow();

        return String.valueOf(minDistance);
    }
}
