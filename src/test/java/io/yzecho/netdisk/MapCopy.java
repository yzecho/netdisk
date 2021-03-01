package io.yzecho.netdisk;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 20/01/2021 23:16
 */
public class MapCopy {

    static class Node {
        int val;
    }

    public static void main(String[] args) {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map2.putAll(map1);
        map1.put("a",1);
        System.out.println(map1);
        System.out.println(map2);
    }
}
