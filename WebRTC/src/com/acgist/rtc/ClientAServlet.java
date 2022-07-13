package com.acgist.rtc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebSoketRTCTest.addUser("clientA");
		request.getRequestDispatcher("/rtcclientA.jsp").forward(request, response);
	}
	
	public void init() throws ServletException {
	}

}
