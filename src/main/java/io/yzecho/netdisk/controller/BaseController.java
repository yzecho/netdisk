package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.provider.GitHubProvider;
import io.yzecho.netdisk.service.FileFolderService;
import io.yzecho.netdisk.service.FileStoreService;
import io.yzecho.netdisk.service.MyFileService;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yzecho
 * @desc
 * @date 17/01/2021 12:45
 */
public class BaseController {

    @Autowired
    protected UserService userService;
    @Autowired
    protected MyFileService myFileService;
    @Autowired
    protected FileFolderService fileFolderService;
    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    protected GitHubProvider gitHubProvider;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected User loginUser;

    @Autowired
    protected JavaMailSenderImpl mailSender;
    protected MailUtil mailUtil;

    /**
     * 在每个子类方法调用之前先调用
     * 设置request,response,session这三个对象
     *
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        loginUser = (User) session.getAttribute("loginUser");
    }
}
