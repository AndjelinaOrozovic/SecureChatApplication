package org.unibl.etf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unibl.etf.beans.UserBean;
import org.unibl.etf.chatApp.GFG;

/**
 * Servlet implementation class TokenController
 */
@WebServlet("/TokenController")
public class TokenController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TokenController() {
		super();
	}
	
	public static String getCookieValue(HttpServletRequest request, String name) { // Cookie metoda
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/tokenCheck.jsp";
		response.addHeader("Content-Security-Policy", "default-src 'self'"); //CSP
		HttpSession session = request.getSession();
		UserBean ub = (UserBean) session.getAttribute("userBean");
		if (ub != null && ub.isLoggedIn()) {
			address = "/WEB-INF/pages/tokenCheck.jsp";
		} else {
			address = "/WEB-INF/pages/404.jsp";
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		session.setAttribute("notification", "");

		if (action != null && action.equals("token")) {
			if (GFG.onlyDigits(request.getParameter("token"))) {
				if (UserBean.token == (Integer.parseInt(request.getParameter("token")))) {
					Cookie loginCookie = new Cookie("JSESSIONID", getCookieValue(request, "JSESSIONID"));
					loginCookie.setHttpOnly(true);
					loginCookie.setSecure(true);
					loginCookie.setMaxAge(15 * 60);
					response.addCookie(loginCookie);
					response.sendRedirect("./MessageController?action=contacts");
				} else {
					session.setAttribute("notification", "Pogresni parametri za pristup!");
					response.sendRedirect("./TokenController?action=token");
				}
			} else {
				session.setAttribute("notification", "Unesite token!");
				response.sendRedirect("./TokenController?action=token");
			}
		} else {
			session.setAttribute("notification", "Dogodila se greska!");
			response.sendRedirect("./ErrorController");
		}
	}

}
