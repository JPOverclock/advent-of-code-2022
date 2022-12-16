package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 implements Day {

    private final long part1Line;
    private final long part2Range;

    public Day15(long part1Line) {
        this.part1Line = part1Line;
        this.part2Range = part1Line * 2;
    }

    public record Position(long x, long y) {
        public long manhattanDistance(Position other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    public record Sensor(Position position, Position closestBeacon, long range) {
        public Stream<Position> coverageAtHeight(long yPosition) {
            // Calculate vertical distance to y position
            var positionAtHeight = new Position(position.x, yPosition);
            var distanceToHeight = position.manhattanDistance(positionAtHeight);
            var maxDistanceCoverage = position.manhattanDistance(closestBeacon);

            if (distanceToHeight > maxDistanceCoverage) return Stream.empty();

            // Return the point at height + opposite X axis points that still fulfill the distance
            var affordableDistance = maxDistanceCoverage - distanceToHeight;

            List<Position> positions = new ArrayList<>();
            positions.add(positionAtHeight);

            for (long i = 1; i <= affordableDistance; i++) {
                positions.add(new Position(position.x - i, yPosition));
                positions.add(new Position(position.x + i, yPosition));
            }

            return positions.stream();
        }

        public Stream<Position> outerPerimeter() {
            // 4 cardinal points at range + 1
            var outer = range + 1;
            Position left = new Position(position.x - outer, position.y);
            Position right = new Position(position.x + outer, position.y);
            Position top = new Position(position.x, position.y - outer);
            Position bottom = new Position(position.x, position.y + outer);

            // Walking alongside the edges produces the perimeter
            Set<Position> edge = new HashSet<>();

            edge.add(left);
            edge.add(right);
            edge.add(top);
            edge.add(bottom);

            // Left to top
            for (long x = left.x + 1, y = left.y - 1; y > top.y; y--,x++) {
                edge.add(new Position(x, y));
            }

            // Top to right
            for (long x = top.x + 1, y = top.y + 1; y < right.y; y++,x++) {
                edge.add(new Position(x, y));
            }

            // Right to bottom
            for (long x = right.x - 1, y = right.y + 1; y < bottom.y; y++,x--) {
                edge.add(new Position(x, y));
            }

            // Bottom to left
            for (long x = bottom.x - 1, y = bottom.y - 1; y > left.y; y--,x--) {
                edge.add(new Position(x, y));
            }

            return edge.stream();
        }

        public boolean isOutside(Position position) {
            return this.position.manhattanDistance(position) > range;
        }

        public static Sensor fromString(String value) {
            var parts = value.split(":");
            var leftComponents = parts[0].split("at ")[1].split(", ");
            var rightComponents = parts[1].split("at ")[1].split(", ");

            var sensorPosition = new Position(Long.parseLong(leftComponents[0].substring(2)), Long.parseLong(leftComponents[1].substring(2)));
            var beaconPosition = new Position(Long.parseLong(rightComponents[0].substring(2)), Long.parseLong(rightComponents[1].substring(2)));

            return new Sensor(sensorPosition, beaconPosition, sensorPosition.manhattanDistance(beaconPosition));
        }
    }

    @Override
    public String part1(Input input) {
        var sensors = input.lines().map(Sensor::fromString).toList();
        var coveredPositions = sensors.stream()
                .flatMap(s -> s.coverageAtHeight(part1Line))
                .collect(Collectors.toSet());
        var beaconPositions = sensors.stream()
                .map(Sensor::closestBeacon)
                .collect(Collectors.toSet());

        coveredPositions.removeAll(beaconPositions);
        return String.valueOf(coveredPositions.size());
    }

    @Override
    public String part2(Input input) {
        var sensors = input.lines().map(Sensor::fromString).toList();

        var position = sensors.stream()
                .flatMap(Sensor::outerPerimeter)
                .filter(p -> p.x >= 0 && p.x <= part2Range && p.y >= 0 && p.y <= part2Range)
                .filter(p -> sensors.stream().allMatch(s -> s.isOutside(p)))
                .findFirst()
                .orElseThrow();

        return new BigInteger(String.valueOf(position.x))
                .multiply(new BigInteger("4000000"))
                .add(new BigInteger(String.valueOf(position.y)))
                .toString();
    }
}
