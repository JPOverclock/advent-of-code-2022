package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    private static final Input INPUT = new StringInput("""
            30373
            25512
            65332
            33549
            35390
            """);

    @Test
    void part1() {
        assertEquals("21", new Day8().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("8", new Day8().part2(INPUT));
    }
}