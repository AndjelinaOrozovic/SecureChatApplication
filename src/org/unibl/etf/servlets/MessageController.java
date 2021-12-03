package org.unibl.etf.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unibl.etf.beans.MessageBean;
import org.unibl.etf.beans.UserBean;
import org.unibl.etf.chatApp.GFG;
import org.unibl.etf.chatApp.MailSendingClass;
import org.unibl.etf.model.Message;
import org.unibl.etf.model.User;

/**
 * Servlet implementation class MessageController
 */
@WebServlet("/MessageController")
public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.addHeader("Content-Security-Policy", "default-src 'self'"); //CSP
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String address = "/WEB-INF/pages/messages.jsp";
		String action = request.getParameter("action");
		session.setAttribute("notification", "");

		UserBean userBean = (UserBean) session.getAttribute("userBean");
		ArrayList<Integer> idMessages = new ArrayList<Integer>();
		MessageBean messageBean = new MessageBean();
		session.setAttribute("messageBean", messageBean);
		ArrayList<Message> messagesBeforeSorting = new ArrayList<Message>();
		ArrayList<String> messagesForUser = new ArrayList<String>();
		User userReceiver = new User();

		if (action != null && action.equals("logout")) {
			session.invalidate();
			address = "/WEB-INF/pages/login.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		} else if (action != null && action.equals("contacts")) {
			UserBean ub = (UserBean) session.getAttribute("userBean");
			if (ub != null && ub.isLoggedIn()) {
				address = "/WEB-INF/pages/contacts.jsp";
				RequestDispatcher dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			} else {
				session.setAttribute("notification", "Dogodila se greska!");
				address = "/WEB-INF/pages/404.jsp";
				RequestDispatcher dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			}
		} else if (action != null && action.equals("showMessages")) {
			UserBean ub = (UserBean) session.getAttribute("userBean");
			if (ub != null && ub.isLoggedIn()) {
				for (User u : userBean.getAll()) {
					if (request.getParameter("receiver") != null) {
						if (request.getParameter("receiver").equals(u.getUsername())) {
							userReceiver = u;
							session.setAttribute("receiver", userReceiver.getName());
							Integer idReceiver = userReceiver.getIdUser();
							session.setAttribute("idReceiver", idReceiver);
						}
					} else if (session.getAttribute("receiver") != null) {
						if (session.getAttribute("receiver").equals(u.getUsername())) {
							userReceiver = u;
							session.setAttribute("receiver", userReceiver.getName());
							Integer idReceiver = userReceiver.getIdUser();
							session.setAttribute("idReceiver", idReceiver);
						}
					}
				}

				for (Message m : messageBean.getForUser(userBean.getUser().getIdUser(), userReceiver.getIdUser())) {
					messagesBeforeSorting.add(m);
					idMessages.add(m.getIdMessage());
				}
				for (Message m : messageBean.getForUser(userReceiver.getIdUser(), userBean.getUser().getIdUser())) {
					messagesBeforeSorting.add(m);
					idMessages.add(m.getIdMessage());
				}

				Collections.sort(idMessages);
				for (int c : idMessages) {
					for (Message m : messagesBeforeSorting) {
						if (c == m.getIdMessage())
							messagesForUser.add(m.getContent());
					}
				}
				session.setAttribute("messagesForUser", messagesForUser);

				if (!UserBean.loggedUsers.contains(userReceiver)) {
					UserBean.loggedUsers.remove(ub.getUser());
					UserBean.loggedUsers.remove(userBean.getUser());
					System.out.println(UserBean.loggedUsers);
					response.setHeader("Clear-Site-Data", "\"cache\""); // DODANO
					session.invalidate();
					response.sendRedirect("./ErrorController");

				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher(address);
					dispatcher.forward(request, response);
				}

			} else {
				session.setAttribute("notification", "Dogodila se greska!");
				address = "/WEB-INF/pages/404.jsp";
				RequestDispatcher dispatcher = request.getRequestDispatcher(address);
				dispatcher.forward(request, response);
			}

		} else {
			session.setAttribute("notification", "Dogodila se greska!");
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

		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		UserBean userBean = (UserBean) session.getAttribute("userBean");

		if (action != null && action.equals("sendMessage")) {
			String messageForSending = request.getParameter("messageForSending");
			if (!GFG.isSql_inj(messageForSending)) {
				if (!GFG.isXSS_attack(messageForSending)) {
					MessageBean mB = new MessageBean();
					Message message = new Message(messageForSending, userBean.getUser().getIdUser(),
							(Integer) session.getAttribute("idReceiver"));
					mB.save(message);
					String receiver = (String) session.getAttribute("receiver");
					response.sendRedirect("./MessageController?action=showMessages&receiver=" + receiver);
				} else {
					MailSendingClass.sendWarningWithMail(userBean.getUser().getEmail(), "Dogodio se pokusaj napada! SQL injection attack!");
					MailSendingClass.sendWarningWithMail(UserBean.getEmailById((Integer) session.getAttribute("idReceiver")), "Dogodio se pokusaj napada! SQL injection attack!");
					session.setAttribute("notification", "Dogodio se pokusaj napada!");
					GFG.writeToFile("Possible XSS attack on messages page: message: " + messageForSending + " idSender: "
							+ userBean.getUser().getIdUser() + " idReceiver: "
							+ (Integer) session.getAttribute("idReceiver"));
					if (UserBean.loggedUsers.remove(userBean.getUser())) {
						System.out.println(UserBean.loggedUsers);
					}
					//response.setHeader("Clear-Site-Data", "\"cache\"");
					session.invalidate();
					response.sendRedirect("./ErrorController");
				}
			} else {
				MailSendingClass.sendWarningWithMail(userBean.getUser().getEmail(), "Dogodio se pokusaj napada! SQL injection attack!");
				MailSendingClass.sendWarningWithMail(UserBean.getEmailById((Integer) session.getAttribute("idReceiver")), "Dogodio se pokusaj napada! SQL injection attack!");
				session.setAttribute("notification", "Dogodio se pokusaj napada!");
				GFG.writeToFile("Possible SQL injection attack on messages page: message: " + messageForSending
						+ " idSender: " + userBean.getUser().getIdUser() + " idReceiver: "
						+ (Integer) session.getAttribute("idReceiver"));
				if (UserBean.loggedUsers.remove(userBean.getUser())) {
					System.out.println(UserBean.loggedUsers);
				}
				//response.setHeader("Clear-Site-Data", "\"cache\"");
				session.invalidate();
				response.sendRedirect("./ErrorController");
			}
		} else {
			session.setAttribute("notification", "Dogodila se greska!");
			String address = "/WEB-INF/pages/404.jsp";
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}

	}
}