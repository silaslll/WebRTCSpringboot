package com.acgist.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Msg1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		
		/*Object a  = request.getParameter("oid");
		if(a == null) {
			request.setAttribute("initiator", "flase");//：initiator：如果是创建人这个参数设为false；如果是加入的时候这个设置为true。
		} else {
			request.setAttribute("initiator", "false");//：initiator：如果是创建人这个参数设为false；如果是加入的时候这个设置为true。
		}*/
		//String initiator = request.getParameter("initiator");
		String oid = request.getParameter("oid");
		String uid = "";
		if(oid == null) {
			uid = "meeting";
		} else {
			uid = session.getId();
		}
	
		request.setAttribute("initiator", "false");//：initiator：如果是创建人这个参数设为false；如果是加入的时候这个设置为true。

		System.out.println(oid + "====================oid==============");
		System.out.println(uid + "====================uid==============");
		System.out.println(request.getAttribute("initiator") + "====================initiator==============");

		
		
		if(!AcgistVideo.canCreate()) {
			response.getWriter().write("不能创建通话房间，超过最大创建数量！");
			return;
		}
		
		if(!AcgistVideo.canJoin(oid)) {
			response.getWriter().write("对不起对方正在通话中，你不能加入！");
			return;
		}
		//会议创建者，即oid=null，返回true
		if(AcgistVideo.addUser(uid, oid)) {
			request.setAttribute("uid", uid);
		} else {
			System.out.println("================添加用户成功");
			request.setAttribute("initiator", "true");
			
			request.setAttribute("uid", uid);
			request.setAttribute("oid", oid);
		}
		
		request.getRequestDispatcher("/server.jsp").forward(request, response);
	}
	
	public void init() throws ServletException {
	}

}
