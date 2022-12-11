package aoc2022.input;

import java.util.stream.Stream;

public class StringInput implements Input {

    private final String value;

    public StringInput(String value) {
        this.value = value;
    }

    @Override
    public String raw() {
        return value;
    }

    @Override
    public Stream<String> lines() {
        return value.lines();
    }
}
