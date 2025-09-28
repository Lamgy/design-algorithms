package algorithms;

import util.ArrayUtils;
import util.Metrics;

import java.util.Random;

public class QuickSort {

    private final Metrics metrics;
    private final Random rand = new Random();

    public QuickSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public void quickSort(int[] arr) {
        if (ArrayUtils.isTrivial(arr)) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int left, int right) {
        metrics.enterRecursion();
        while (left < right) {
            int pivotIndex = left + rand.nextInt(right - left + 1);
            int pivotNewIndex = ArrayUtils.partition(arr, left, right, pivotIndex, metrics);

            if (pivotNewIndex - left < right - pivotNewIndex) {
                quickSort(arr, left, pivotNewIndex - 1);
                left = pivotNewIndex + 1;
            } else {
                quickSort(arr, pivotNewIndex + 1, right);
                right = pivotNewIndex - 1;
            }
        }
        metrics.exitRecursion();
    }


}