package algorithms;

import util.Metrics;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {

    private final Metrics metrics;

    public ClosestPair(Metrics metrics) {
        this.metrics = metrics;
    }

    public double findClosest(Point[] points) {
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;

        Point[] px = points.clone();
        Arrays.sort(px, Comparator.comparingInt(p -> p.x));

        Point[] py = points.clone();
        Arrays.sort(py, Comparator.comparingInt(p -> p.y));

        double bestSq = closest(px, 0, px.length - 1, py);
        return Math.sqrt(bestSq);
    }

    private double closest(Point[] px, int left, int right, Point[] py) {
        metrics.enterRecursion();

        int n = right - left + 1;
        if (n <= 3) {
            double d2 = bruteForceSquared(px, left, right);
            metrics.exitRecursion();
            return d2;
        }

        int mid = (left + right) >>> 1;
        Point midPoint = px[mid];
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        Point[] pyl = new Point[leftSize];
        Point[] pyr = new Point[rightSize];
        int li = 0, ri = 0;
        for (Point p : py) {
            if (p.x < midPoint.x) {
                if (li < leftSize) pyl[li++] = p;
                else pyr[ri++] = p;
            } else if (p.x > midPoint.x) {
                if (ri < rightSize) pyr[ri++] = p;
                else pyl[li++] = p;
            } else {
                if (li < leftSize) pyl[li++] = p;
                else pyr[ri++] = p;
            }
        }

        double dl2 = closest(px, left, mid, pyl);
        double dr2 = closest(px, mid + 1, right, pyr);
        double d2 = Math.min(dl2, dr2);

        int stripCount = 0;
        Point[] strip = new Point[py.length];
        for (Point p : py) {
            long dx = (long) p.x - midPoint.x;
            if ((double) dx * dx < d2) {
                strip[stripCount++] = p;
            }
        }

        double best = d2;
        for (int i = 0; i < stripCount; i++) {
            Point a = strip[i];
            for (int j = i + 1; j < stripCount; j++) {
                Point b = strip[j];
                long dy = (long) b.y - a.y;
                double dy2 = (double) dy * dy;
                if (dy2 >= best) break;
                metrics.incComparisons();
                double dist2 = distSquared(a, b);
                if (dist2 < best) best = dist2;
            }
        }

        metrics.exitRecursion();
        return Math.min(d2, best);
    }

    private double bruteForceSquared(Point[] px, int left, int right) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                metrics.incComparisons();
                double d2 = distSquared(px[i], px[j]);
                if (d2 < best) best = d2;
            }
        }
        return best;
    }

    private double distSquared(Point a, Point b) {
        long dx = (long) a.x - b.x;
        long dy = (long) a.y - b.y;
        return (double) dx * dx + (double) dy * dy;
    }

    public static class Point {
        public final int x, y;
        public Point(int x, int y) { this.x = x; this.y = y; }
    }
}
