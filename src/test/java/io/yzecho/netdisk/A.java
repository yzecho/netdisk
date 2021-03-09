package io.yzecho.netdisk;

import java.util.*;

/**
 * @className: A
 * @description:
 * @author: liuzhe
 * @date: 2021/3/3
 **/
public class A {
    public static void main(String[] args) {
        Map<String, List<Integer>> map = new HashMap<>();
        String key = "abc";
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                List<Integer> list = map.get(key);
                if (list == null) {
                    synchronized (A.class) {
                        list = map.get(key);
                        if (list == null) {
                            map.put(key, new ArrayList<>());
                        }
                    }
                } else {
                    System.out.println(list);
                }
            }).start();
        }
    }

    public static String createRandomString(int length) {
//        String str = "abcdefghijklmnopqrstuvwxyz";
        String str = "abcde";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(5);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
