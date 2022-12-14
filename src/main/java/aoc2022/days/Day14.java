package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day14 implements Day {

    public record Point(int x, int y) {
        public static Point fromString(String value) {
            var parts = value.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    public record Line(Point start, Point end) {

        public Stream<Point> points() {
            if (start.x == end.x) { // Vertical segment
                return IntStream.range(Math.min(start.y, end.y), Math.max(start.y + 1, end.y + 1)).mapToObj(y -> new Point(start.x, y));
            } else { // Horizontal segment
                return IntStream.range(Math.min(start.x, end.x), Math.max(start.x + 1, end.x + 1)).mapToObj(x -> new Point(x, start.y));
            }
        }

        public static Stream<Line> fromSegmentDefinition(String value) {
            var points = Arrays.stream(value.split(" -> ")).map(Point::fromString).toList();

            return IntStream.range(0, points.size() - 1)
                    .mapToObj(i -> new Line(points.get(i), points.get(i + 1)));
        }
    }

    public static class Simulation {
        private final Set<Point> rocks;
        private final Set<Point> sandGrains;

        private final int lowerBound;

        public Simulation(Collection<Line> lines) {
            this.rocks = lines.stream().flatMap(Line::points).collect(Collectors.toSet());
            this.lowerBound = rocks.stream().mapToInt(Point::y).max().orElseThrow();
            this.sandGrains = new HashSet<>();
        }

        public Point simulate() {
            // Grain of sand starts at point (500, 0)
            var position = new Point(500, 0);

            while (true) {
                var below = new Point(position.x, position.y + 1);

                if (below.y >= lowerBound + 1) {
                    // Hit the bottom, return null!
                    return null;
                } else if (hasObstacle(below)) {
                    // Obstacle found. Attempt to go below left
                    var downLeft = new Point(below.x - 1, below.y);
                    var downRight = new Point(below.x + 1, below.y);

                    if (!hasObstacle(downLeft)) {
                        position = downLeft;
                    } else if (!hasObstacle(downRight)) {
                        position = downRight;
                    } else {
                        // Particle has stopped, return position
                        sandGrains.add(position);
                        return position;
                    }
                } else {
                    position = below;
                }
            }
        }

        public Point simulateWithFloor() {
            // Grain of sand starts at point (500, 0)
            var position = new Point(500, 0);
            var floorLine = lowerBound + 2;

            // End condition
            if (sandGrains.contains(position)) return null;

            while (true) {
                var below = new Point(position.x, position.y + 1);

                if (below.y == floorLine) {
                    // Sand grain hit the floor; Stop and return position
                    sandGrains.add(position);
                    return position;
                } else if (hasObstacle(below)) {
                    // Obstacle found. Attempt to go below left
                    var downLeft = new Point(below.x - 1, below.y);
                    var downRight = new Point(below.x + 1, below.y);

                    if (!hasObstacle(downLeft)) {
                        position = downLeft;
                    } else if (!hasObstacle(downRight)) {
                        position = downRight;
                    } else {
                        // Particle has stopped, return position
                        sandGrains.add(position);
                        return position;
                    }
                } else {
                    position = below;
                }
            }
        }

        private boolean hasObstacle(Point position) {
            return rocks.contains(position) || sandGrains.contains(position);
        }

        public String toString() {
            var minY = 0;
            var minX = rocks.stream().mapToInt(Point::x).min().orElseThrow();
            var maxX = rocks.stream().mapToInt(Point::x).max().orElseThrow();

            StringBuilder builder = new StringBuilder();

            for (int y = minY; y <= lowerBound; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (rocks.contains(new Point(x, y))) {
                        builder.append('#');
                    } else if (sandGrains.contains(new Point(x, y))) {
                        builder.append('o');
                    } else {
                        builder.append('.');
                    }
                }
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    private static Collection<Line> parseInput(Input input) {
        return input.lines().flatMap(Line::fromSegmentDefinition).toList();
    }

    @Override
    public String part1(Input input) {
        var lines = parseInput(input);
        var simulation = new Simulation(lines);

        while(simulation.simulate() != null);

        return String.valueOf(simulation.sandGrains.size());
    }

    @Override
    public String part2(Input input) {
        var lines = parseInput(input);
        var simulation = new Simulation(lines);

        while(simulation.simulateWithFloor() != null);

        return String.valueOf(simulation.sandGrains.size());
    }
}
