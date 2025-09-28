package algorithms;
import util.Metrics;
import util.ArrayUtils;

public class MergeSort {

    private final Metrics metrics;

    public MergeSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public void mergeSort(int[] arr) {
        if (ArrayUtils.isTrivial(arr)) return;
        int[] buffer = new int[arr.length];
        mergeSort(arr, buffer, 0, arr.length - 1);
    }

    void InsertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int temp = arr[i];
            int j = i - 1;

            while (j >= left) {
                metrics.incComparisons();
                if (arr[j] > temp) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }

            arr[j + 1] = temp;
        }
    }

    void mergeSort(int[] arr, int[] buffer, int left, int right) {
        metrics.enterRecursion();
        if (left >= right) {
            metrics.exitRecursion();
            return;
        }

        if(right - left < 8){
            InsertionSort(arr, left, right);
            metrics.exitRecursion();
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(arr, buffer, left, mid);
        mergeSort(arr, buffer, mid + 1, right);

        LinearMerge(arr, buffer, left, mid, right);
        metrics.exitRecursion();
    }
    void LinearMerge(int[] arr, int[] buffer, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            metrics.incComparisons();
            if (arr[i] <= arr[j]) {
                buffer[k++] = arr[i++];
            } else {
                buffer[k++] = arr[j++];
            }
        }

        while (i <= mid) {
            buffer[k++] = arr[i++];
        }

        while (j <= right) {
            buffer[k++] = arr[j++];
        }

        if (right + 1 - left >= 0) System.arraycopy(buffer, left, arr, left, right + 1 - left);
    }
}
