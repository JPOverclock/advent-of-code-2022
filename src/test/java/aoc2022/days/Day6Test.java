package aoc2022.days;

import aoc2022.input.StringInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

    @Test
    void part1() {
        assertEquals("7", new Day6().part1(new StringInput("mjqjpqmgbljsphdztnvjfqwrcgsmlb")));
        assertEquals("5", new Day6().part1(new StringInput("bvwbjplbgvbhsrlpgdmjqwftvncz")));
        assertEquals("6", new Day6().part1(new StringInput("nppdvjthqldpwncqszvftbrmjlhg")));
        assertEquals("10", new Day6().part1(new StringInput("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")));
        assertEquals("11", new Day6().part1(new StringInput("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")));
    }

    @Test
    void part2() {
        assertEquals("19", new Day6().part2(new StringInput("mjqjpqmgbljsphdztnvjfqwrcgsmlb")));
        assertEquals("23", new Day6().part2(new StringInput("bvwbjplbgvbhsrlpgdmjqwftvncz")));
        assertEquals("23", new Day6().part2(new StringInput("nppdvjthqldpwncqszvftbrmjlhg")));
        assertEquals("29", new Day6().part2(new StringInput("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")));
        assertEquals("26", new Day6().part2(new StringInput("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")));
    }
}