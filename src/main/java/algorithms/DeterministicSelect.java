package algorithms;

import util.ArrayUtils;
import util.Metrics;

public class DeterministicSelect {

    private final Metrics metrics;

    public DeterministicSelect(Metrics metrics) {
        this.metrics = metrics;
    }

    public int select(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return select(arr, 0, arr.length - 1, k);
    }

    private int select(int[] arr, int left, int right, int k) {
        while (true) {
            if (left == right) return arr[left];

            metrics.enterRecursion();

            int pivotIndex = medianOfMedians(arr, left, right);
            int pivotNewIndex = ArrayUtils.partition(arr, left, right, pivotIndex, metrics);

            if (k == pivotNewIndex) {
                metrics.exitRecursion();
                return arr[k];
            } else if (k < pivotNewIndex) {
                right = pivotNewIndex - 1;
            } else {
                left = pivotNewIndex + 1;
            }

            metrics.exitRecursion();
        }
    }

    private int medianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;

        int numMedians = (int) Math.ceil(n / 5.0);
        int[] medians = new int[numMedians];

        for (int i = 0; i < numMedians; i++) {
            int subLeft = left + i * 5;
            int subRight = Math.min(subLeft + 4, right);
            insertionSort(arr, subLeft, subRight);
            int medianIndex = (subLeft + subRight) / 2;
            medians[i] = arr[medianIndex];
        }

        if (numMedians == 1) {
            for (int i = left; i <= right; i++) {
                metrics.incComparisons();
                if (arr[i] == medians[0]) return i;
            }
        }

        DeterministicSelect subSelect = new DeterministicSelect(metrics);
        int mom = subSelect.select(medians, numMedians / 2);

        for (int i = left; i <= right; i++) {
            metrics.incComparisons();
            if (arr[i] == mom) return i;
        }
        return left;
    }

    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
                metrics.incComparisons();
                if (arr[j] <= key) break;
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}
