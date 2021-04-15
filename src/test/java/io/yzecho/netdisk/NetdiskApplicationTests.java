package io.yzecho.netdisk;

import io.yzecho.netdisk.utils.MinioUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
class NetdiskApplicationTests {

    @Autowired
    MinioUtil minioUtil;

    @Test
    void contextLoads() {
        try {
            InputStream inputStream = minioUtil.getObject("temp-bucket", "temp-1618315818333/yzecho.jpg");
            File file = new File("/Users/admin/Desktop/yzecho.jpg");

            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                bufferedOutputStream.write(inputStream.readAllBytes());
            }
            System.out.println("下载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 初始化连接
//        FtpUtil.initFtpClient();
//        boolean res = FtpUtil.existFile("/var");
//        System.out.println(res);
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
