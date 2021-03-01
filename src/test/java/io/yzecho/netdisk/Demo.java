package io.yzecho.netdisk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 21/01/2021 10:37
 */
public class Demo {
    // 1 2 2 2 3 1 3 4
    // 1 1 2 2 2 3 3 4
    public static List<Integer> demo(int[] array) {

        List<Integer> res = new ArrayList<>();
        if (array == null || array.length == 0) {
            return res;
        }
        int len = array.length;

        Arrays.sort(array);
        int m = 0;
        int n = 1;
        while (m < len && n < len) {
            if (array[m] == array[n]) {
                res.add(m);
            }
            m = n;
            n++;
        }
        return res;
    }


    // a     b
    // 1     x
    // 2     y
    // 2     z
    // 3     m

}
