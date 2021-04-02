package io.yzecho.netdisk.utils;

import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 19:54
 */
public class MailUtil {

    private final JavaMailSenderImpl mailSender;

    Logger logger = LogUtil.getInstance(MailUtil.class);

    public MailUtil(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 发送简单邮件（邮件正文不支持HTML标签）
     *
     * @param title
     * @param text
     * @param accountEmail
     */
    public void sendSimpleMailMessage(String title, String text, String accountEmail) {
        logger.info("开始发送简单邮件...");
        logger.info("mailSender对象为：" + mailSender);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(text);
        message.setFrom("yzecho@foxmail.com");
        message.setTo(accountEmail);
        logger.info("message对象为：" + message);
        mailSender.send(message);
    }

    /**
     * 发送复杂邮件（邮件正文支持HTML标签）
     *
     * @param title
     * @param text
     * @param accountEmail
     */
    public void sendComplexMailMessage(String title, String text, String accountEmail) {
        logger.info("开始发送复杂邮件...");
        logger.info("mailSender对象为：" + mailSender);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject(title);
            helper.setText(text, true);
            helper.setFrom("yzecho@foxmail.com");
            helper.setTo(accountEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        logger.info("mimeMessage对象为：" + mimeMessage);
        mailSender.send(mimeMessage);
    }

    /**
     * 生成验证码
     *
     * @param email
     * @return
     */
    public String sendCode(String email) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        logger.info("开始发送复杂邮件...");
        logger.info("mailSender对象为:" + mailSender);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("宝大网盘-邮箱验证");
            helper.setText("" +
                    "<h2>宝大网盘-简洁、优雅、免费</h2>" +
                    "<h3>用户注册-邮箱验证<h3/>" +
                    "您现在正在注册宝大网盘账号<br>" +
                    "验证码： <span style='color : red'>" + code + "</span><br>" +
                    "<hr>" +
                    "<h5 style='color : red'>如果并非本人操作，请忽略本邮件</h5>", true);
            helper.setFrom("yzecho@foxmail.com");
            helper.setTo(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
        return String.valueOf(code);
    }
}
