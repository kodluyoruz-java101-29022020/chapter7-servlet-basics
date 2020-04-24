package com.servlets.basics.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
		filterName = "authenticationFilter",
		urlPatterns = { "/user/account", "/empty", "/greeting" } )
public class AuthenticationFilter implements Filter {
	
	private List<String> tokens;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		tokens = loadValidTokens();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		
		String httpMethod = httpRequest.getMethod();
		
		if("GET".contentEquals(httpMethod) && "/user/account".contentEquals(httpRequest.getServletPath())) {
			System.out.println("Authorization is ok!");
			chain.doFilter(request, response);
		}
		else {
			String apiKey = httpRequest.getHeader("x-api-key");
			
			if(apiKey == null) {
				apiKey = httpRequest.getParameter("x-api-key");
			}
			
			if(apiKey == null) {
				unAuthorizedHttpRequest(httpResponse);
				return;
			}
			else if(!tokens.contains(apiKey)) {
				unAuthorizedHttpRequest(httpResponse);
				return;
			}
			
			System.out.println("Authorization is ok!");
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void destroy() {
		tokens.clear();
	}
	
	private void unAuthorizedHttpRequest(HttpServletResponse httpResponse) throws IOException {
		
		httpResponse.setStatus(401);
		httpResponse.setContentType("text/html;charset=UTF-8");
		httpResponse.getWriter().println("<html>");
		httpResponse.getWriter().println("<head>");
		httpResponse.getWriter().println("Kaynağa erişim için geçerli bir yetkiniz yoktur. Lütfen geçerli bir x-api-key giriniz!");
		httpResponse.getWriter().println("</head>");
		httpResponse.getWriter().println("</html>");
	}
	
	private List<String> loadValidTokens() {
		
		List<String> tokens = new ArrayList<String>();
		tokens.add("t1");
		tokens.add("t2");
		tokens.add("t3");
		tokens.add("t4");
		tokens.add("t5");
		
		return tokens;
	}

}
