package aoc2022.days;

import aoc2022.Day;
import aoc2022.input.Input;
import aoc2022.util.Math;

import java.math.BigInteger;
import java.util.*;

public class Day11 implements Day {

    public record MonkeyAction(BigInteger item, Integer destinationMonkeyId) { }

    public static class Test {

        private final BigInteger divisibleBy;
        private final Integer monkeyToThrowIfTrue;
        private final Integer monkeyToThrowIfFalse;

        public Test(BigInteger divisibleBy, Integer monkeyToThrowIfTrue, Integer monkeyToThrowIfFalse) {
            this.divisibleBy = divisibleBy;
            this.monkeyToThrowIfTrue = monkeyToThrowIfTrue;
            this.monkeyToThrowIfFalse = monkeyToThrowIfFalse;
        }

        public MonkeyAction execute(BigInteger item) {
            return new MonkeyAction(item, item.mod(divisibleBy).equals(BigInteger.ZERO) ? monkeyToThrowIfTrue : monkeyToThrowIfFalse);
        }
    }

    public sealed interface Operation permits AddOperation, MultiplyOperation {
        BigInteger execute(BigInteger item);
    }

    public sealed interface Operand permits ConstantOperand, ItemOperand {
        BigInteger resolve(BigInteger item);
    }

    public static final class ConstantOperand implements Operand {
        private final BigInteger value;

        public ConstantOperand(BigInteger value) {
            this.value = value;
        }

        @Override
        public BigInteger resolve(BigInteger item) {
            return value;
        }
    }

    public static final class ItemOperand implements Operand {
        @Override
        public BigInteger resolve(BigInteger item) {
            return item;
        }
    }

    public static final class AddOperation implements Operation {

        private final Operand operand1;
        private final Operand operand2;

        public AddOperation(Operand operand1, Operand operand2) {
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        @Override
        public BigInteger execute(BigInteger item) {
            return operand1.resolve(item).add(operand2.resolve(item));
        }
    }

    public static final class MultiplyOperation implements Operation {

        private final Operand operand1;
        private final Operand operand2;

        public MultiplyOperation(Operand operand1, Operand operand2) {
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        @Override
        public BigInteger execute(BigInteger item) {
            return operand1.resolve(item).multiply(operand2.resolve(item));
        }
    }

    public static class Monkeys {
        private final List<Monkey> monkeys;

        public Monkeys(List<Monkey> monkeys) {
            this.monkeys = monkeys;
        }

        public void round() {
            for (Monkey monkey : monkeys) {
                Collection<MonkeyAction> actions = monkey.turn();

                for (MonkeyAction action : actions) {
                    findMonkeyById(action.destinationMonkeyId()).catchItem(action.item());
                }
            }
        }

        private Monkey findMonkeyById(Integer id) {
            return monkeys.stream().filter(m -> m.getId().equals(id)).findFirst().orElseThrow();
        }

        public static Monkeys fromString(String value, BigInteger worryLevelDivisor) {
            var monkeys = Arrays.stream(value.split("\n\n")).map(s -> Monkey.fromString(s, worryLevelDivisor)).toList();
            return new Monkeys(monkeys);
        }
    }

    public static class Monkey {
        private final List<BigInteger> items = new LinkedList<>();
        private final Integer id;

        private final Operation operation;

        private final Test test;

        private final BigInteger worryLevelDivisor;

        private BigInteger itemsTested = BigInteger.ZERO;

        private List<BigInteger> primes;

        public Monkey(Integer id, Operation operation, Test test, BigInteger worryLevelDivisor, List<BigInteger> startingItems) {
            this.id = id;
            this.operation = operation;
            this.test = test;
            this.worryLevelDivisor = worryLevelDivisor;
            this.items.addAll(startingItems);
        }

        public Integer getId() {
            return id;
        }

        public void catchItem(BigInteger item) {
            items.add(item);
        }

