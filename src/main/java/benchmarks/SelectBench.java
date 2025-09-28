package benchmarks;

import algorithms.DeterministicSelect;
import algorithms.MergeSort;
import algorithms.QuickSort;
import org.openjdk.jmh.annotations.*;

import util.Metrics;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(2)
@State(Scope.Benchmark)
public class SelectBench {
    private int[] data;
    private DeterministicSelect ds;
    private MergeSort merge;
    private QuickSort quick;

    @Setup(Level.Iteration)
    public void setup() {
        Random rand = new Random();
        data = rand.ints(10000, 0, 100000).toArray();
        ds = new DeterministicSelect(new Metrics());
        merge = new MergeSort(new Metrics());
        quick = new QuickSort(new Metrics());
    }

    @Benchmark
    public int testSelect() {
        int k = data.length / 2;
        return ds.select(Arrays.copyOf(data, data.length), k);
    }

    @Benchmark
    public int testMergeSort() {
        int[] copy = Arrays.copyOf(data, data.length);
        merge.mergeSort(copy);
        return copy[copy.length / 2];
    }

    @Benchmark
    public int testQuickSort() {
        int[] copy = Arrays.copyOf(data, data.length);
        quick.quickSort(copy);
        return copy[copy.length / 2];
    }

    @Benchmark
    public int testArraysSort() {
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}
