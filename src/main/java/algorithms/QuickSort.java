package algorithms;

import util.Metrics;
import java.util.Random;

public class QuickSort {
    private final Metrics metrics;
    private final Random rand = new Random();

    public QuickSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int left, int right) {
        metrics.enterRecursion();
        while (left < right) {
            int pivotIndex = left + rand.nextInt(right - left + 1);
            int pivotValue = arr[pivotIndex];

            int i = left;
            int j = right;
            while (i <= j) {
                while (arr[i] < pivotValue) {
                    metrics.incComparisons();
                    i++;
                }
                while (arr[j] > pivotValue) {
                    metrics.incComparisons();
                    j--;
                }
                if (i <= j) {
                    swap(arr, i, j);
                    i++;
                    j--;
                }
            }

            if (j - left < right - i) {
                if (left < j) {
                    quickSort(arr, left, j);
                }
                left = i;
            } else {
                if (i < right) {
                    quickSort(arr, i, right);
                }
                right = j;
            }
        }
        metrics.exitRecursion();
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
