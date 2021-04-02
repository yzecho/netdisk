package io.yzecho.netdisk.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author yzecho
 * @desc
 * @date 10/01/2021 14:36
 */
public class FtpUtil {

    /**
     * yzecho mac ftp host
     *
     * private static final String HOST = "192.168.0.102";
     * /
     * /**
     * admin mac ftp host
     *private static final String HOST = "10.60.103.187";
     */

    /**
     * ziru mac ftp host
     */
    private static final String HOST = "192.168.199.205";

    private static final int PORT = 21;

    private static final String USERNAME = "admin";

    private static final String PASSWORD = "admin";

    private static final String BASE_PATH = "";

    private static FTPClient ftpClient;

    /**
     * 初始化FTP客户端
     *
     * @return
     */
    public static boolean initFtpClient() {
        ftpClient = new FTPClient();
        int reply;
        try {
            ftpClient.connect(HOST, PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.setBufferSize(10240);
            ftpClient.setDataTimeout(600000);
            ftpClient.setConnectTimeout(600000);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param filePath FTP服务器文件存放路径。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    本地要上传的文件的输入流
     * @return
     */
    public static boolean uploadFile(String filePath, String filename, InputStream input) {
        boolean result = false;
        try {
            filePath = new String(filePath.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            filename = new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }
            // 切换到上传目录
            ftpClient.enterLocalPassiveMode();
            if (!ftpClient.changeWorkingDirectory(BASE_PATH + filePath)) {
                // 如果目录不存在则创建目录
                String[] dirs = filePath.split("/");
                String tempPath = BASE_PATH;
                for (String dir : dirs) {
                    if (dir == null || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return false;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            ftpClient.enterLocalPassiveMode();
            if (!ftpClient.storeFile(filename, input)) {
                return false;
            }
            input.close();
            ftpClient.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remotePath   FTP服务器上的相对路径
     * @param filename     要下载的文件名
     * @param outputStream 本地路径输出流（宿主）
     * @return
     */
    public static boolean downloadFile(String remotePath, String filename, OutputStream outputStream) {
        boolean result = false;
        try {
            remotePath = new String(remotePath.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            filename = new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }
            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ftpFile : fs) {
                if (ftpFile.getName().equals(filename)) {
                    ftpClient.enterLocalPassiveMode();
                    // FileOutputStream outputStream = new FileOutputStream(localPath);
                    ftpClient.retrieveFile(remotePath + "/" + filename, outputStream);
                    result = true;
                    outputStream.close();
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param remotePath
     * @param filename
     * @return
     */
    public static boolean deleteFile(String remotePath, String filename) {
        try {
            remotePath = new String(remotePath.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            filename = new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }

            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ftpFile : fs) {
                if ("".equals(filename)) {
                    return false;
                }
                if (ftpFile.getName().equals(filename)) {
                    String filePath = remotePath + "/" + filename;
                    ftpClient.deleteFile(filePath);
                    return true;
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param remotePath
     * @return
     */
    public static boolean deleteFolder(String remotePath) {
        boolean result = false;
        try {
            remotePath = new String(remotePath.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }
            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            ftpClient.enterLocalPassiveMode();
            FTPFile[] fs = ftpClient.listFiles();
            if (fs.length == 0) {
                ftpClient.removeDirectory(remotePath);
                result = true;
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 修改文件名或者文件夹名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean renameFile(String oldName, String newName) {
        boolean result = false;
        try {
            oldName = new String(oldName.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            newName = new String(newName.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.rename(oldName, newName);
            result = true;
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 判断FTP服务器文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean existFile(String filePath) {
        boolean result = false;
        try {
            filePath = new String(filePath.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            if (!initFtpClient()) {
                return false;
            }
            FTPFile[] fs = ftpClient.listFiles(filePath);
            System.out.println(Arrays.stream(fs).findFirst());
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}