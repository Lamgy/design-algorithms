package tests;

import algorithms.QuickSort;
import org.junit.jupiter.api.Test;
import util.Metrics;
import util.CsvWriter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class QuickSortTest {

    private int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000); // numbers between 0–999
        }
        return arr;
    }

    private void runAndRecord(int size) {
        int[] arr = generateRandomArray(size);
        int[] expected = arr.clone();
        java.util.Arrays.sort(expected);

        Metrics metrics = new Metrics();
        QuickSort sorter = new QuickSort(metrics);

        long start = System.nanoTime();
        sorter.quickSort(arr);
        long end = System.nanoTime();

        assertArrayEquals(expected, arr);

        double elapsedMs = (end - start) / 1000000.0; // convert to ms

        CsvWriter writer = new CsvWriter("results.csv");
        writer.writeHeaderIfNeeded();
        writer.writeRow(size, metrics.comparisons, metrics.maxDepth, elapsedMs);
    }

    @Test
    void testSorting10() {
        runAndRecord(10);
    }

    @Test
    void testSorting100() {
        runAndRecord(100);
    }

    @Test
    void testSorting1000() {
        runAndRecord(1000);
    }

}
