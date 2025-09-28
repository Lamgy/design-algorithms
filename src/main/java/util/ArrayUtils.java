package util;

import java.util.Random;

public class ArrayUtils {
    private static final Random RAND = new Random();

    public static void swap(int[] arr, int i, int j) {
        if (i == j) return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int partition(int[] arr, int left, int right, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, i, storeIndex);
                storeIndex++;
            }
        }

        swap(arr, storeIndex, right);
        return storeIndex;
    }

    public static void shuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RAND.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    public static boolean isTrivial(int[] arr) {
        return arr == null || arr.length <= 1;
    }
}
