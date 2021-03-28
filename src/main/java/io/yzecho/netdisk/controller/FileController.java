package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.constant.FileTypeConstant;
import io.yzecho.netdisk.enums.FileEnum;
import io.yzecho.netdisk.model.FileFolder;
import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.utils.FtpUtil;
import io.yzecho.netdisk.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: FileController
 * @description:
 * @author: liuzhe
 * @date: 2021/3/12
 **/
@Controller
public class FileController extends BaseController {

    private final Logger logger = LogUtil.getInstance(FileController.class);

    /**
     * 登录之后进入主页
     *
     * @param map
     * @return
     */
    @GetMapping("/index")
    public String index(Map<String, Object> map) {
        FileStoreStatistics statistics = getOrDefaultFileStoreStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        return "u-admin/index";
    }

    private FileStoreStatistics getOrDefaultFileStoreStatistics(Integer fileStoreId) {
        FileStoreStatistics statistics = myFileService.getCountStatistics(fileStoreId);
        if (statistics != null) {
            statistics.setFileStore(fileStoreService.getFileStoreByFileStoreId(loginUser.getFileStoreId()));
        } else {
            statistics = FileStoreStatistics.builder()
                    .fileStore(fileStoreService.getFileStoreByUserId(loginUser.getUserId()))
                    .folderNum(0)
                    .fileNum(0)
                    .docNum(0)
                    .imageNum(0)
                    .musicNum(0)
                    .videoNum(0)
                    .otherNum(0)
                    .build();
        }
        return statistics;
    }

