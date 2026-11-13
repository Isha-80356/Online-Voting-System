package edu.demo.voting;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    protected void doPost(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        if (!WebUtil.validCsrf(req)) {
            resp.sendError(403);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        resp.sendRedirect(req.getContextPath() + "/");
    }
}