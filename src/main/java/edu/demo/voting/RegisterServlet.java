
package edu.demo.voting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/register.jsp")
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

            boolean success = XmlStore.register(
                req.getParameter("username").trim(),
                req.getParameter("name").trim(),
                req.getParameter("password")
            );

            if (success) {
                resp.sendRedirect(
                    req.getContextPath() + "/login?registered=1"
                );
            } else {
                req.setAttribute(
                    "error",
                    "Registration failed. Username may exist or password is too short."
                );

                req.getRequestDispatcher("/WEB-INF/register.jsp")
                    .forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
