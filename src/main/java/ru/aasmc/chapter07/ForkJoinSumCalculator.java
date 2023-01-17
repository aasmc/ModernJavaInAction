package ru.aasmc.chapter07;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

import static ru.aasmc.chapter07.ParallelStreamsHarness.FORK_JOIN_POOL;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    public static final long THRESHOLD = 10_000;

    private final long[] numbers;

    private final int start;

    private final int end;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        // asynchronously execute the newly created subtask using another thread of ForkJoinPool
        // it might seem natural to call fork on both the left and right subtasks, but this is
        // less efficient than directly calling compute on one of them.
        leftTask.fork();
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        // execute the task synchronously potentially allowing further recursive splits
        Long rightResult = rightTask.compute();
        // invoking the join method on a task blocks the caller until the result produced
        // by that task is ready. For this reason, it's necessary to call it after the
        // computation of both subtasks has been started.
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; ++i) {
            sum += numbers[i];
        }
        return sum;
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        // the invoke method shouldn't be used from within a RecursiveTask. Instead, you
        // should always call the methods compute or fork directly.
        // Only sequential code should use invoke to begin parallel execution.
        return FORK_JOIN_POOL.invoke(task);
    }
}
