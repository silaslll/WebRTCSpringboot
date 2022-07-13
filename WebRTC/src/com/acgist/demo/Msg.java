package com.acgist.demo;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Msg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String oid = request.getParameter("oid");
		String uid = UUID.randomUUID().toString();
		
		
		request.setAttribute("initiator", "false");
		
		if(!AcgistVideo.canCreate()) {
			response.getWriter().write("不能创建通话房间，超过最大创建数量！");
			return;
		}
		
		if(!AcgistVideo.canJoin(oid)) {
			response.getWriter().write("对不起对方正在通话中，你不能加入！");
			return;
		}
		
		if(AcgistVideo.addUser(uid, oid)) {
			request.setAttribute("uid", uid);
		} else {
			request.setAttribute("initiator", "true");
			
			request.setAttribute("uid", uid);
			request.setAttribute("oid", oid);
		}
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	public void init() throws ServletException {
	}

}
