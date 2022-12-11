package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day5 implements Day {

    public static class Ship {
        private final Map<Integer, Stack<String>> stacks;

        public Ship(Map<Integer, Stack<String>> stacks) {
            this.stacks = stacks;
        }

        public String remove(Integer stackNumber) {
            return stacks.get(stackNumber).pop();
        }

        public void add(Integer stackNumber, String crate) {
            stacks.get(stackNumber).push(crate);
        }

        public String topRow() {
            StringBuilder value = new StringBuilder();

            for (int i = 0; i < stacks.size(); i++) {
                if (!stacks.get(i + 1).isEmpty()) {
                    value.append(stacks.get(i + 1).peek());
                }
            }

            return value.toString();
        }
    }

    public interface Crane {
        void interpret(Instruction instruction, Ship ship);
    }

    public static class Crane9000 implements Crane {
        @Override
        public void interpret(Instruction instruction, Ship ship) {
            for (int i = 0; i < instruction.getCount(); i++) {
                var crate = ship.remove(instruction.getSource());
                ship.add(instruction.getDestination(), crate);
            }
        }
    }

    public static class Crane9001 implements Crane {
        @Override
        public void interpret(Instruction instruction, Ship ship) {
            Stack<String> crates = new Stack<>();

            for (int i = 0; i < instruction.getCount(); i++) {
                crates.push(ship.remove(instruction.getSource()));
            }

            while (!crates.isEmpty()) {
                ship.add(instruction.getDestination(), crates.pop());
            }
        }
    }

    public static class Instruction {
        private final Integer source;
        private final Integer destination;
        private final Integer count;

        public Instruction(Integer source, Integer destination, Integer count) {
            this.source = source;
            this.destination = destination;
            this.count = count;
        }

        public Integer getSource() {
            return source;
        }

        public Integer getDestination() {
            return destination;
        }

        public Integer getCount() {
            return count;
        }
    }

    public static class LoadingPlan {
        private final Ship ship;
        private final List<Instruction> instructions;

        public LoadingPlan(Ship ship, List<Instruction> instructions) {
            this.ship = ship;
            this.instructions = instructions;
        }

        public void apply(Crane crane) {
            for (Instruction instruction : instructions) {
                crane.interpret(instruction, ship);
            }
        }

        public String topRow() {
            return ship.topRow();
        }

        public static LoadingPlan fromString(String value) {
            String[] parts = value.split("\n\n");
            String initialState = parts[0];
            String instructions = parts[1];

            // Create stacks
            LinkedList<String> stateLines = new LinkedList<>();
            initialState.lines().forEach(stateLines::push);

            int stackCount = stateLines.get(0).split("(?<=\\G....)").length;

            Map<Integer, Stack<String>> stacks = new HashMap<>();

            for (int i = 0; i < stackCount; i++) {
                stacks.put(i + 1, new Stack<>());
            }

            stateLines.stream().skip(1).forEach(l -> {
                String[] columns = l.split("(?<=\\G....)");

                for (int i = 0; i < columns.length; i++) {
                    var item = columns[i].strip();

                    if (item.startsWith("[")) {
                        stacks.get(i + 1).push(item.replaceAll("[\\[\\]]", ""));
                    }
                }
            });

            Ship ship = new Ship(stacks);

            // Parse instructions
            List<Instruction> instructionList = instructions.lines().map(l -> {
                String[] instructionParts = l.split(" ");

                return new Instruction(Integer.parseInt(instructionParts[3]), Integer.parseInt(instructionParts[5]), Integer.parseInt(instructionParts[1]));
            }).toList();

            return new LoadingPlan(ship, instructionList);
        }
    }

    @Override
    public String part1(Input input) {
        LoadingPlan plan = LoadingPlan.fromString(input.raw());

        plan.apply(new Crane9000());
        return plan.topRow();
    }

    @Override
    public String part2(Input input) {
        LoadingPlan plan = LoadingPlan.fromString(input.raw());

        plan.apply(new Crane9001());
        return plan.topRow();
    }
}
