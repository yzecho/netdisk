package io.yzecho.netdisk.controller;

import io.yzecho.netdisk.model.FileStore;
import io.yzecho.netdisk.model.FileStoreStatistics;
import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.model.dto.AccessTokenDTO;

import io.yzecho.netdisk.model.dto.GitHubUser;
import io.yzecho.netdisk.utils.LogUtil;
import io.yzecho.netdisk.utils.MailUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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

    @Value("${oauth.github.client-id}")
    private String clientId;

    @Value("${oauth.github.client-secret}")
    private String clientSecret;

    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    @PostMapping("/register")
    public String register(User user, String code, Map<String, Object> map) {
        String uCode = (String) session.getAttribute(user.getEmail() + "_code");
        if (!code.equals(uCode)) {
            map.put("errMsg", "验证码错误");
            return "index";
        }
        user.setUserName(user.getUserName().trim());
        // password加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        // 默认头像
        user.setImagePath("baoda-cloud/img/logo.png");
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

    /**
     * Test
     *
     * @return
     */
    @GetMapping("/admin")
    public String adminLogin() {
        User user = userService.queryUserByGithubId("38834224");
        logger.info("使用免登陆方式登录成功！" + user);
        session.setAttribute("loginUser", user);
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(User user, Map<String, Object> map) {
        User userByEmail = userService.queryUserByEmail(user.getEmail());

        if (userByEmail != null
                && DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)).equals(userByEmail.getPassword())) {
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
     * 第三方GitHub登录callback
     */
    @GetMapping("/loginByGitHub")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClientId(clientId);
        accessTokenDTO.setClientSecret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirectUri(redirectUri);
        accessTokenDTO.setState(state);

        // 获取token
        String token = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(token);

        User user = userService.queryUserByGithubId(gitHubUser.getId());
        if (user == null) {
            user = User.builder()
                    .githubId(gitHubUser.getId())
                    .userName(gitHubUser.getName())
                    .imagePath(gitHubUser.getAvatarUrl())
                    .registerTime(LocalDateTime.now())
                    .build();
            if (userService.insert(user)) {
                logger.info("注册用户成功：{}", user);
                FileStore store = FileStore.builder().userId(user.getUserId()).build();
                if (fileStoreService.addFileStore(store) == 1) {
                    user.setFileStoreId(store.getFileStoreId());
                    userService.update(user);
                    logger.info("注册仓库成功：{}", store);
                }
            } else {
                logger.error("注册用户失败");
            }
        } else {
            user.setUserName(gitHubUser.getName());
            user.setImagePath(gitHubUser.getAvatarUrl());
            userService.update(user);
        }
        logger.info("GitHub用户登录成功：{}", user);
        session.setAttribute("loginUser", user);
        return "redirect:/index";
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
