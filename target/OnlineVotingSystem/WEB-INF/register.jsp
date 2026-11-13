<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.demo.voting.WebUtil" %>

<!DOCTYPE html>
<html>
<head>
    <title>Voter Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<main class="card">

    <h1>Voter Registration</h1>

    <% if(request.getAttribute("error") != null) { %>
        <p class="error">
            <%= WebUtil.escape((String)request.getAttribute("error")) %>
        </p>
    <% } %>

    <form method="post" action="register">

        <input type="hidden" name="csrf"
        value="<%= WebUtil.csrfToken(session) %>">

        <label>Full Name</label>
        <input type="text" name="name" required maxlength="80">

        <label>Username</label>
        <input type="text" name="username" required maxlength="40">

        <label>Password (minimum 8 characters)</label>
        <input type="password" name="password"
        required minlength="8">

        <button type="submit">Register</button>
    </form>

   <p><a href="login.jsp">Already registered? Login</a></p>
</main>
</body>
</html>