package tests;

import algorithms.DeterministicSelect;
import org.junit.jupiter.api.Test;
import util.Metrics;
import util.CsvWriter;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeterministicSelectTest {

    private int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000); // numbers between 0–9999
        }
        return arr;
    }

    private void runAndRecord(int size, int k) {
        int[] arr = generateRandomArray(size);
        int[] arrCopy = arr.clone();
        java.util.Arrays.sort(arrCopy);

        Metrics metrics = new Metrics();
        DeterministicSelect select = new DeterministicSelect(metrics);

        long start = System.nanoTime();
        int result = select.select(arr, k);
        long end = System.nanoTime();

        // ✅ Check correctness against sorted array
        assertEquals(arrCopy[k], result);

        // Only reached if correct
        double elapsedMs = (end - start) / 1_000_000.0;

        CsvWriter writer = new CsvWriter("results.csv");
        writer.writeHeaderIfNeeded();
        writer.writeRow("DeterministicSelect", size, metrics.comparisons, metrics.maxDepth, elapsedMs);
    }

    @Test
    void testSelect10() {
        runAndRecord(10, 5); // pick middle element
    }

    @Test
    void testSelect100() {
        runAndRecord(100, 50);
    }

    @Test
    void testSelect1000() {
        runAndRecord(1000, 500);
    }
}
