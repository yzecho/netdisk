package io.yzecho.netdisk.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yzecho
 * @desc 登录拦截器
 * @date 12/01/2021 21:50
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("/baoda-cloud/");
            return false;
        } else {
            return true;
        }
    }
}
