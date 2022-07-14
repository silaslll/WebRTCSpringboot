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
		// ��ȡbiz
		Userbiz ubiz = new Userbizimps();
		//��ѯ�����û�
		List<User> userlist = new ArrayList<User>();
		userlist = ubiz.userMap();
		// ��ȡ���Ĳ�Ӧ�ý����Ǹ�ģ��
		String action = request.getParameter("action");
		// ��ȡsession�е���֤��
		HttpSession session = request.getSession();
		String code = (String) session.getAttribute("code");
		// �жϵ�¼�û��Ƿ����(������ڽ���ֱ����,��������ڻ���������,���߽���ע��)
		if ("login".equals(action)) {
			// ��ȡ�û���
			String userCode = request.getParameter("userCode");
			// ��ȡ����
			String userPassword = request.getParameter("userPassword");
			// ��ȡ��֤��
			String captcha = request.getParameter("yanzhengma");

			User user = new User();

			user.setName(userCode);
			user.setPassword(userPassword);

			user = ubiz.userlist(userCode, userPassword);
			if (user == null) {
				out.print("<script type=\"text/javascript\">");
				out.print("alert(\"�û����������,�����µ�¼\");");
				out.print("open(\"" + contextPath + "/loger.jsp\",\"_self\");");
				out.print("</script>");
			} else if (!captcha.equalsIgnoreCase(code)) {
				out.print("<script type=\"text/javascript\">");
				out.print("alert(\"��֤���������!\");");
				out.print("open(\"" + contextPath + "/loger.jsp\",\"_self\");");
				out.print("</script>");
			} else if (user != null && captcha.equalsIgnoreCase(code)) {
				// ����session�ỰʧЧ
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
			// �ж�ע��
		} else if ("regist".equals(action)) {
			// ��ȡ�û���
			String username = request.getParameter("username");
			// ��ȡ����
			String password = request.getParameter("password");
			// ��ȡ����
			String email = request.getParameter("email");
			User user = new User();
			user.setName(username);
			user.setPassword(password);
			int a = ubiz.insertuser(user);
			if (a > 0) {
				out.print("<script type=\"text/javascript\" > ");

				out.print("alert(\"ע��ɹ�,������¼\");");

				out.print("window.location=\"loger.jsp\"");
				out.print("</script>");

			} else {

				out.print("<script type=\"text/javascript\" > ");

				out.print("alert(\"ע��ʧ��\");");

				out.print("window.location=\"registered.jsp\"");
				out.print("</script>");

			}
			//�ж��û����Ƿ��ѱ�ע��
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
