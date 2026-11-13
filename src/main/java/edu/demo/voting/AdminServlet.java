
package edu.demo.voting;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null
            || !"admin".equals(session.getAttribute("role"))) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Map<String, Integer> results = XmlStore.results();

            req.setAttribute("results", results);
            req.setAttribute("totalVotes", XmlStore.totalVotes());
            req.setAttribute("totalVoters", XmlStore.totalVoters());
            req.setAttribute("winner", XmlStore.getWinner());

            req.getRequestDispatcher("/WEB-INF/admin.jsp")
                .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
