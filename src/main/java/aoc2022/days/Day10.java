package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.List;
import java.util.Set;

public class Day10 implements Day {

    enum OpCode {
        NOOP(1),
        ADDX(2);

        final int cycles;

        OpCode(int cycles) {
            this.cycles = cycles;
        }
    }

    static abstract sealed class Instruction permits Noop, AddX {
        private int waitCycles;

        public Instruction(OpCode opCode) {
            this.waitCycles = opCode.cycles;
        }

        public boolean run(CPU cpu) {
            if (--waitCycles == 0) {
                execute(cpu);
                return true;
            } else {
                return false;
            }
        }

        protected abstract void execute(CPU cpu);
    }

    static final class Noop extends Instruction {
        public Noop() {
            super(OpCode.NOOP);
        }

        @Override
        protected void execute(CPU cpu) {
            // Do nothing
        }
    }

    static final class AddX extends Instruction {
        private final int operand;

        public AddX(String instruction) {
            super(OpCode.ADDX);
            this.operand = Integer.parseInt(instruction.split(" ")[1]);
        }

        @Override
        protected void execute(CPU cpu) {
            cpu.nextX += operand;
        }
    }

    public static class CPU {
        public int X = 1;
        public int nextX = 1;

        private final List<String> instructions;
        private int programCounter = 0;
        private Instruction currentInstruction;

        public CPU(List<String> instructions) {
            this.instructions = instructions;
        }

        public boolean tick() {
            if (programCounter >= instructions.size()) return false;

            if (currentInstruction == null) {
                X = nextX;
                currentInstruction = parseInstruction(instructions.get(programCounter));
            }

            if (currentInstruction.run(this)) {
                programCounter++;
                currentInstruction = null;
            }

            return true;
        }

        public void tickCycles(int cycles) {
            for (int i = 0; i < cycles; i++)
                tick();
        }

        public void reset() {
            this.X = 1;
            this.programCounter = 0;
            this.currentInstruction = null;
        }

        private Instruction parseInstruction(String instruction) {
            var parts = instruction.split(" ");

            return switch (parts[0]) {
                case "noop" -> new Noop();
                case "addx" -> new AddX(instruction);
                default -> throw new RuntimeException("Failed to parse instruction " + instruction);
            };
        }
    }

    public static class CRT {
        public static final int NUMBER_OF_SCANLINES = 6;
        public static final int SCANLINE_RESOLUTION = 40;
        private final boolean[] frameBuffer = new boolean[SCANLINE_RESOLUTION * NUMBER_OF_SCANLINES];
        private final CPU cpu;

        private int line = 0;
        private int column = 0;

        public CRT(CPU cpu) {
            this.cpu = cpu;
        }

        public void tick() {
            // CPU's X register contains the middle H-position of a 3-pixel-wide sprite
            var pixels = Set.of(cpu.X - 1, cpu.X, cpu.X + 1);

            frameBuffer[(line * SCANLINE_RESOLUTION) + column] = pixels.contains(column);

            column++;
            if (column >= SCANLINE_RESOLUTION) {
                column = 0;
                line++;
            }

            if (line >= 6) {
                line = 0;
            }
        }

        public String display() {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < NUMBER_OF_SCANLINES; i++) {
                for (int j = 0; j < SCANLINE_RESOLUTION; j++) {
                    builder.append(frameBuffer[(i * SCANLINE_RESOLUTION) + j] ? "#" : ".");
                }
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    @Override
    public String part1(Input input) {
        var cpu = new CPU(input.lines().toList());

        cpu.tickCycles(20);
        var a = 20 * cpu.X;
        cpu.tickCycles(40); // 60
        a += 60 * cpu.X;
        cpu.tickCycles(40); // 100
        a += 100 * cpu.X;
        cpu.tickCycles(40); // 140
        a += 140 * cpu.X;
        cpu.tickCycles(40); // 180
        a += 180 * cpu.X;
        cpu.tickCycles(40); // 220
        a += 220 * cpu.X;

        return String.valueOf(a);
    }

    @Override
    public String part2(Input input) {
        var cpu = new CPU(input.lines().toList());
        var crt = new CRT(cpu);

        while (cpu.tick()) {
            crt.tick();
        }

        return crt.display();
    }
}
