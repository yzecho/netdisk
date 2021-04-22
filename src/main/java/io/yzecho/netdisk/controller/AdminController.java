package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.model.FileFolder;
import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.MyFile;
import io.yzecho.netdisk.model.dto.PageRequestDTO;
import io.yzecho.netdisk.model.vo.UserVO;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.utils.FtpUtil;
import io.yzecho.netdisk.utils.LogUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @className: AdminController
 * @description:
 * @author: liuzhe
 * @date: 2021/4/11
 **/
@Controller
public class AdminController extends BaseController {

    @Autowired
    private FileFtpController fileFtpController;

    @Autowired
    private UserService userService;

    private final Logger logger = LogUtil.getInstance(FileFtpController.class);

    @GetMapping("/managesUsers")
    public String managerUser(Map<String, Object> map, Integer cur) {
        if (loginUser.getRole() == 1) {
            logger.error("当前登录用户: {}无管理员权限", logger.getName());
            return "redirect:/error401Page";
        }
        Integer usersCount = userService.getUsersCount();
        cur = (cur == null || cur <= 0) ? 0 : cur - 1;
        FileStoreStatistics countStatistics = myFileService.getCountStatistics(loginUser.getFileStoreId());
        if (countStatistics == null) {
            countStatistics = FileStoreStatistics.builder()
                    .fileStore(fileStoreService.getFileStoreByUserId(loginUser.getUserId()))
                    .folderNum(0)
                    .fileNum(0)
                    .docNum(0)
                    .imageNum(0)
                    .musicNum(0)
                    .videoNum(0)
                    .otherNum(0)
                    .build();
        } else {
            countStatistics.setFolderNum(userService.getUserFolderCount(loginUser.getUserId()));
        }
        List<UserVO> users = userService.queryUserVOs(cur, 5);
        PageRequestDTO page = new PageRequestDTO(cur, 5);
        map.put("statistics", countStatistics);
        map.put("users", users);
        map.put("page", page);
        map.put("usersCount", usersCount);
        logger.info("用户管理域内容: {}", map);
        return "admin/manage-users";
    }

    @GetMapping("/updateStoreInfo")
    @ResponseBody
    public String updateStoreInfo(Integer uId, Integer pre, Integer size) {
        Integer integer = fileStoreService.updatePermission(uId, pre, size * 1024);
        if (integer == 1) {
            logger.info("修改用户" + userService.queryUserByUserId(uId).getUserName() + ":的权限和仓库大小成功");
            return "200";
        } else {
            logger.error("修改用户" + userService.queryUserByUserId(uId).getUserName() + " :的权限和仓库大小失败");
            return "500";
        }
    }

    @GetMapping("/deleteUser")
    @Transactional(rollbackFor = Exception.class, transactionManager = "defaultTransactionManager")
    public String deleteUser(Integer uId, Integer cur) {
        cur = (cur == null || cur < 0) ? 1 : cur;
        FileStore fileStore = fileStoreService.getFileStoreByUserId(uId);
        List<FileFolder> folders = fileFolderService.getRootFoldersByFileStoreId(fileStore.getFileStoreId());
        for (FileFolder f : folders) {
            fileFtpController.deleteFolderF(f);
        }
        List<MyFile> files = myFileService.getRootFilesByFileStoreId(fileStore.getFileStoreId());
        for (MyFile f : files) {
            String remotePath = f.getMyFilePath();
            String fileName = f.getMyFileName() + f.getPostfix();
            if (FtpUtil.deleteFile("/" + remotePath, fileName)) {
                fileStoreService.subSize(f.getFileStoreId(), f.getSize());
                myFileService.deleteFileByFileId(f.getMyFileId());
            }
            logger.info("删除文件成功:{}", f);
        }
        if (FtpUtil.deleteFolder("/" + uId)) {
            logger.info("清空FTP上该用户的文件成功");
        } else {
            logger.error("清空FTP上该用户的文件失败");
        }
        userService.deleteUserByUserId(uId);
        fileStoreService.deleteByFileStoreId(fileStore.getFileStoreId());
        return "redirect:/managesUsers?cur=" + cur;
    }
}
