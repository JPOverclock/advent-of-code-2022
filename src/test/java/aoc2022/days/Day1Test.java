package aoc2022.days;

import aoc2022.input.Input;
import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    private static final Input INPUT = new StringInput("""
            1000
            2000
            3000
                        
            4000
                        
            5000
            6000
                        
            7000
            8000
            9000
                        
            10000
            """);

    @Test
    void part1() {
        assertEquals("24000", new Day1().part1(INPUT));
    }

    @Test
    void part2() {
        assertEquals("45000", new Day1().part2(INPUT));
    }
}