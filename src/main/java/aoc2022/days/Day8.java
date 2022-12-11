package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Day8 implements Day {

    public record Location(int i, int j) { }

    public record Tree(Location location, int height) { }

    public static class Forest {
        private final int width;
        private final int height;

        private final Map<Location, Tree> trees;

        public Forest(Stream<String> input) {
            List<String> lines = input.toList();

            this.height = lines.size();
            this.width = lines.get(0).length();
            this.trees = new HashMap<>();

            for (int i = 0; i < lines.size(); i++) {
                var line = lines.get(i);
                int[] lineValues = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray();

                for (int j = 0; j < lineValues.length; j++) {
                    var location = new Location(i, j);
                    trees.put(location, new Tree(location, lineValues[j]));
                }
            }
        }

        public Set<Tree> visibleFromEdges() {
            Set<Tree> visible = new HashSet<>();

            visible.addAll(visibleFromLeft());
            visible.addAll(visibleFromRight());
            visible.addAll(visibleFromTop());
            visible.addAll(visibleFromBottom());

            return visible;
        }

        public Collection<Tree> visibleFromTop() {
            Set<Tree> visible = new HashSet<>();
            int maxHeight = 0;

            for (int j = 0; j < width; j++) {
                for (int i = 0; i < height; i++) {
                    var location = new Location(i, j);

                    if (i == 0 || trees.get(location).height > maxHeight) {
                        visible.add(trees.get(location));
                        maxHeight = trees.get(location).height;
                    }
                }
            }

            return visible;
        }

        public Collection<Tree> visibleFromBottom() {
            Set<Tree> visible = new HashSet<>();
            int maxHeight = 0;

            for (int j = 0; j < width; j++) {
                for (int i = height - 1; i >= 0; i--) {
                    var location = new Location(i, j);

                    if (i == (height - 1) || trees.get(location).height > maxHeight) {
                        visible.add(trees.get(location));
                        maxHeight = trees.get(location).height;
                    }
                }
            }

            return visible;
        }

        public Collection<Tree> visibleFromLeft() {
            Set<Tree> visible = new HashSet<>();
            int maxHeight = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    var location = new Location(i, j);

                    if (j == 0 || trees.get(location).height > maxHeight) {
                        visible.add(trees.get(location));
                        maxHeight = trees.get(location).height;
                    }
                }
            }

            return visible;
        }

        public Collection<Tree> visibleFromRight() {
            Set<Tree> visible = new HashSet<>();
            int maxHeight = 0;

            for (int i = 0; i < height; i++) {
                for (int j = width - 1; j >= 0; j--) {
                    var location = new Location(i, j);

                    if (j == (width - 1) || trees.get(location).height > maxHeight) {
                        visible.add(trees.get(location));
                        maxHeight = trees.get(location).height;
                    }
                }
            }

            return visible;
        }

        public int bestScenicScore() {
            return trees.keySet().stream().mapToInt(this::scenicScore).max().orElse(0);
        }

        public int scenicScore(Location location) {
            if (location.i == 0 || location.i == (height - 1) || location.j == 0 || location.j == (width - 1)) return 0;

            var tree = trees.get(location);

            // How many trees can we see to the left?
            int leftScore = 1;

            for (int j = location.j - 1; j > 0; j--) {
                if (trees.get(new Location(location.i, j)).height < tree.height) {
                    leftScore++;
                } else {
                    break;
                }
            }

            // ... and to the right?
            int rightScore = 1;

            for (int j = location.j + 1; j < (width - 1); j++) {
                if (trees.get(new Location(location.i, j)).height < tree.height) {
                    rightScore++;
                } else {
                    break;
                }
            }

            // ... now the top?
            int topScore = 1;

            for (int i = location.i - 1; i > 0; i--) {
                if (trees.get(new Location(i, location.j)).height < tree.height) {
                    topScore++;
                } else {
                    break;
                }
            }

            // ... finally the bottom.
            int bottomScore = 1;

            for (int i = location.i + 1; i < (height - 1); i++) {
                if (trees.get(new Location(i, location.j)).height < tree.height) {
                    bottomScore++;
                } else {
                    break;
                }
            }

            // Calculate the scenic score
            return leftScore * rightScore * bottomScore * topScore;
        }
    }

    @Override
    public String part1(Input input) {
        var forest = new Forest(input.lines());
        return String.valueOf(forest.visibleFromEdges().size());
    }

    @Override
    public String part2(Input input) {
        var forest = new Forest(input.lines());
        return String.valueOf(forest.bestScenicScore());
    }
}
