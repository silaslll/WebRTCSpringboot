package com.acgist.manyrtc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebSoketRTCTest.addUser("clientC");
		request.getRequestDispatcher("/manyrtcclientC.jsp").forward(request, response);
	}
	
	public void init() throws ServletException {
	}

}
