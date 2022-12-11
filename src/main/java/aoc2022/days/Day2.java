package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;

public class Day2 implements Day {

    record Strategy(Play winsAgainst, Play losesAgainst, Play drawsAgainst) { }

    record Round(Play adversary, Play self) {
        public int score() {
            if (self.strategy.winsAgainst.equals(adversary)) {
                return 6 + self.score;
            } else if (self.strategy.drawsAgainst.equals(adversary)) {
                return 3 + self.score;
            } else {
                return self.score;
            }
        }
    }

    enum Play {
        ROCK,
        PAPER,
        SCISSORS;

        private Strategy strategy;
        private int score;

        static {
            ROCK.strategy = new Strategy(SCISSORS, PAPER, ROCK);
            ROCK.score = 1;
            PAPER.strategy = new Strategy(ROCK, SCISSORS, PAPER);
            PAPER.score = 2;
            SCISSORS.strategy = new Strategy(PAPER, ROCK, SCISSORS);
            SCISSORS.score = 3;
        }
    }

    private static Play parseAdversary(String adversary) {
        return switch (adversary) {
            case "A" -> Play.ROCK;
            case "B" -> Play.PAPER;
            case "C" -> Play.SCISSORS;
            default -> throw new RuntimeException("Cannot parse adversary move " + adversary);
        };
    }

    private static Play parsePart1Strategy(String strategy) {
        return switch (strategy) {
            case "X" -> Play.ROCK;
            case "Y" -> Play.PAPER;
            case "Z" -> Play.SCISSORS;
            default -> throw new RuntimeException("Cannot parse strategy " + strategy);
        };
    }

    private static Play parsePart2Strategy(Play adversary, String strategy) {
        return switch (strategy) {
            case "X" -> adversary.strategy.winsAgainst(); // Need to lose
            case "Y" -> adversary.strategy.drawsAgainst(); // Need to draw
            case "Z" -> adversary.strategy.losesAgainst(); // Need to win
            default -> throw new RuntimeException("Cannot parse strategy " + strategy);
        };
    }

    @Override
    public String part1(Input input) {
        int score = input.lines()
                .map(l -> l.split(" "))
                .map(p -> new Round(parseAdversary(p[0]), parsePart1Strategy(p[1])))
                .mapToInt(Round::score)
                .sum();

        return String.valueOf(score);
    }

    @Override
    public String part2(Input input) {
        int score = input.lines()
                .map(l -> l.split(" "))
                .map(p -> new Round(parseAdversary(p[0]), parsePart2Strategy(parseAdversary(p[0]), p[1])))
                .mapToInt(Round::score)
                .sum();

        return String.valueOf(score);
    }
}
