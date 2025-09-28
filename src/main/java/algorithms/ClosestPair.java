package algorithms;

import util.Metrics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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

        return closest(px, py, px.length);
    }

    private double closest(Point[] px, Point[] py, int n) {
        metrics.enterRecursion();

        if (n <= 3) {
            double d = bruteForce(px, n);
            metrics.exitRecursion();
            return d;
        }

        int mid = n / 2;
        Point midPoint = px[mid];

        Point[] pxLeft = Arrays.copyOfRange(px, 0, mid);
        Point[] pxRight = Arrays.copyOfRange(px, mid, n);

        Set<Point> leftSet = new HashSet<>(Arrays.asList(pxLeft));

        Point[] pyl = new Point[pxLeft.length];
        Point[] pyr = new Point[pxRight.length];
        int li = 0, ri = 0;
        for (int i = 0; i < py.length; i++) {
            Point p = py[i];
            if (leftSet.contains(p)) {
                pyl[li++] = p;
            } else {
                pyr[ri++] = p;
            }
        }

        double dl = closest(pxLeft, pyl, pxLeft.length);
        double dr = closest(pxRight, pyr, pxRight.length);
        double d = Math.min(dl, dr);

        Point[] strip = new Point[n];
        int stripSize = 0;
        for (int i = 0; i < py.length; i++) {
            if (Math.abs((long) py[i].x - midPoint.x) < d) {
                strip[stripSize++] = py[i];
            }
        }

        double min = d;
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < min; j++) {
                metrics.incComparisons();
                min = Math.min(min, dist(strip[i], strip[j]));
            }
        }

        metrics.exitRecursion();
        return Math.min(d, min);
    }

    private double bruteForce(Point[] pts, int n) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                metrics.incComparisons();
                min = Math.min(min, dist(pts[i], pts[j]));
            }
        }
        return min;
    }

    private double dist(Point a, Point b) {
        long dx = (long) a.x - b.x;
        long dy = (long) a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static class Point {
        public final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }
}
