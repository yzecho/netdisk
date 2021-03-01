package io.yzecho.netdisk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yzecho
 * @desc 单例模式
 * @date 13/01/2021 19:55
 */
public class LogUtil {
    private static Logger logger;

    public static Logger getInstance(Class<?> c) {
        return logger = LoggerFactory.getLogger(c);
    }

    private LogUtil() {

    }
}
