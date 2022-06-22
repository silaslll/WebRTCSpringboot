package com.yealink.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @date 2018/3/9 13:49
 */
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //检查拦截的url
        //System.out.println(httpServletRequest.getServletPath());
        String requestUrl=httpServletRequest.getServletPath();
        //首先要检查是否是 登录或者登出请求,登录登出请求直接放行
        if(requestUrl.startsWith("/login")||requestUrl.startsWith("/Page/test")){
            return true;
        }
        //获取Session
        HttpSession session = httpServletRequest.getSession();
        String username = (String)session.getAttribute("username");
        //判断请求是否是需要管理员权限的请求，是的话校验权限
        if(requestUrl.indexOf("/admin") != -1){
            if(username !=null && username.trim().equals("admin")){
                return true;
            }else{
                httpServletResponse.sendRedirect("/");
                return false;
            }
        }
        //然后对其他请求进行普通用户检查
        if(username !=null && !username.trim().equals("")){
            return true;
        }
        httpServletResponse.sendRedirect("/");
        return false;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
