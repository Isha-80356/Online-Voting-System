<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.demo.voting.WebUtil" %>

<%
if(session.getAttribute("role") == null ||
   !"voter".equals(session.getAttribute("role"))) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <title>Cast Your Vote</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">

    <h1>Cast Your Vote</h1>

    <p>
        Welcome,
        <%= WebUtil.escape((String)session.getAttribute("username")) %>
    </p>

    <% if(request.getAttribute("message") != null) { %>
        <p class="success">
            <%= WebUtil.escape((String)request.getAttribute("message")) %>
        </p>
    <% } %>

    <form method="post" action="vote">

        <input type="hidden" name="csrf"
        value="<%= WebUtil.csrfToken(session) %>">

        <label class="choice">
            <input type="radio" name="candidate"
            value="Candidate A" required>
            Candidate A
        </label>

        <label class="choice">
            <input type="radio" name="candidate"
            value="Candidate B">
            Candidate B
        </label>

        <label class="choice">
            <input type="radio" name="candidate"
            value="Candidate C">
            Candidate C
        </label>

        <button type="submit">Submit Vote</button>

    </form>

    <form method="post" action="logout">
        <input type="hidden" name="csrf"
        value="<%= WebUtil.csrfToken(session) %>">

        <button class="secondary" type="submit">Logout</button>
    </form>

</main>
</body>
</html>