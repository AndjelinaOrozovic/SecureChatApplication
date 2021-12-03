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
import org.unibl.etf.chatApp.GFG;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LoginController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.setAttribute("notification", "");
		String address = "/WEB-INF/pages/login.jsp";
		response.addHeader("Content-Security-Policy", "default-src 'self'"); //CSP
		UserBean userBean = (UserBean) session.getAttribute("userBean");
		if (userBean == null || !userBean.isLoggedIn()) {
			address = "/WEB-INF/pages/login.jsp";
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
		request.getSession().invalidate(); // invalidiraj staru sesiju
		HttpSession newSession = request.getSession(); // kreiraj novu sesiju
		session = newSession; // dodijeli novu staroj zbog koda

		if (action == null || action.equals("")) {
			response.sendRedirect("./LoginController?action=login");
		} else if (action.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (!GFG.isSql_inj(username) && !GFG.isSql_inj(password)) {
				if (!GFG.isXSS_attack(username) && !GFG.isXSS_attack(password)) {
					UserBean userBean = new UserBean();
					if (userBean.login(username, password)) { 
						System.out.println(UserBean.token);
						userBean.setLoggedIn(true);
						userBean.setUser(userBean.getUserByUserName(username, password));
						session.setAttribute("userBean", userBean);
						session.setAttribute("userName", userBean.getUser().getName());
						System.out.println(UserBean.loggedUsers);
						UserBean ub = (UserBean) session.getAttribute("userBean");
						if (ub != null && ub.isLoggedIn()) {
							response.sendRedirect("./TokenController?action=token");
						}
					} else if (username != null || password != null) {
						session.setAttribute("notification", "Pogresni parametri za pristup!");
						response.sendRedirect("./LoginController?");
					} else {
						response.sendRedirect("./LoginController?action=login");
					}
				} else {
					session.setAttribute("notification", "Dogodio se pokusaj napada!");
					GFG.writeToFile("Possible XSS injection attack on login page: username: " + username
							+ ", password: " + password);
					response.sendRedirect("./ErrorController");
				}
			} else {
				session.setAttribute("notification", "Dogodio se pokusaj napada!");
				GFG.writeToFile("Possible SQL injection attack on login page: username: " + username + ", password: "
						+ password);
				response.sendRedirect("./ErrorController");
			}
		} else if (action.equals("logout")) {
			session.invalidate();
			response.sendRedirect("./LoginController");
		} else {
			session.setAttribute("notification", "Dogodila se greska!");
			response.sendRedirect("./ErrorController");
		}

	}
}
