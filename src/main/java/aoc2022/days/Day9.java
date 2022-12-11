package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Day9 implements Day {

    public enum Direction {
        UP(new Position(0, 1)),
        DOWN(new Position(0, -1)),
        LEFT(new Position(-1, 0)),
        RIGHT(new Position(1, 0));

        private final Position position;

        Direction(Position position) {
            this.position = position;
        }

        public Position position() {
            return position;
        }
    }

    public record Position(int x, int y) {

        private static final Position START = new Position(0, 0);

        boolean touches(Position other) {
            return Math.abs(verticalDistance(other)) <= 1 && Math.abs(horizontalDistance(other)) <= 1;
        }

        int horizontalDistance(Position other) {
            return other.x - x;
        }

        int verticalDistance(Position other) {
            return other.y - y;
        }

        Position add(Position other) {
            return new Position(x + other.x, y + other.y);
        }
    }

    public record Rope(Position[] nodes) {

        Position tail() {
            return nodes[nodes.length - 1];
        }

        Rope move(Direction direction) {
            // Copy the nodes over to a new array
            var newNodes = nodes.clone();

            // Move the head in the desired direction
            newNodes[0] = newNodes[0].add(direction.position());

            // Move all other nodes accordingly
            for (int i = 1; i < newNodes.length; i++) {
                // Move tail to still be touching
                var newHead = newNodes[i - 1];
                var newTail = newNodes[i];

                if (!newHead.touches(newTail)) {
                    var hDistance = newTail.horizontalDistance(newHead);
                    var vDistance = newTail.verticalDistance(newHead);

                    // Move 1 unit in the direction that has a distance greater than 1
                    int moveX = 0;
                    int moveY = 0;

                    if (Math.abs(hDistance) >= 1) {
                        moveX = hDistance / Math.abs(hDistance);
                    }

                    if (Math.abs(vDistance) >= 1) {
                        moveY = vDistance / Math.abs(vDistance);
                    }

                    newTail = newTail.add(new Position(moveX, moveY));
                }

                newNodes[i] = newTail;
            }

            return new Rope(newNodes);
        }

        public static Rope withNodes(int count) {
            return new Rope(IntStream.range(0, count).mapToObj(i -> Position.START).toList().toArray(new Position[0]));
        }
    }

    private Collection<Direction> parseDirections(Input input) {
        return input.lines().map(l -> l.split(" ")).flatMap(command -> {
           var times = Integer.parseInt(command[1]);
           var direction = switch (command[0]) {
               case "R" -> Direction.RIGHT;
               case "D" -> Direction.DOWN;
               case "U" -> Direction.UP;
               case "L" -> Direction.LEFT;
               default -> throw new RuntimeException("Invalid direction " + command[0]);
           };

           return IntStream.range(0, times).mapToObj(i -> direction);
        }).toList();
    }

    private int uniqueTailPositions(Collection<Direction> commands, Rope rope) {
        Set<Position> positions = new HashSet<>();
        Rope current = rope;
        positions.add(current.tail());

        for (Direction direction : commands) {
            current = current.move(direction);
            positions.add(current.tail());
        }

        return positions.size();
    }

    @Override
    public String part1(Input input) {
        return String.valueOf(uniqueTailPositions(parseDirections(input), Rope.withNodes(2)));
    }

    @Override
    public String part2(Input input) {
        return String.valueOf(uniqueTailPositions(parseDirections(input), Rope.withNodes(10)));
    }
}