    /**
     * 展示文件仓库
     *
     * @param fId
     * @param fName
     * @param errorCode
     * @param map
     * @return
     */
    @GetMapping("/files")
    public String showFileStore(Integer fId, String fName, Integer errorCode, Map<String, Object> map) {
        // 判断是否包含错误信息
        if (errorCode != null) {
            if (errorCode == 1) {
                map.put("error", "添加失败！当前已存在同名文件夹");
            }
            if (errorCode == 2) {
                map.put("error", "重命名失败！文件夹已存在");
            }
        }
        // 包含的子文件夹
        List<FileFolder> folders;

        // 包含的文件
        List<MyFile> files;

        // 当前文件夹信息
        FileFolder nowFolder;

        // 当前文件夹的相对路径
        List<FileFolder> location = new ArrayList<>();
        if (fId == null || fId <= 0) {
            // 代表当前为根目录
            fId = 0;
            folders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
            files = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
            nowFolder = FileFolder.builder().fileFolderId(fId).build();
            location.add(nowFolder);
        } else {
            // 当前为具体目录，访问的文件夹不是当前登录用户所创建的文件夹
            FileFolder folder = fileFolderService.getFileFolderByFileFolderId(fId);
            if (folder.getFileStoreId() - loginUser.getFileStoreId() != 0) {
                return "redirect:/error401Page";
            }
            // 当前为具体目录，访问的文件夹是当前登录用户所创建的文件夹
            folders = fileFolderService.getFileFolderByParentFileFolderId(fId);
            files = myFileService.getFilesByParentFolderId(fId);
            nowFolder = fileFolderService.getFileFolderByFileFolderId(fId);
            // 遍历查询当前目录
            FileFolder temp = nowFolder;
            while (temp.getParentFolderId() != 0) {
                temp = fileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }

        Collections.reverse(location);
        // 获得统计信息
        if (myFileService.getCountStatistics(loginUser.getFileStoreId()) == null) {

        }
        FileStoreStatistics statistics = getOrDefaultFileStoreStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        map.put("folders", folders);
        map.put("files", files);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        logger.info("网盘页面域中的数据:" + map);
        return "u-admin/files";
    }

    /**
     * 文件上传入口
     *
     * @param fId
     * @param fName
     * @param map
     * @return
     */
    @GetMapping("/upload")
    public String upload(Integer fId, String fName, Map<String, Object> map) {
        // 包含的子文件夹
        List<FileFolder> folders;
        // 当前文件夹信息
        FileFolder nowFolder;
        // 当前文件夹的相对路径
        List<FileFolder> location = new ArrayList<>();
        if (fId == null || fId <= 0) {
            // 代表当前为根目录
            fId = 0;
            folders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
            nowFolder = FileFolder.builder().fileFolderId(fId).build();
            location.add(nowFolder);
        } else {
            // 当前为具体目录
            folders = fileFolderService.getFileFolderByParentFileFolderId(fId);
            nowFolder = fileFolderService.getFileFolderByFileFolderId(fId);
            // 遍历查询当前目录
            FileFolder temp = nowFolder;
            while (temp.getParentFolderId() != 0) {
                temp = fileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }

        Collections.reverse(location);
        // 获得统计信息
        FileStoreStatistics statistics = getOrDefaultFileStoreStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("folders", folders);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        logger.info("网盘页面域中的数据:" + map);
        return "u-admin/upload";
    }

    /**
     * doc类型文件展示
     *
     * @param map
     * @return
     */
    @GetMapping("/doc-files")
    public String showDocFiles(Map<String, Object> map) {
        return showFiles(map, FileTypeConstant.DOC_TYPE_FILE);
    }

    /**
     * image类型文件展示
     *
     * @param map
     * @return
     */
    @GetMapping("/image-files")
    public String showImageFiles(Map<String, Object> map) {
        return showFiles(map, FileTypeConstant.IMAGE_TYPE_FILE);
    }

    /**
     * video类型文件展示
     *
     * @param map
     * @return
     */
    @GetMapping("/video-files")
    public String showVideoFiles(Map<String, Object> map) {
        return showFiles(map, FileTypeConstant.VIDEO_TYPE_FILE);
    }


    /**
     * music类型文件展示
     *
     * @param map
     * @return
     */
    @GetMapping("/music-files")
    public String showMusicFiles(Map<String, Object> map) {
        return showFiles(map, FileTypeConstant.MUSIC_TYPE_FILE);
    }

    /**
     * 其他类型文件展示
     *
     * @param map
     * @return
     */
    @GetMapping("/other-files")
    public String showOtherFiles(Map<String, Object> map) {
        return showFiles(map, FileTypeConstant.OTHER_TYPE_FILE);
    }

    private String showFiles(Map<String, Object> map, int typeCode) {
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("permission", fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission());
        map.put("files", myFileService.getFilesByType(loginUser.getFileStoreId(), typeCode));

        return switch (typeCode) {
            case FileTypeConstant.DOC_TYPE_FILE -> "u-admin/doc-files";
            case FileTypeConstant.IMAGE_TYPE_FILE -> "u-admin/image-files";
            case FileTypeConstant.VIDEO_TYPE_FILE -> "u-admin/video-files";
            case FileTypeConstant.MUSIC_TYPE_FILE -> "u-admin/music-files";
            case FileTypeConstant.OTHER_TYPE_FILE -> "u-admin/other-files";
            default -> "error/400";
        };
    }

    /**
     * 新建文件夹
     *
     * @param fileFolder
     * @param map
     * @return
     */
    @PostMapping("/addFolder")
    public String addFolder(FileFolder fileFolder, Map<String, Object> map) {
        fileFolder.setFileFolderId(loginUser.getFileStoreId());
        fileFolder.setTime(LocalDateTime.now());

        List<FileFolder> folders;
        if (fileFolder.getParentFolderId() == 0) {
            // 向用户根目录添加目录
            folders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
        } else {
            // 向用户的其他目录添加文件夹
            folders = fileFolderService.getFileFolderByParentFileFolderId(fileFolder.getParentFolderId());
        }

        for (FileFolder folder : folders) {
            if (folder.getFileFolderName().equals(fileFolder.getFileFolderName())) {
                logger.info("新建文件夹失败，该文件夹已存在");
                return "redirect:/files?error=1&Id=" + fileFolder.getParentFolderId();
            }
        }

        fileFolderService.addFileFolder(fileFolder);
        logger.info("新建文件夹成功：" + fileFolder);
        return "redirect:/files?fId=" + fileFolder.getParentFolderId();
    }

    /**
     * 文件上传
     *
     * @param files
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile files) {
        Map<String, Object> map = new HashMap<>(16);
        if (fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission() != 0) {
            logger.error(FileEnum.NO_UPLOAD_PERMISSION.getMsg());
            map.put("code", FileEnum.NO_UPLOAD_PERMISSION.getCode());
            return map;
        }

        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        Integer folderId = Integer.valueOf(request.getHeader("id"));
        String name = Objects.requireNonNull(files.getOriginalFilename()).replaceAll(" ", "");
        // 当前上传的目录下所有的文件，判断文件是否已存在
        List<MyFile> fileList;
        if (folderId == 0) {
            fileList = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
        } else {
            fileList = myFileService.getFilesByParentFolderId(folderId);
        }
        for (MyFile file : fileList) {
            if ((file.getMyFileName() + file.getPostfix()).equals(name)) {
                logger.error(FileEnum.FILE_EXISTS.getMsg());
                map.put("code", FileEnum.FILE_EXISTS.getCode());
                return map;
            }
        }

        String path = loginUser.getUserId() + "/" + LocalDateTime.now().toString() + "/" + folderId;
        if (!checkFileName(name)) {
            logger.error(FileEnum.FILE_NAME_NO_STANDER.getMsg());
            map.put("code", FileEnum.FILE_NAME_NO_STANDER.getCode());
            return map;
        }

        int sizeInt = Math.toIntExact(files.getSize() / 1024);
        if (store.getCurrentSize() + sizeInt > store.getMaxSize()) {
            logger.error(FileEnum.NOT_ENOUGH_SPACE.getMsg());
            map.put("code", FileEnum.NOT_ENOUGH_SPACE.getCode());
            return map;
        }

        // 处理文件大小
        String size = String.valueOf(files.getSize() / 1024.0);
        int indexDot = size.lastIndexOf(".");
        // 取整
        size = size.substring(0, indexDot);
        int index = name.lastIndexOf(".");
        String tempName;
        String postfix = "";
        int type = 5;

        if (index != -1) {
            // 后缀名
            tempName = name.substring(index);
            // 文件名
            name = name.substring(0, index);
            type = getFileType(tempName.toLowerCase());
            postfix = tempName.toLowerCase();
        }
        try {
            boolean res = FtpUtil.uploadFile("/" + path, name + postfix, files.getInputStream());
            if (res) {
                logger.info(FileEnum.UPLOAD_SUCCESS.getMsg() + files.getOriginalFilename());
                myFileService.addFileByFileStoreId(
                        MyFile.builder()
                                .myFileName(name)
                                .fileStoreId(loginUser.getFileStoreId())
                                .myFilePath(path)
                                .downloadTime(0)
                                .uploadTime(LocalDateTime.now())
                                .parentFolderId(folderId)
                                .size(Integer.valueOf(size))
                                .type(type)
                                .postfix(postfix)
                                .build()
                );

                fileStoreService.addSize(store.getFileStoreId(), Integer.valueOf(size));
                map.put("code", FileEnum.UPLOAD_SUCCESS.getCode());
            } else {
                logger.error(FileEnum.UPLOAD_FAILED + files.getOriginalFilename());
                map.put("code", FileEnum.UPLOAD_FAILED.getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/downloadFile")
    public String downloadFile(@RequestParam Integer fId) {
        if (fileStoreService.getFileStoreByUserId(loginUser.getUserId()).getPermission() == 2) {
            logger.error(FileEnum.NO_DOWNLOAD_PERMISSION.getMsg());
            return "redirect:/error401Page";
        }

        MyFile myFile = myFileService.getFileByFileId(fId);
        String remotePath = myFile.getMyFilePath();
        String fileName = myFile.getMyFileName() + myFile.getPostfix();

        try {
            // 从FTP上拉
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            boolean res = FtpUtil.downloadFile("/" + remotePath, fileName, outputStream);
            if (res) {
                myFileService.updateFileByFileId(MyFile.builder()
                        .myFileId(myFile.getMyFileId())
                        .downloadTime(myFile.getDownloadTime() + 1)
                        .build());
                outputStream.flush();
                outputStream.close();
                logger.info("文件下载成功: {}", myFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 重命名文件
     *
     * @param file
     * @param map
     * @return
     */
    @PostMapping("/updateFileName")
    public String renameFile(MyFile file, Map<String, Object> map) {
        MyFile oldFile = myFileService.getFileByFileId(file.getMyFileId());
        if (oldFile != null) {
            String oldName = oldFile.getMyFileName();
            String newName = file.getMyFileName();
            if (!oldName.equals(newName)) {
                boolean renameRes = FtpUtil.renameFile(oldFile.getMyFilePath() + "/" + oldName + oldFile.getPostfix(),
                        oldFile.getMyFilePath() + "/" + newName + oldFile.getPostfix());
                if (renameRes) {
                    Integer res = myFileService.updateFileByFileId(MyFile.builder().myFileId(oldFile.getMyFileId()).myFileName(newName).build());
                    if (res == 1) {
                        logger.info("修改文件名成功。原文件名：{}，新文件名：{}", oldName, newName);
                    } else {
                        logger.info("修改文件名失败。原文件名：{}，新文件名：{}", oldName, newName);
                    }
                }
            }
        }
        return "redirect:/files?fId=" + oldFile.getParentFolderId();
    }

    /**
     * 重命名文件夹
     *
     * @param folder
     * @param map
     * @return
     */
    @PostMapping("/updateFolder")
    public String renameFolder(FileFolder folder, Map<String, Object> map) {

        // 获得文件夹的数据库信息
        FileFolder fileFolder = fileFolderService.getFileFolderByFileFolderId(folder.getFileFolderId());
        fileFolder.setFileFolderName(folder.getFileFolderName());

        // 获得当前目录下的所有文件夹,用于检查文件夹是否已经存在
        List<FileFolder> fileFolders = fileFolderService.getFileFolderByParentFileFolderId(fileFolder.getParentFolderId());
        for (FileFolder f : fileFolders) {
            if (f.getFileFolderName().equals(folder.getFileFolderName()) && !f.getFileFolderId().equals(folder.getFileFolderId())) {
                logger.info("重命名文件夹失败，文件夹已存在");
                return "redirect:/files?error=2&fId=" + fileFolder.getParentFolderId();
            }
        }

        // 向数据库写入数据
        fileFolderService.updateFileFolderById(fileFolder);
        logger.info("重命名文件夹成功：{}", folder);
        return "redirect:/files?fId=" + fileFolder.getParentFolderId();
    }

    /**
     * 删除文件
     *
     * @param fId
     * @param folderId
     * @return
     */
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam(value = "fId") Integer fId, Integer folderId) {
        MyFile file = myFileService.getFileByFileId(fId);
        String remotePath = file.getMyFilePath();
        String fileName = file.getMyFileName() + file.getPostfix();
        boolean res = FtpUtil.deleteFile("/" + remotePath, fileName);
        if (res) {
            fileStoreService.subSize(file.getFileStoreId(), file.getSize());
            myFileService.deleteFileByFileId(fId);
        }
        logger.info("删除文件成功：{}", file);
        return "redirect:/files?fId=" + folderId;
    }

    /**
     * 删除文件夹（包括里面所有子文件及子文件夹）
     *
     * @return
     */
    @GetMapping("/deleteFolder")
    public String deleteFolder(@RequestParam(value = "fId") Integer fId) {
        FileFolder folder = fileFolderService.getFileFolderByFileFolderId(fId);

        deleteFolderF(folder);
        return folder.getParentFolderId() == 0 ? "redirect:/files" : "redirect:/files?fId=" + folder.getParentFolderId();
    }

    /**
     * 递归删除
     *
     * @param folder
     */
    private void deleteFolderF(FileFolder folder) {
        // 该文件夹为根目录的所有子文件夹
        List<FileFolder> folders = fileFolderService.getFileFolderByParentFileFolderId(folder.getParentFolderId());

        // 该文件夹下的所有子文件
        List<MyFile> files = myFileService.getFilesByParentFolderId(folder.getParentFolderId());
        if (files.size() != 0) {
            files.forEach(file -> {
                boolean deleteFileRes = FtpUtil.deleteFile("/" + file.getMyFilePath(), file.getMyFileName() + file.getPostfix());
                if (deleteFileRes) {
                    myFileService.deleteFileByFileId(file.getMyFileId());
                    fileStoreService.subSize(file.getFileStoreId(), file.getSize());
                }
            });
        }
        if (folders.size() != 0) {
            folders.forEach(this::deleteFolderF);
        }
        fileFolderService.deleteFileFolderByFileFolderId(folder.getFileFolderId());
    }

    /**
     * 根据文件后缀名获得文件类型
     *
     * @param type
     * @return
     */
    public int getFileType(String type) {
        if (".chm".equals(type) || ".txt".equals(type) || ".xmind".equals(type) || ".xlsx".equals(type) || ".md".equals(type)
                || ".doc".equals(type) || ".docx".equals(type) || ".pptx".equals(type)
                || ".wps".equals(type) || ".word".equals(type) || ".html".equals(type) || ".pdf".equals(type)) {
            return FileTypeConstant.DOC_TYPE_FILE;
        } else if (".bmp".equals(type) || ".gif".equals(type) || ".jpg".equals(type) || ".ico".equals(type) || ".vsd".equals(type)
                || ".pic".equals(type) || ".png".equals(type) || ".jepg".equals(type) || ".jpeg".equals(type) || ".webp".equals(type)
                || ".svg".equals(type)) {
            return FileTypeConstant.IMAGE_TYPE_FILE;
        } else if (".avi".equals(type) || ".mov".equals(type) || ".qt".equals(type)
                || ".asf".equals(type) || ".rm".equals(type) || ".navi".equals(type) || ".wav".equals(type)
                || ".mp4".equals(type) || ".mkv".equals(type) || ".webm".equals(type)) {
            return FileTypeConstant.VIDEO_TYPE_FILE;
        } else if (".mp3".equals(type) || ".wma".equals(type)) {
            return FileTypeConstant.MUSIC_TYPE_FILE;
        } else {
            return FileTypeConstant.OTHER_TYPE_FILE;
        }
    }

    /**
     * 验证文件名是否合法[汉字，字符，数字，下划线，英文句号，横线]
     *
     * @param target
     * @return
     */
    public boolean checkFileName(String target) {
        String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(target);
        return !matcher.find();
    }

    /**
     * 帮助页面
     *
     * @param map
     * @return
     */
    @GetMapping("/help")
    public String showHelp(Map<String, Object> map) {
        FileStoreStatistics statistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        return "u-admin/help";
    }

}
