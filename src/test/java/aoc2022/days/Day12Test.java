package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test {

    private static final Input INPUT = new StringInput("""
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
            """);

    @Test
    void part1() {
        assertEquals("31", new Day12().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("29", new Day12().part2(INPUT));
    }
}