package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.service.FileFolderService;
import io.yzecho.netdisk.service.FileStoreService;
import io.yzecho.netdisk.service.MyFileService;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.utils.MailUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 12:45
 */
@Controller
public class BaseController {

    protected UserService userService;
    protected MyFileService myFileService;
    protected FileFolderService fileFolderService;
    protected FileStoreService fileStoreService;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected User loginUser;

    protected JavaMailSenderImpl mailSender;
    protected MailUtil mailUtil;

    public BaseController(UserService userService, MyFileService myFileService, FileFolderService fileFolderService, FileStoreService fileStoreService, JavaMailSenderImpl mailSender) {
        this.userService = userService;
        this.myFileService = myFileService;
        this.fileFolderService = fileFolderService;
        this.fileStoreService = fileStoreService;
        this.mailSender = mailSender;
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        loginUser = (User) session.getAttribute("loginUser");
    }
}
