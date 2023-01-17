package ru.aasmc.chapter07;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCount {
    public static final String SENTENCE =
            " Nel   mezzo del cammin  di nostra  vita "
                    + "mi  ritrovai in una  selva oscura"
                    + " che la  dritta via era   smarrita ";

    public static void main(String[] args) {
        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");
        System.out.println("Found " + countWords(SENTENCE) + " words");
    }

    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) {
                    ++counter;
                }
                lastSpace = Character.isWhitespace(c);
            }
        }
        return counter;
    }

    public static int countWords(String s) {
        Spliterator<Character> spliterator = new WordCounterSpliterator(s);
        Stream<Character> stream = StreamSupport.stream(spliterator, true);
        return countWords(stream);
    }

    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(
                new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine
        );
        return wordCounter.getCounter();
    }

    private static class WordCounter {
        private final int counter;
        private final boolean lastSpace;

        public WordCounter(int counter, boolean lastSpace) {
            this.counter = counter;
            this.lastSpace = lastSpace;
        }

        public int getCounter() {
            return counter;
        }

        public WordCounter accumulate(Character c) {
            if (Character.isWhitespace(c)) {
                return lastSpace ? this : new WordCounter(counter, true);
            } else {
                return lastSpace ? new WordCounter(counter + 1, false) : this;
            }
        }

        public WordCounter combine(WordCounter wordCounter) {
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }
    }

    private static class WordCounterSpliterator implements Spliterator<Character> {
        private final String string;
        private int currentChar = 0;

        public WordCounterSpliterator(String string) {
            this.string = string;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {
            // consumes current character
            action.accept(string.charAt(currentChar++));
            // returns true if there are further characters to be consumed
            return currentChar < string.length();
        }

        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = string.length() - currentChar;
            if (currentSize < 10) {
                // signals that the String is small enough to be processed sequentially
                return null;
            }
            // sets the candidate split position to be half of the String to be parsed
            for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
                // advances the split position until the next space and
                if (Character.isWhitespace(string.charAt(splitPos))) {
                    // creates a new WordCounterSpliterator parsing the String from the start to te
                    // split position
                    Spliterator<Character> spliterator =
                            new WordCounterSpliterator(string.substring(currentChar, splitPos));
                    // sets the start position of
                    // the current WordsCounterSpliterator to the split position
                    currentChar = splitPos;
                    // found a space and created the new Spliterator, so exit the loop
                    return spliterator;
                }
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return string.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }
}


























