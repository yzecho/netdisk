package io.yzecho.netdisk.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author yzecho
 * @desc 单例模式
 * @date 13/01/2021 19:55
 */
public class LogUtil {

    private static Logger logger = LogManager.getLogger(LogUtil.class);

    public static Logger getInstance(Class<?> c) {
        return logger = LogManager.getLogger(c);
    }

    private LogUtil() {
    }
}
