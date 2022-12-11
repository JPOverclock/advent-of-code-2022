package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 implements Day {

    public static abstract sealed class FileSystemEntry permits File, Directory {

        private final FileSystemEntry parent;
        private final String name;

        public FileSystemEntry(FileSystemEntry parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public String name() {
            return name;
        }

        public FileSystemEntry parent() {
            return parent;
        }

        public abstract BigInteger size();

        public abstract Collection<FileSystemEntry> children();

        public abstract void addChild(FileSystemEntry entry);

        public String fullyQualifiedName() {
            var parents = new LinkedList<FileSystemEntry>();
            FileSystemEntry current = this;

            while (current.parent() != null) {
                parents.add(current);
                current = current.parent();
            }

            Collections.reverse(parents);
            return parents.stream().map(FileSystemEntry::name).collect(Collectors.joining("/"));
        }
    }

    public static final class File extends FileSystemEntry {

        private final BigInteger size;

        public File(BigInteger size, String name, FileSystemEntry parent) {
            super(parent, name);
            this.size = size;
        }

        @Override
        public BigInteger size() {
            return size;
        }

        @Override
        public Collection<FileSystemEntry> children() {
            return List.of();
        }

        @Override
        public void addChild(FileSystemEntry entry) {
            // Do nothing
        }
    }

    public static final class Directory extends FileSystemEntry {

        private final List<FileSystemEntry> children;

        public Directory(String name, FileSystemEntry parent) {
            super(parent, name);
            this.children = new ArrayList<>();
        }

        @Override
        public BigInteger size() {
            return children.stream().map(FileSystemEntry::size).reduce(BigInteger.ZERO, BigInteger::add);
        }

        @Override
        public Collection<FileSystemEntry> children() {
            return children;
        }

        @Override
        public void addChild(FileSystemEntry entry) {
            children.add(entry);
        }
    }

    public static class FileSystem {
        private final FileSystemEntry root = new Directory("/", null);
        private FileSystemEntry current = root;

        private final Map<String, FileSystemEntry> directoryListing = new HashMap<>();

        public void interpret(String line) {
            if (line.startsWith("$")) {
                // Command
                var command = line.substring(2).split(" ");

                switch (command[0]) {
                    case "cd" -> changeDirectory(command[1]);
                    case "ls", default -> {
                    }
                }
            } else {
                var parts = line.split(" ");

                if (parts[0].equals("dir")) {
                    // Create a sub-directory
                    var directory = new Directory(parts[1], current);
                    current.addChild(directory);
                    directoryListing.put(directory.fullyQualifiedName(), directory);
                } else {
                    // Create a file
                    current.addChild(new File(new BigInteger(parts[0]), parts[1], current));
                }
            }
        }

        private void changeDirectory(String name) {
            if (name.equals("/")) {
                current = root;
            } else if (name.equals("..")) {
                current = current.parent();
            } else {
                // Look for a directory for a given name
                current = current.children().stream().filter(f -> f.name().equals(name)).findFirst().orElse(current);
            }
        }
    }

    private FileSystem buildFileSystem(Input input) {
       var filesystem = new FileSystem();
       input.lines().forEach(filesystem::interpret);

       return filesystem;
    }

    @Override
    public String part1(Input input) {
        BigInteger totalSize = buildFileSystem(input).directoryListing.values().stream()
                .map(FileSystemEntry::size)
                .filter(size -> size.compareTo(new BigInteger("100000")) <= 0)
                .reduce(BigInteger.ZERO, BigInteger::add);

        return totalSize.toString();
    }

    @Override
    public String part2(Input input) {
        var filesystem = buildFileSystem(input);
        var freeSpace = new BigInteger("70000000").subtract(filesystem.root.size());

        BigInteger totalSize = filesystem.directoryListing.values().stream()
                .map(FileSystemEntry::size)
                .sorted()
                .filter(s -> freeSpace.add(s).compareTo(new BigInteger("30000000")) >= 0)
                .findFirst()
                .orElse(BigInteger.ZERO);

        return totalSize.toString();
    }
}
