package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    private static final Input INPUT = new StringInput("""
            A Y
            B X
            C Z
            """);

    @Test
    void part1() {
        assertEquals("15", new Day2().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("12", new Day2().part2(INPUT));
    }
}