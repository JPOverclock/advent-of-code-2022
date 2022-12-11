package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

    private static final Input INPUT = new StringInput("""
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
            """);

    @Test
    void part1() {
        assertEquals("2", new Day4().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("4", new Day4().part2(INPUT));
    }
}