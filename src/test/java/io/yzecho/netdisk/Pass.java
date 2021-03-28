package io.yzecho.netdisk;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @className: Pass
 * @description:
 * @author: liuzhe
 * @date: 2021/3/12
 **/
public class Pass {
    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("liyuqi".getBytes(StandardCharsets.UTF_8));
        System.out.println(s);
    }
}
