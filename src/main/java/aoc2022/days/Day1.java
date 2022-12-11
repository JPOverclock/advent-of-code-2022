package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Day1 implements Day {

    private record ElfBag(List<Integer> itemCalories) {
        public int totalCalories() {
            return itemCalories.stream().mapToInt(i -> i).sum();
        }
    }

    private static Collection<ElfBag> bags(Input input) {
        return Arrays.stream(input.raw().split("\n\n"))
                .map(section -> Arrays.stream(section.split("\n")).map(Integer::parseInt).toList())
                .map(ElfBag::new)
                .toList();
    }

    @Override
    public String part1(Input input) {
        int maxCalories = bags(input).stream()
                .mapToInt(ElfBag::totalCalories)
                .max()
                .orElse(0);

        return String.valueOf(maxCalories);
    }

    @Override
    public String part2(Input input) {
        int topThreeTotalCalories = bags(input).stream()
                .map(ElfBag::totalCalories)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToInt(i -> i)
                .sum();

        return String.valueOf(topThreeTotalCalories);
    }
}
