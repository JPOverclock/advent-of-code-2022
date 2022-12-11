package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

    private static final Input INPUT = new StringInput("""
                [D]   \s
            [N] [C]   \s
            [Z] [M] [P]
             1   2   3\s
                        
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
            """);

    @Test
    void part1() {
        assertEquals("CMZ", new Day5().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("MCD", new Day5().part2(INPUT));
    }
}