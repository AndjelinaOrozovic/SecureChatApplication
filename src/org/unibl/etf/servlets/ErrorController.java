package org.unibl.etf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unibl.etf.beans.UserBean;

/**
 * Servlet implementation class ErrorController
 */
@WebServlet("/ErrorController")
public class ErrorController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ErrorController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("Content-Security-Policy", "default-src 'self'"); //CSP
		//response.setHeader("Clear-Site-Data", "\"cache\"");
		String action = request.getParameter("action");
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/404.jsp";
		HttpSession session = request.getSession();
		session.getAttribute("notification");
		// session.setAttribute("notification", "");
		UserBean ub = (UserBean) session.getAttribute("userBean");

		if (ub != null && ub.isLoggedIn()) {
			if (action != null && action.equals("error")) {
				response.sendRedirect("./ErrorController?action=error");
			} else {
				response.sendRedirect("./ErrorController");
			}
		} else {
			address = "/WEB-INF/pages/404.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
