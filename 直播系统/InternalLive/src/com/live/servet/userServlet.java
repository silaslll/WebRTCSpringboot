package com.live.servet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.live.biz.Userbiz;
import com.live.bizimps.Userbizimps;
import com.live.entit.User;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

public class userServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String contextPath = request.getContextPath();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		// 获取biz
		Userbiz ubiz = new Userbizimps();
		//查询所有用户
		List<User> userlist = new ArrayList<User>();
		userlist = ubiz.userMap();
		// 获取传的参应该进入那个模块
		String action = request.getParameter("action");
		// 获取session中的验证码
		HttpSession session = request.getSession();
		String code = (String) session.getAttribute("code");
		// 判断登录用户是否存在(如果存在进入直播间,如果不存在或者重输入,或者进行注册)
		if ("login".equals(action)) {
			// 获取用户名
			String userCode = request.getParameter("userCode");
			// 获取密码
			String userPassword = request.getParameter("userPassword");
			// 获取验证码
			String captcha = request.getParameter("yanzhengma");

			User user = new User();

			user.setName(userCode);
			user.setPassword(userPassword);

			user = ubiz.userlist(userCode, userPassword);
			if (user == null) {
				out.print("<script type=\"text/javascript\">");
				out.print("alert(\"用户名密码错误,请重新登录\");");
				out.print("open(\"" + contextPath + "/loger.jsp\",\"_self\");");
				out.print("</script>");
			} else if (!captcha.equalsIgnoreCase(code)) {
				out.print("<script type=\"text/javascript\">");
				out.print("alert(\"验证码输入错误!\");");
				out.print("open(\"" + contextPath + "/loger.jsp\",\"_self\");");
				out.print("</script>");
			} else if (user != null && captcha.equalsIgnoreCase(code)) {
				// 设置session会话失效
				request.getSession().setAttribute("user", user);
				session.setAttribute("sus", user);
				session.setMaxInactiveInterval(10 * 60);
				if ("admin".equals(userCode) && "admin".equals(userPassword)) {
					request.getRequestDispatcher("display.jsp").forward(
							request, response);
				} else {
					request.setAttribute("yonghuming", user.getName());
					request.getRequestDispatcher("watch.jsp").forward(request,
							response);
				}
			}
			// 判断注册
		} else if ("regist".equals(action)) {
			// 获取用户名
			String username = request.getParameter("username");
			// 获取密码
			String password = request.getParameter("password");
			// 获取邮箱
			String email = request.getParameter("email");
			User user = new User();
			user.setName(username);
			user.setPassword(password);
			int a = ubiz.insertuser(user);
			if (a > 0) {
				out.print("<script type=\"text/javascript\" > ");

				out.print("alert(\"注册成功,立即登录\");");

				out.print("window.location=\"loger.jsp\"");
				out.print("</script>");

			} else {

				out.print("<script type=\"text/javascript\" > ");

				out.print("alert(\"注册失败\");");

				out.print("window.location=\"registered.jsp\"");
				out.print("</script>");

			}
			//判断用户名是否已被注册
		}else if ("unas".equals(action)) {
			String username = request.getParameter("username");
			boolean fal = false;
			for (User user : userlist) {
				if (user.getName().equals(username)) {
					fal = true;
				}
			}
			out.print(fal);
		}
	}

}