        public Collection<MonkeyAction> turn() {
            final List<MonkeyAction> actions = new LinkedList<>();

            for (BigInteger item : items) {
                // Monkey inspects item and applies operation
                BigInteger worryLevel = operation.execute(item);

                if (worryLevelDivisor.equals(BigInteger.ONE)) {
                    final BigInteger congruenceCalc = worryLevel;
                    var a = primes.stream().map(congruenceCalc::mod).toList();

                    worryLevel = Math.ChineseRemainderTheorem(a, primes);
                } else {
                    // Before throwing, the worry level is divided by the worry level divisor
                    // Only really useful for part 1
                    worryLevel = worryLevel.divide(worryLevelDivisor);
                }

                // The monkey tests the item/worry level to know what to do next
                MonkeyAction action = test.execute(worryLevel);
                actions.add(action);
                itemsTested = itemsTested.add(BigInteger.ONE);
            }

            items.clear();
            return actions;
        }

        public BigInteger getItemsTested() {
            return itemsTested;
        }

        public static Monkey fromString(String value, BigInteger worryLevelDivisor) {
            var parts = value.split("\n");

            // Parse ID
            Integer id = Integer.parseInt(parts[0].strip().substring(7, parts[0].strip().indexOf(":")));

            // Parse starting items
            List<BigInteger> items = Arrays.stream(parts[1].substring(parts[1].indexOf(":") + 1).split(","))
                    .map(String::strip)
                    .map(BigInteger::new)
                    .toList();

            // Parse operation
            String[] operationParts = parts[2].substring(parts[2].indexOf("= ") + 2).split(" ");

            var operand1 = operationParts[0].strip().equals("old") ? new ItemOperand() : new ConstantOperand(new BigInteger(operationParts[0].strip()));
            var operand2 = operationParts[2].strip().equals("old") ? new ItemOperand() : new ConstantOperand(new BigInteger(operationParts[2].strip()));
            var operationType = operationParts[1].strip();

            Operation operation = switch (operationType) {
                case "+" -> new AddOperation(operand1, operand2);
                case "*" -> new MultiplyOperation(operand1, operand2);
                default -> throw new RuntimeException("Invalid operation type " + operationParts[0]);
            };

            // Parse test
            BigInteger divisibleBy = new BigInteger(parts[3].substring(parts[3].lastIndexOf(" ") + 1));
            Integer monkeyIfTrue = Integer.parseInt(parts[4].substring(parts[4].lastIndexOf(" ") + 1));
            Integer monkeyIfFalse = Integer.parseInt(parts[5].substring(parts[5].lastIndexOf(" ") + 1));

            Test test = new Test(divisibleBy, monkeyIfTrue, monkeyIfFalse);

            return new Monkey(id, operation, test, worryLevelDivisor, items);
        }

        public void setPrimes(List<BigInteger> primes) {
            this.primes = primes;
        }
    }

    @Override
    public String part1(Input input) {
        var monkeys = Monkeys.fromString(input.raw(), new BigInteger("3"));

        for (int i = 0; i < 20; i++) {
            monkeys.round();
        }

        BigInteger monkeyBusiness = monkeys.monkeys.stream()
                .sorted(Comparator.comparing(Monkey::getItemsTested).reversed())
                .limit(2)
                .map(Monkey::getItemsTested)
                .reduce(BigInteger::multiply)
                .orElseThrow();

        return monkeyBusiness.toString();
    }

    @Override
    public String part2(Input input) {
        var monkeys = Monkeys.fromString(input.raw(), new BigInteger("1"));

        // Change the monkeys to be able to calculate the CRT using all divisors (which are pairwise coprime)
        var primes = monkeys.monkeys.stream().map(m -> m.test).map(t -> t.divisibleBy).toList();
        monkeys.monkeys.forEach(m -> m.setPrimes(primes));

        for (int i = 0; i < 10_000; i++) {
            monkeys.round();
        }

        BigInteger monkeyBusiness = monkeys.monkeys.stream()
                .sorted(Comparator.comparing(Monkey::getItemsTested).reversed())
                .limit(2)
                .map(Monkey::getItemsTested)
                .reduce(BigInteger::multiply)
                .orElseThrow();

        return monkeyBusiness.toString();
    }
}
