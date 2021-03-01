package io.yzecho.netdisk;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yzecho
 * @desc
 * @date 15/01/2021 19:36
 */
public class Main {

    // 0 1 array
    public static int getMaxLength(int[] arr) {

        if (arr == null || arr.length == 0) {
            return 0;
        }

        if (arr.length == 1 && arr[0] == 1) {
            return 1;
        }

        int m = 0;
        int n = 1;
        int max = Integer.MIN_VALUE;
        // 0 1 0 1 1 1 0 1 1 1 1
        while (m < arr.length && n < arr.length) {
            if (arr[m] == 0) {
                m++;
                n++;
                continue;
            }
            if (arr[m] == 1 && arr[n] != 1) {
                m++;
                n++;
                max = 1;
            } else if (arr[m] == 1 && arr[n] == 1) {
                n++;
                if (arr[n] == 0) {
                    max = Math.max(max, n - m + 1);
                    m = n + 1;
                }
            }
        }
        return max;
    }

    public static int[] quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return null;
        }
        int left = low;
        int right = high;
        int tmpVal = arr[left];

        while (left <= right) {
            while (left <= right && arr[left] <= tmpVal) {
                left++;
            }
            arr[right] = arr[left];
            while (left <= right && arr[right] > tmpVal) {
                right--;
            }
            arr[left] = arr[right];
        }
        arr[left] = tmpVal;
        quickSort(arr, 0, low);
        quickSort(arr, low + 1, high);
        return arr;
    }

    public static void main(String[] args) {
        System.out.println(new Date());
        System.out.println(LocalDateTime.now().toString().substring(0,19));
    }
}
