package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day3 implements Day {

    public record Rucksack(List<Character> items) {

        public Character itemInBothCompartments() {
            var firstCompartment = items.subList(0, items.size() / 2);
            var secondCompartment = items.subList(items.size() / 2, items.size());

            return firstCompartment.stream()
                    .distinct()
                    .filter(secondCompartment::contains)
                    .findFirst()
                    .orElseThrow();
        }

        static Rucksack fromString(String value) {
            return new Rucksack(value.chars().mapToObj(i -> (char) i).toList());
        }
    }

    public record ElfGroup(List<Rucksack> rucksacks) {
        public char intersectingItem() {
            return rucksacks.stream()
                    .map(Rucksack::items)
                    .flatMap(items -> items.stream().distinct())
                    .collect(Collectors.groupingBy(item -> item, HashMap::new, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() == rucksacks.size())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
        }
    }

    public static int itemPriority(char item) {
        if (item >= 'a' && item <= 'z') {
            return (item - 'a') + 1;
        } else if (item >= 'A' && item <= 'Z') {
            return (item - 'A') + 1 + 26;
        } else {
            throw new RuntimeException("Invalid item type " + item);
        }
    }

    @Override
    public String part1(Input input) {
        int result = input.lines()
                .map(Rucksack::fromString)
                .map(Rucksack::itemInBothCompartments)
                .mapToInt(Day3::itemPriority)
                .sum();

        return String.valueOf(result);
    }

    @Override
    public String part2(Input input) {
        final var groupSize = 3;
        final var groupCounter = new AtomicInteger();

        int result = input.lines()
                .map(Rucksack::fromString)
                .collect(Collectors.groupingBy(it -> groupCounter.getAndIncrement() / groupSize))
                .values()
                .stream()
                .map(ElfGroup::new)
                .map(ElfGroup::intersectingItem)
                .mapToInt(Day3::itemPriority)
                .sum();

        return String.valueOf(result);
    }
}
