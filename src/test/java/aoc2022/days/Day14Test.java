package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

    private static final Input INPUT = new StringInput("""
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
            """);

    @Test
    void part1() {
        assertEquals("24", new Day14().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("93", new Day14().part2(INPUT));
    }
}