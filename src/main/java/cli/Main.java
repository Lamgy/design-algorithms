package cli;

import algorithms.MergeSort;
import algorithms.QuickSort;
import algorithms.DeterministicSelect;
import algorithms.ClosestPair;
import algorithms.ClosestPair.Point;
import util.CsvWriter;
import util.Metrics;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java -jar app.jar <algo> <n>");
            System.out.println("Algos: mergesort, quicksort, select, closest");
            return;
        }

        String algo = args[0].toLowerCase();
        int n = Integer.parseInt(args[1]);

        CsvWriter writer = new CsvWriter("results.csv");
        writer.writeHeaderIfNeeded();

        Random rand = new Random();
        Metrics metrics = new Metrics();

        switch (algo) {
            case "mergesort": {
                int[] arr = rand.ints(n, 0, 1000).toArray();
                MergeSort sorter = new MergeSort(metrics);

                long start = System.nanoTime();
                sorter.mergeSort(arr);
                long end = System.nanoTime();

                double elapsedMs = (end - start) / 1_000_000.0;
                writer.writeRow("MergeSort", n, metrics.comparisons, metrics.maxDepth, elapsedMs);
                break;
            }

            case "quicksort": {
                int[] arr = rand.ints(n, 0, 1000).toArray();
                QuickSort sorter = new QuickSort(metrics);

                long start = System.nanoTime();
                sorter.quickSort(arr);
                long end = System.nanoTime();

                double elapsedMs = (end - start) / 1_000_000.0;
                writer.writeRow("QuickSort", n, metrics.comparisons, metrics.maxDepth, elapsedMs);
                break;
            }

            case "select": {
                int[] arr = rand.ints(n, 0, 1000).toArray();
                int k = n / 2; // median position
                DeterministicSelect selector = new DeterministicSelect(metrics);

                long start = System.nanoTime();
                int result = selector.select(arr, k);
                long end = System.nanoTime();

                double elapsedMs = (end - start) / 1_000_000.0;
                writer.writeRow("Select", n, metrics.comparisons, metrics.maxDepth, elapsedMs);

                System.out.println("Selected element (median): " + result);
                break;
            }

            case "closest": {
                Point[] points = new Point[n];
                for (int i = 0; i < n; i++) {
                    points[i] = new Point(rand.nextInt(1000), rand.nextInt(1000));
                }

                ClosestPair cp = new ClosestPair(metrics);

                long start = System.nanoTime();
                double dist = cp.findClosest(points);
                long end = System.nanoTime();

                double elapsedMs = (end - start) / 1_000_000.0;
                writer.writeRow("ClosestPair", n, metrics.comparisons, metrics.maxDepth, elapsedMs);

                System.out.println("Closest distance: " + dist);
                break;
            }

            default:
                System.out.println("Unknown algorithm: " + algo);
        }
    }
}
