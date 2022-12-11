package aoc2022.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileInput implements Input {

    private final Path path;

    public FileInput(Path path) {
        this.path = path;
    }

    @Override
    public String raw() {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file " + path, e);
        }
    }

    @Override
    public Stream<String> lines() {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file " + path, e);
        }
    }
}
