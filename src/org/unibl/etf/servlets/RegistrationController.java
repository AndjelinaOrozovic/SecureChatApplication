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
import org.unibl.etf.chatApp.DigitalCertificates;
import org.unibl.etf.chatApp.GFG;
import org.unibl.etf.model.User;

/**
 * Servlet implementation class RegistrationController
 */
@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("Content-Security-Policy", "default-src 'self'"); // CSP
		request.setCharacterEncoding("UTF-8");
		String address = "/WEB-INF/pages/registration.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		session.setAttribute("notification", "");

		if (action != null && action.equals("registration")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String passwordConfirm = request.getParameter("passwordConfirm");
			String email = request.getParameter("email");
			String name = request.getParameter("name");
			String surname = request.getParameter("surname");
			UserBean userBean = new UserBean();
			try {
				if (username != null) {
					if (password.equals(passwordConfirm)) {
						if (!GFG.isSql_inj(surname) && !GFG.isSql_inj(name) && !GFG.isSql_inj(username)
								&& !GFG.isSql_inj(password) && !GFG.isSql_inj(passwordConfirm)) {
							if (!GFG.isXSS_attack(surname) && !GFG.isXSS_attack(name) && !GFG.isXSS_attack(username)
									&& !GFG.isXSS_attack(password) && !GFG.isXSS_attack(passwordConfirm)) {
								if (userBean.isUserNameAllowed(request.getParameter("username"))
										&& userBean.isEmailAllowed(email)) {
									if (DigitalCertificates.generateCertificate(username)) {
										User user = new User(name, surname, username, password, email);
										if (userBean.add(user)) {
											DigitalCertificates.sendGeneratedCertificate(username);
											session.setAttribute("notification", "Nalog je uspjesno kreiran!");
											response.sendRedirect("./RegistrationController?action=registration");
										} else {
											session.setAttribute("notification", "Dogodila se greska!");
											response.sendRedirect("./RegistrationController?action=registration");
										}
									}
								} else {
									session.setAttribute("notification", "Username i/ili email je zauzet!");
									response.sendRedirect("./RegistrationController?action=registration");
								}
							} else {
								session.setAttribute("notification", "Dogodio se pokusaj napada!");
								GFG.writeToFile("Posible XSS injection attack on registration page: name: " + name
										+ ", surname: " + surname + ", username: " + username + ", password: "
										+ password + ", passwordConfirm: " + passwordConfirm);
								response.sendRedirect("./ErrorController");
							}
						} else {
							session.setAttribute("notification", "Dogodio se pokusaj napada!");
							GFG.writeToFile("Possible SQL injection attack on registration page: name: " + name
									+ ", surname: " + surname + ", username: " + username + ", password: " + password
									+ ", passwordConfirm: " + passwordConfirm);
							response.sendRedirect("./ErrorController");
						}
					} else {
						session.setAttribute("notification", "Unesite isti password!");
						response.sendRedirect("./RegistrationController?action=registration");
					}
				} else {
					response.sendRedirect("./RegistrationController?action=registration");
				}
			} catch (Exception e) {
				session.setAttribute("notification", "Dogodila se greska!");
				response.sendRedirect("./RegistrationController");
			}
		} else {
			UserBean userBean = (UserBean) session.getAttribute("userBean");
			if (userBean == null || !userBean.isLoggedIn()) {
				response.sendRedirect("./LoginController");
			}
		}
	}
}
