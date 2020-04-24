package com.servlets.basics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GreetingServlet extends HttpServlet {

	private static final long serialVersionUID = 9143771339421764723L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String contentType = req.getParameter("content-type");
		
		if("html".equals(contentType)) {
			writeHtmlContentToServletResponse(req, resp);
		}
		else if("text".equals(contentType)) {
			writePlainTextToServletResponse(resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("POST!");
	}
	
	private void writePlainTextToServletResponse(HttpServletResponse resp) {
		
		try {
			resp.setContentType("text/plain;charset=UTF-8");
			resp.getWriter().write("Hello, Java Servlet! (HTTP GET)");
		} 
		catch (IOException e) {	
			e.printStackTrace();
		}
		finally {
			try {
				resp.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeHtmlContentToServletResponse(HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter responseWriter = resp.getWriter();
			responseWriter.println("<html>");
			responseWriter.println("<head>");
			responseWriter.println("Hello Java Servlet!");
			responseWriter.println("</head>");
			responseWriter.println("<body>");
			responseWriter.println("<h1>Java Servlet active at " + req.getContextPath() + "</h1>");
			responseWriter.println("<p> Servlet runned at " + new Date().toString() + "</p>");
			responseWriter.println("</body>");
			responseWriter.println("</html>");
		} 
		catch (IOException e) {	
			e.printStackTrace();
		}
		finally {
			try {
				resp.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
