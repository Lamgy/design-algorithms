package algorithms;

import util.Metrics;

import java.util.*;

public class ClosestPair {

    private final Metrics metrics;

    public ClosestPair(Metrics metrics) {
        this.metrics = metrics;
    }

    public double findClosest(Point[] points) {
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;
        if (points.length == 2) return dist(points[0], points[1]);

        // Sort points by x and y
        Point[] px = points.clone();
        Arrays.sort(px, Comparator.comparingInt(p -> p.x));

        Point[] py = points.clone();
        Arrays.sort(py, Comparator.comparingInt(p -> p.y));

        return closest(px, py, px.length);
    }

    private double closest(Point[] px, Point[] py, int n) {
        metrics.enterRecursion();

        // Handle small subproblems directly
        if (n <= 3) {
            double d = bruteForce(px, n);
            metrics.exitRecursion();
            return d;
        }

        int mid = n / 2;
        Point midPoint = px[mid];

        // Divide px into left and right
        Point[] pxLeft = Arrays.copyOfRange(px, 0, mid);
        Point[] pxRight = Arrays.copyOfRange(px, mid, n);

        // Divide py into left and right without HashSet (duplicate-safe, faster)
        List<Point> pylList = new ArrayList<>();
        List<Point> pyrList = new ArrayList<>();
        for (Point p : py) {
            if (p.x <= midPoint.x) {
                pylList.add(p);
            } else {
                pyrList.add(p);
            }
        }

        Point[] pyl = pylList.toArray(new Point[0]);
        Point[] pyr = pyrList.toArray(new Point[0]);

        // Recursive calls
        double dl = closest(pxLeft, pyl, pxLeft.length);
        double dr = closest(pxRight, pyr, pxRight.length);

        double d = Math.min(dl, dr);

        // Build strip of points close to mid line
        List<Point> strip = new ArrayList<>();
        for (Point p : py) {
            if (Math.abs((long) p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }

        // Check strip pairs
        double min = d;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < min; j++) {
                metrics.incComparisons();
                min = Math.min(min, dist(strip.get(i), strip.get(j)));
                if (min == 0) { // early exit on duplicates
                    metrics.exitRecursion();
                    return 0.0;
                }
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
                if (min == 0) return 0.0; // duplicates shortcut
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
