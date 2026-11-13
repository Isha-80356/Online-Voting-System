
package edu.demo.voting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/login.jsp")
            .forward(req, resp);
    }

    @Override
    protected void doPost(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            if (!WebUtil.validCsrf(req)) {
                resp.sendError(403);
                return;
            }

            String username = req.getParameter("username").trim();

            String role = XmlStore.authenticate(
                username, req.getParameter("password")
            );

            if (role == null) {
                req.setAttribute(
                    "error", "Invalid username or password."
                );

                req.getRequestDispatcher("/WEB-INF/login.jsp")
                    .forward(req, resp);

                return;
            }

            HttpSession old = req.getSession(false);

            if (old != null) old.invalidate();

            HttpSession session = req.getSession(true);

            session.setAttribute("username", username);
            session.setAttribute("role", role);

            WebUtil.csrfToken(session);

            resp.sendRedirect(
                req.getContextPath()
                + (role.equals("admin") ? "/admin" : "/vote")
            );

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
