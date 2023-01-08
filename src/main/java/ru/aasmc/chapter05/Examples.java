package ru.aasmc.chapter05;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Examples {
    public static void main(String[] args) throws IOException {
        List<Integer> first = Arrays.asList(1, 2, 3);
        List<Integer> second = Arrays.asList(3, 4);
        List<int[]> pairs = first.stream()
                .flatMap(i -> second.stream()
                        .filter(j -> (j + i) % 3 == 0)
                        .map(j -> new int[]{i, j}))
                .collect(Collectors.toList());
        pairs.forEach(p -> System.out.println(Arrays.toString(p)));

        int sum = first.stream().reduce(0, (a, b) -> a + b);
        int sum2 = first.stream().reduce(0, Integer::sum);
        System.out.println(sum);
        System.out.println(sum2);

        int product = first.stream().reduce(1, (a, b) -> a * b);
        System.out.println(product);

        Optional<Integer> max = first.stream().reduce(Integer::max);
        max.ifPresent(System.out::println);

        List<Transaction> transactions = getTransactions();

        // Find all transactions in the year 2011 and sort them by value (small to high).
        var tr2011 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(tr2011);

        // What are all the unique cities where the traders work?
        var uniqueCities = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(uniqueCities);

        // Find all traders from Cambridge and sort them by name.
        var fromCambridge = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());
        System.out.println(fromCambridge);

        // Return a string of all traders’ names sorted alphabetically.
        var names = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(names);

        // Are any traders based in Milan?
        var fromMilan = transactions.stream()
                .anyMatch(t -> t.getTrader().getCity().equals("Milan"));
        System.out.println(fromMilan);

        // Print the values of all transactions from the traders living in Cambridge.
        transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .forEach(t -> System.out.println(t.getValue()));

        // What’s the highest value of all the transactions?
        System.out.println("Highest value: ");
        var highest = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        highest.ifPresent(System.out::println);

        // Find the transaction with the smallest value.
        System.out.println("Min value: ");
        var lowest = transactions.stream()
                .map(Transaction::getValue)
                .min(Comparator.naturalOrder());
        lowest.ifPresent(System.out::println);


        // Numeric ranges
        IntStream intStream = IntStream.rangeClosed(1, 100);
        IntStream even = intStream.filter(n -> n % 2 == 0);
        System.out.println(even.count());
        // a^2 + b^2 = c^2
        var pythagoreanTriplesStream = IntStream.rangeClosed(1, 100)
                .boxed().flatMap(a ->
                        IntStream.rangeClosed(a, 100)
                                // check the number is a whole number
                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                .mapToObj(b ->
                                        new PythagoreanTriple(a, b, (int) Math.sqrt(a * a + b * b))
                                )
                );
        pythagoreanTriplesStream.limit(5)
                .forEach(t ->
                        System.out.println(t.a + ", " + t.b + ", " + t.c));


        long uniqueWords = 0;
        var file = Examples.class.getResource("/data.txt").getFile();
        try (Stream<String> lines = Files.lines(Paths.get(file), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        }
        System.out.println(uniqueWords);

        // iterate
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

        // fibonacci
        Stream.iterate(new int[]{0, 1},
                        t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));

        // iterate with predicate
        IntStream.iterate(0, n -> n < 100, n -> n + 4)
                .forEach(System.out::println);

        // Generate
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        // fibonacci generate
        LongSupplier fib = new LongSupplier() {
            private long prev = 0;
            private long curr = 1;
            @Override
            public long getAsLong() {
                long oldPrev = this.prev;
                long next = this.prev + this.curr;
                this.prev = this.curr;
                this.curr = next;
                return oldPrev;
            }
        };

        LongStream.generate(fib).limit(50).forEach(System.out::println);
    }

    private static List<Transaction> getTransactions() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        return Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }

    static class PythagoreanTriple {
        final int a;
        final int b;
        final int c;

        public PythagoreanTriple(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
