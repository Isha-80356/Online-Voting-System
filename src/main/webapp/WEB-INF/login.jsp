<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.demo.voting.WebUtil" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">

    <h1>Login</h1>

    <% if(request.getParameter("registered") != null) { %>
        <p class="success">Registration successful. Please login.</p>
    <% } %>

    <% if(request.getAttribute("error") != null) { %>
        <p class="error">
            <%= WebUtil.escape((String)request.getAttribute("error")) %>
        </p>
    <% } %>

    <form method="post" action="login">

        <input type="hidden" name="csrf"
        value="<%= WebUtil.csrfToken(session) %>">

        <label>Username</label>
        <input type="text" name="username" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <button type="submit">Login</button>
    </form>

    <p><a href="register">Create voter account</a></p>

</main>
</body>
</html>