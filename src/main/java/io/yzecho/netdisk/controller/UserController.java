package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.service.FileFolderService;
import io.yzecho.netdisk.service.FileStoreService;
import io.yzecho.netdisk.service.MyFileService;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.utils.LogUtil;
import io.yzecho.netdisk.utils.MailUtil;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 17:20
 */
@Controller
public class UserController extends BaseController {

    private final Logger logger = LogUtil.getInstance(UserController.class);

    public UserController(UserService userService, MyFileService myFileService, FileFolderService fileFolderService, FileStoreService fileStoreService, JavaMailSenderImpl mailSender) {
        super(userService, myFileService, fileFolderService, fileStoreService, mailSender);
    }

    @PostMapping("/register")
    public String register(User user, String code, Map<String, Object> map) {
        String uCode = (String) session.getAttribute(user.getEmail() + "_code");
        if (!code.equals(uCode)) {
            map.put("errMsg", "验证码错误");
            return "index";
        }
        user.setUserName(user.getUserName().trim());
        user.setImagePath("static/img/logo.png");
        user.setRegisterTime(LocalDateTime.now());
        user.setRole(1);
        if (userService.insert(user)) {
            FileStore store = FileStore.builder().userId(user.getUserId()).currentSize(0).build();
            fileStoreService.addFileStore(store);
            user.setFileStoreId(store.getFileStoreId());
            userService.update(user);
            logger.info("注册用户成功！当前注册用户：{}", user);
            logger.info("注册仓库成功！当前注册仓库：{}", store);
        } else {
            map.put("errMsg", "服务器发生错误，注册失败");
            return "index";
        }
        session.removeAttribute(user.getEmail() + "_code");
        session.setAttribute("loginUser", user);
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String login(User user, Map<String, Object> map) {
        User userByEmail = userService.queryUserByEmail(user.getEmail());
        if (userByEmail != null && user.getPassword().equals(userByEmail.getPassword())) {
            session.setAttribute("loginUser", userByEmail);
            logger.info("登录成功！{}", userByEmail);
            return "redirect:/index";
        } else {
            String errMsg = userByEmail == null ? "该邮箱尚未注册" : "密码错误";
            logger.info("登录失败！请确认邮箱和密码是否正确");
            map.put("errMsg", errMsg);
            return "index";
        }
    }

    /**
     * 第三方GitHub登录
     */
    @GetMapping("/loginByGitHub")
    public void login() {
        
    }

    @ResponseBody
    @RequestMapping("/sendCode")
    public String sendCode(String email) {
        User user = userService.queryUserByEmail(email);
        if (user != null) {
            logger.error("发送验证码失败，该邮箱已被注册");
            return "error";
        }
        logger.info("开始发送邮件...");
        mailUtil = new MailUtil(mailSender);
        String code = mailUtil.sendCode(email);
        session.setAttribute(email + "_code", code);
        return "success";
    }

    @GetMapping("/logout")
    public String logout() {
        logger.info("用户退出！");
        session.invalidate();
        return "redirect:/index";
    }
}
