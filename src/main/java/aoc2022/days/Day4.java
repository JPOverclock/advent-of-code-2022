package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.Collection;

public class Day4 implements Day {

    public static class Range {
        private final Long min;
        private final Long max;

        public Range(Long min, Long max) {
            this.min = min;
            this.max = max;
        }

        public boolean containedIn(Range other) {
            return min >= other.min && max <= other.max;
        }

        public boolean intersects(Range other) {
            return !(max < other.min || min > other.max);
        }
    }

    public static class CleaningPair {
        private final Range first;
        private final Range second;

        public CleaningPair(Range first, Range second) {
            this.first = first;
            this.second = second;
        }

        public boolean hasFullOverlap() {
            return first.containedIn(second) || second.containedIn(first);
        }

        public boolean hasOverlap() {
            return first.intersects(second);
        }
    }

    private Collection<CleaningPair> cleaningPairs(Input input) {
        return input.lines().map(line -> {
           var elements = line.split(",");
           var first = elements[0].split("-");
           var second = elements[1].split("-");

           return new CleaningPair(
                   new Range(Long.parseLong(first[0]), Long.parseLong(first[1])),
                   new Range(Long.parseLong(second[0]), Long.parseLong(second[1]))
           );
        }).toList();
    }

    @Override
    public String part1(Input input) {
        return String.valueOf(cleaningPairs(input).stream().filter(CleaningPair::hasFullOverlap).count());
    }

    @Override
    public String part2(Input input) {
        return String.valueOf(cleaningPairs(input).stream().filter(CleaningPair::hasOverlap).count());
    }
}
