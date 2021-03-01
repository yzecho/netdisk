package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.service.FileFolderService;
import io.yzecho.netdisk.service.FileStoreService;
import io.yzecho.netdisk.service.MyFileService;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 16:15
 */
@Controller
public class SysController extends BaseController{

    Logger logger = LogUtil.getInstance(SysController.class);

    public SysController(UserService userService, MyFileService myFileService, FileFolderService fileFolderService, FileStoreService fileStoreService, JavaMailSenderImpl mailSender) {
        super(userService, myFileService, fileFolderService, fileStoreService, mailSender);
    }
}
