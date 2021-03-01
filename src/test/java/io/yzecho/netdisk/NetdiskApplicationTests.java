package io.yzecho.netdisk;

import io.yzecho.netdisk.utils.FtpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class NetdiskApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        // 初始化连接
        FtpUtil.initFtpClient();
        boolean res = FtpUtil.existFile("/var");
        System.out.println(res);
//        File file = new File("/Users/yecho/Desktop/demo.txt");
//        FileInputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        boolean flag = FtpUtil.uploadFile("", "demo.txt", inputStream);
//        System.out.println(flag);

//        boolean res = FtpUtil.renameFile("demo.txt", "rename.txt");
//        System.out.println(res);

//        boolean res = FtpUtil.deleteFile("", "rename.txt");
//        System.out.println(res);

//        boolean res = FtpUtil.downloadFile("", "demo.txt", "/Users/yecho/Desktop/other/text.txt");
//        System.out.println(res);

//        boolean res = FtpUtil.deleteFolder("foldertest");
//        System.out.println(res);

    }
}
