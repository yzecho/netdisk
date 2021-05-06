package io.yzecho.netdisk.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author yzecho
 * @desc
 * @date 10/01/2021 14:36
 */
@Component
public class FtpUtil {

    private static String host;

    private static Integer port;

    private static String username;

    private static String password;

    private static Integer bufferSize;

    private static Integer dataTimeOut;

    private static Integer connectTimeout;

    @Value("${ftp.host}")
    public void setHost(String host) {
        FtpUtil.host = host;
    }

    @Value("${ftp.port}")
    public void setPort(Integer port) {
        FtpUtil.port = port;
    }

    @Value("${ftp.username}")
    public void setUsername(String username) {
        FtpUtil.username = username;
    }

    @Value("${ftp.password}")
    public void setPassword(String password) {
        FtpUtil.password = password;
    }

    @Value("${ftp.bufferSize}")
    public void setBufferSize(Integer bufferSize) {
        FtpUtil.bufferSize = bufferSize;
    }

    @Value("${ftp.dataTimeOut}")
    public void setDataTimeOut(Integer dataTimeOut) {
        FtpUtil.dataTimeOut = dataTimeOut;
    }

    @Value("${ftp.connectTimeout}")
    public void setConnectTimeout(Integer connectTimeout) {
        FtpUtil.connectTimeout = connectTimeout;
    }

    private static final String BASE_PATH = "";

    private static FTPClient ftpClient;

    /**
     * 初始化FTP客户端
     *
     * @return
     */
    public static boolean initClient() {
        ftpClient = new FTPClient();
        int reply;
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setBufferSize(bufferSize);
            ftpClient.setDataTimeout(dataTimeOut);
            ftpClient.setConnectTimeout(connectTimeout);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
            if (initClient()) {
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
            if (initClient()) {
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
            if (initClient()) {
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
            if (initClient()) {
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
            if (initClient()) {
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
            if (initClient()) {
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