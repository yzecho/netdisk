package io.yzecho.netdisk;

import java.util.*;

/**
 * @className: A
 * @description:
 * @author: liuzhe
 * @date: 2021/3/3
 **/
public class A {
    //    public static Map<String, List<Integer>> map = new HashMap<>();
    public static Map<String, Integer> map = new HashMap<>();
    private StringBuilder sb;


    public static void main(String[] args) {
//        String key = "abc";
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                List<Integer> list = map.get(key);
//                if (list == null) {
//                    synchronized (A.class) {
//                        list = map.get(key);
//                        if (list == null) {
//                            map.putIfAbsent(key, new ArrayList<>());
//                        }
//                    }
//                } else {
//                    System.out.println(list);
//                }
//            }).start();
//        }

        map.put("a", 1);
        // map.putIfAbsent 如果key存在则无效果，反之put
        map.putIfAbsent("a", 2);
        map.putIfAbsent("b", 2);

//        map.compute()
        System.out.println(map);
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



