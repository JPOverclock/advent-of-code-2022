package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day9Test {

    private static final Input INPUT = new StringInput("""
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
            """);

    private static final Input LARGER_INPUT = new StringInput("""
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
            """);

    @Test
    void part1() {
        assertEquals("13", new Day9().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("1", new Day9().part2(INPUT));
        assertEquals("36", new Day9().part2(LARGER_INPUT));
    }
}