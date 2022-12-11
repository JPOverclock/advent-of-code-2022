package aoc2022.input;

import java.util.stream.Stream;

public interface Input {
    String raw();
    Stream<String> lines();
}
