package tests;

import algorithms.ClosestPair;
import algorithms.ClosestPair.Point;
import org.junit.jupiter.api.Test;
import util.CsvWriter;
import util.Metrics;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosestPairTest {

    private Point[] generateRandomPoints(int n) {
        Random rand = new Random();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(rand.nextInt(10000), rand.nextInt(10000));
        }
        return pts;
    }

    private double bruteForce(Point[] pts) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = (double) (pts[i].x - pts[j].x);
                double dy = (double) (pts[i].y - pts[j].y);
                double d = Math.sqrt(dx * dx + dy * dy);
                if (d < min) min = d;
            }
        }
        return min;
    }

    private void runAndRecord(int n) {
        Point[] pts = generateRandomPoints(n);

        Metrics metrics = new Metrics();
        ClosestPair cp = new ClosestPair(metrics);

        long start = System.nanoTime();
        double result = cp.findClosest(pts);
        long end = System.nanoTime();

        if (n <= 2000) {
            double expected = bruteForce(pts);
            assertEquals(expected, result, 1e-6);
        }

        double elapsedMs = (end - start) / 1000000.0;

        CsvWriter writer = new CsvWriter("results.csv");
        writer.writeHeaderIfNeeded();
        writer.writeRow("ClosestPair", n, metrics.comparisons, metrics.maxDepth, elapsedMs);
    }

    @Test
    void testClosest10() {
        runAndRecord(10);
    }

    @Test
    void testClosest100() {
        runAndRecord(100);
    }

    @Test
    void testClosest1000() {
        runAndRecord(1000);
    }
}
