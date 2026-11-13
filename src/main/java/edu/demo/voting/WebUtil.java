package edu.demo.voting;

import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebUtil {

    public static String escape(String s) {
        if (s == null) return "";

        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }

    public static String csrfToken(HttpSession session) {

        String token = (String) session.getAttribute("csrf");

        if (token == null) {
            byte[] bytes = new byte[24];

            new SecureRandom().nextBytes(bytes);

            token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

            session.setAttribute("csrf", token);
        }

        return token;
    }

    public static boolean validCsrf(
        HttpServletRequest request
    ) {

        HttpSession session = request.getSession(false);

        return session != null
            && session.getAttribute("csrf") != null
            && session.getAttribute("csrf")
                .equals(request.getParameter("csrf"));
    }
}