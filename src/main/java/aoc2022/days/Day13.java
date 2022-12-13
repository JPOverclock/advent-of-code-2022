package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 implements Day {

    public static class PacketPair {
        private final Packet left;
        private final Packet right;

        public PacketPair(Packet left, Packet right) {
            this.left = left;
            this.right = right;
        }

        public static PacketPair fromString(String value) {
            var parts = value.split("\n");

            return new PacketPair(
                    Packet.parse(parts[0]),
                    Packet.parse(parts[1])
            );
        }

        public boolean isRightOrder() {
            return left.compareTo(right) < 0;
        }
    }

    public interface Packet extends Comparable<Packet> {
        static Packet parse(String value) {
            Stack<ListPacket> stack = new Stack<>();
            ListPacket current = null;
            StringBuilder workingString = new StringBuilder();

            for (char c : value.toCharArray()) {
                switch (c) {
                    case '[' -> {
                        if (current != null) {
                            // Flush working string...
                            var items = Arrays.stream(workingString.toString().split(",")).filter(m -> !m.isBlank()).map(Integer::parseInt).map(IntegerPacket::new).toList();
                            current.items.addAll(items);
                            workingString = new StringBuilder();
                        }
                        stack.push(current);
                        current = new ListPacket(new ArrayList<>());
                    }
                    case ']' -> {
                        // Flush working string
                        var items = Arrays.stream(workingString.toString().split(",")).filter(m -> !m.isBlank()).map(Integer::parseInt).map(IntegerPacket::new).toList();
                        if (current != null) current.items.addAll(items);
                        workingString = new StringBuilder();

                        // Complete list and add to parent
                        var parent = stack.pop();

                        if (parent == null) return current;
                        else {
                            parent.items.add(current);
                            current = parent;
                        }
                    }
                    default -> workingString.append(c);
                }
            }

            return current;
        }
    }

    public static class IntegerPacket implements Packet {
        private final Integer item;

        public IntegerPacket(Integer item) {
            this.item = item;
        }

        public String toString() {
            return item.toString();
        }

        @Override
        public int compareTo(Packet o) {
            if (o instanceof IntegerPacket i) {
                return item.compareTo(i.item);
            } else if (o instanceof ListPacket l) {
                // Cast self to list and compare
                return new ListPacket(List.of(this)).compareTo(l);
            }

            return 0;
        }
    }

    public static class ListPacket implements Packet {
        private final List<Packet> items;

        public ListPacket(List<Packet> items) {
            this.items = items;
        }

        public String toString() {
            return "[" + items.stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
        }

        @Override
        public int compareTo(Packet o) {
            if (o instanceof IntegerPacket i) {
                // Cast other to list and compare
                return compareTo(new ListPacket(List.of(i)));
            } else if (o instanceof ListPacket l) {
                // Compare lists
                var itemsToCheck = Math.min(items.size(), l.items.size());

                for (int i = 0; i < itemsToCheck; i++) {
                    var comparisonResult = items.get(i).compareTo(l.items.get(i));

                    if (comparisonResult < 0) return comparisonResult;
                    else if (comparisonResult > 0) return 1;
                }

                // Reached the end of the list, apply list size rule
                if (items.size() == l.items.size()) return 0;
                else return (l.items.size() < items.size()) ? 1 : -1;
            }

            return 0;
        }
    }

    @Override
    public String part1(Input input) {
        var pairs = Arrays.stream(input.raw().split("\n\n")).map(PacketPair::fromString).toList();

        var result = 0;
        for (int i = 0; i < pairs.size(); i++) {
            var pair = pairs.get(i);
            if (pair.isRightOrder()) {
                result += (i + 1);
            }
        }

        return String.valueOf(result);
    }

    @Override
    public String part2(Input input) {
        var decoderPacket1 = Packet.parse("[[2]]");
        var decoderPacket2 = Packet.parse("[[6]]");

        var keys = Stream.of(decoderPacket1, decoderPacket2);
        var ungroupedPackets = Arrays.stream(input.raw().split("\n\n")).map(PacketPair::fromString).flatMap(pair -> Stream.of(pair.left, pair.right));

        var packets = Stream.concat(keys, ungroupedPackets).sorted().toList();

        var decoderPacket1Index = packets.indexOf(decoderPacket1) + 1;
        var decoderPacket2Index = packets.indexOf(decoderPacket2) + 1;

        return String.valueOf(decoderPacket1Index * decoderPacket2Index);
    }
}
