
package edu.demo.voting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null
            || !"voter".equals(session.getAttribute("role"))) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/vote.jsp")
            .forward(req, resp);
    }

    @Override
    protected void doPost(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null
            || !"voter".equals(session.getAttribute("role"))) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!WebUtil.validCsrf(req)) {
            resp.sendError(403);
            return;
        }

        try {
            boolean success = XmlStore.castVote(
                (String) session.getAttribute("username"),
                req.getParameter("candidate")
            );

            req.setAttribute(
                "message",
                success
                    ? "Your vote has been recorded."
                    : "You have already voted or selected an invalid candidate."
            );

            req.getRequestDispatcher("/WEB-INF/vote.jsp")
                .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
