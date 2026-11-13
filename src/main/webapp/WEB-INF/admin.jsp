
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
    Map<String, Integer> results =
        (Map<String, Integer>) request.getAttribute("results");

    Integer totalVotes =
        (Integer) request.getAttribute("totalVotes");

    Integer totalVoters =
        (Integer) request.getAttribute("totalVoters");

    String winner =
        (String) request.getAttribute("winner");

    if (results == null) {
        results = new java.util.LinkedHashMap<>();
        results.put("Candidate A", 0);
        results.put("Candidate B", 0);
        results.put("Candidate C", 0);
    }

    if (totalVotes == null) totalVotes = 0;
    if (totalVoters == null) totalVoters = 0;
    if (winner == null) winner = "No votes yet";

    int participation = totalVoters == 0 ? 0 :
        totalVotes * 100 / totalVoters;

    boolean hasVotes = totalVotes > 0;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin Dashboard | Online Voting System</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            color: #1e293b;
        }

        .navbar {
            background: #172554;
            color: white;
            padding: 20px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        .navbar h2 {
            font-size: 22px;
        }

        .navbar span {
            color: #bfdbfe;
            font-size: 14px;
        }

        .container {
            max-width: 1150px;
            margin: 35px auto;
            padding: 0 20px;
        }

        .heading {
            margin-bottom: 25px;
        }

        .heading h1 {
            font-size: 30px;
            margin-bottom: 8px;
        }

        .heading p {
            color: #64748b;
        }

        .cards {
            display: grid;
            grid-template-columns:
                repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 25px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 4px 15px #00000008;
            border: 1px solid #e2e8f0;
        }

        .card h3 {
            color: #64748b;
            font-size: 15px;
            margin-bottom: 15px;
        }

        .number {
            font-size: 34px;
            font-weight: bold;
            color: #2563eb;
        }

        .winner-card {
            background: linear-gradient(
                120deg, #166534, #15803d
            );
            color: white;
            padding: 30px;
            border-radius: 16px;
            margin-bottom: 30px;
            box-shadow: 0 5px 20px #16653422;
        }

        .winner-card h2 {
            margin-bottom: 15px;
        }

        .winner-card h1 {
            font-size: 30px;
            margin-bottom: 10px;
        }

        .winner-card p {
            color: #dcfce7;
        }

        .section {
            background: white;
            padding: 25px;
            border-radius: 14px;
            border: 1px solid #e2e8f0;
            margin-bottom: 25px;
        }

        .section h2 {
            margin-bottom: 25px;
        }

        .candidate {
            margin-bottom: 25px;
        }

        .candidate:last-child {
            margin-bottom: 0;
        }

        .candidate-info {
            display: flex;
            justify-content: space-between;
            gap: 10px;
            margin-bottom: 10px;
            flex-wrap: wrap;
        }

        .candidate-info strong {
            font-size: 16px;
        }

        .candidate-info span {
            color: #64748b;
        }

        .bar {
            width: 100%;
            height: 14px;
            background: #e2e8f0;
            border-radius: 20px;
            overflow: hidden;
        }

        .fill {
            height: 100%;
            background: #2563eb;
            border-radius: 20px;
            transition: width 0.5s;
        }

        .winner-fill {
            background: #16a34a;
        }

        .status {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            background: #dcfce7;
            color: #166534;
            font-size: 12px;
            font-weight: bold;
        }

        .empty {
            background: #eff6ff;
            color: #1d4ed8;
            padding: 18px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 25px;
        }

        .btn {
            padding: 12px 22px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            display: inline-block;
        }

        .refresh {
            background: #2563eb;
            color: white;
        }

        .logout {
            background: #fee2e2;
            color: #b91c1c;
        }

        .footer {
            text-align: center;
            padding: 25px;
            color: #64748b;
            font-size: 13px;
        }

        @media(max-width: 600px) {
            .heading h1 {
                font-size: 24px;
            }

            .winner-card h1 {
                font-size: 24px;
            }

            .navbar {
                padding: 18px;
            }
        }
    </style>
</head>

<body>

<nav class="navbar">
    <h2>Online Voting System</h2>
    <span>Administrator Dashboard</span>
</nav>

<main class="container">

    <div class="heading">
        <h1>Admin Dashboard</h1>
        <p>Monitor election statistics and voting results.</p>
    </div>

    <div class="cards">

        <div class="card">
            <h3>Registered Voters</h3>
            <div class="number"><%= totalVoters %></div>
        </div>

        <div class="card">
            <h3>Total Votes Cast</h3>
            <div class="number"><%= totalVotes %></div>
        </div>

        <div class="card">
            <h3>Participation Rate</h3>
            <div class="number"><%= participation %>%</div>
        </div>

    </div>

    <div class="winner-card">
        <h2>🏆 Election Winner</h2>

        <h1><%= winner %></h1>

        <p>
            <% if (hasVotes) { %>
                Winner based on the highest number of votes.
            <% } else { %>
                The election has not received any votes yet.
            <% } %>
        </p>
    </div>

    <div class="section">

        <h2>📊 Candidate-wise Results</h2>

        <% if (!hasVotes) { %>

            <div class="empty">
                No votes have been recorded yet.
                Results will appear after voters cast their votes.
            </div>

        <% } %>

        <% for (Map.Entry<String, Integer> entry :
                results.entrySet()) {

            int votes = entry.getValue();

            int percentage = totalVotes == 0 ? 0 :
                votes * 100 / totalVotes;

            boolean isWinner = hasVotes &&
                entry.getKey().equals(winner);
        %>

            <div class="candidate">

                <div class="candidate-info">

                    <strong>
                        <%= entry.getKey() %>

                        <% if (isWinner) { %>
                            🏆 <span class="status">Winner</span>
                        <% } %>
                    </strong>

                    <span>
                        <%= votes %> votes
                        ( <%= percentage %>% )
                    </span>

                </div>

                <div class="bar">

                    <div class="fill <%= isWinner ? "winner-fill" : "" %>"
                         style="width: <%= percentage %>%;">
                    </div>

                </div>

            </div>

        <% } %>

    </div>

    <div class="actions">

        <a class="btn refresh"
           href="<%= request.getContextPath() %>/admin">
            ↻ Refresh Results
        </a>

        <a class="btn logout"
           href="<%= request.getContextPath() %>/logout">
            Logout
        </a>

    </div>

</main>

<footer class="footer">
    Online Voting System | University Project
</footer>

</body>
</html>
