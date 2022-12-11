package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.HashSet;
import java.util.Set;

public class Day6 implements Day {

    @Override
    public String part1(Input input) {
        return String.valueOf(firstUniquePacketPosition(4, input.raw()));
    }

    @Override
    public String part2(Input input) {
        return String.valueOf(firstUniquePacketPosition(14, input.raw()));
    }

    private int firstUniquePacketPosition(int size, String input) {
        var characters = input.toCharArray();

        for (int i = 0; i < characters.length - size; i++) {
            Set<Character> x = new HashSet<>();

            for (int j = 0; j < size; j++) {
                x.add(characters[i + j]);
            }

            if (x.size() == size) {
                return i + size;
            }
        }

        return -1;
    }
}
