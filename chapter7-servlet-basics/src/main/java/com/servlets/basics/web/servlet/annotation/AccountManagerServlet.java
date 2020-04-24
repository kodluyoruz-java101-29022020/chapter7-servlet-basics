package com.servlets.basics.web.servlet.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlets.basics.web.servlet.annotation.model.User;

@WebServlet(name = "accountManagerServlet", urlPatterns = "/user/account", loadOnStartup = 0)
public class AccountManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 6149205809868611564L;

	private int userIdCounter;
	private List<User> users; 
	
	
	@Override
	public void init() throws ServletException {
		
		userIdCounter = 0;
		users = Collections.synchronizedList(new ArrayList<User>());
	}

	@Override
	public void destroy() {
		
		users.clear();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// User Account HTML sayfasına redirect ediyoruz
		loadUserAccountHtml(resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Yeni bir hesap kaydını listeye ekliyoruz.
		addUserFromHttpRequest(req);
		resp.sendRedirect("../user_account_success.html");
	}
	
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		User user = deleteUser(req);
		
		if(user != null) {
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("Kullanıcı silindi!");
			resp.getWriter().println("</head>");
			resp.getWriter().println("<body>");
			resp.getWriter().println("Silinen Kullanıcı ID: " + user.getId());
			resp.getWriter().println("</body>");
			resp.getWriter().println("</html>");
		}
		else {
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("Kullanıcı silme işlemi başarısız!");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		User user = updateUser(req);
		
		if(user != null) {
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("Kullanıcı güncellendi!");
			resp.getWriter().println("</head>");
			resp.getWriter().println("<body>");
			resp.getWriter().println("Güncellenen Kullanıcı ID: " + user.getId());
			resp.getWriter().println("</body>");
			resp.getWriter().println("</html>");
		}
		else {
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("Kullanıcı güncelleme işlemi başarısız!");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
		}
	}
	
	
	private synchronized void addUserFromHttpRequest(HttpServletRequest req) {
		
		// HTTP isteğinden gönderilen verileri alıyoruz.
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		User user = new User(userIdCounter, username, password, email);
		userIdCounter++;
		users.add(user);
	}
	
	private synchronized User deleteUser(HttpServletRequest req) {
		
		String username = req.getParameter("username");
		
		Iterator<User> userIterator = users.iterator();
		
		User user = null;
		boolean removed = false;
		while(userIterator.hasNext()) {
			
			user = userIterator.next();
			if(user.getUsername().equals(username)) {
				userIterator.remove();
				removed = true;
				break;
			}
		}
		
		return removed ? user : null;
	}
	
	private synchronized User updateUser(HttpServletRequest req) {
		
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		Iterator<User> userIterator = users.iterator();
		
		int foundUserIndex = -1;
		int userCounter = 0;
		while(userIterator.hasNext()) {
			
			User user = userIterator.next();
			if(user.getUsername().equals(username)) {
				foundUserIndex = userCounter;
				break;
			}
			userCounter++;
		}
		
		if(userCounter > -1) {
			User user = users.get(foundUserIndex);
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(password);
			users.set(foundUserIndex, user);
			return user;
		}
		
		return null;
	}
	
	private void loadUserAccountHtml(HttpServletResponse resp) throws IOException {
		
		resp.sendRedirect("../user_account.html");
	}
	
}
