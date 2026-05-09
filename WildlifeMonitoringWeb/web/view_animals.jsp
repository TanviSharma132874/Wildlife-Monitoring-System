<%@ page import="java.sql.*" %>
<%@ page import="com.wildlife.dao.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<title>Animals - Wildlife Monitoring</title>
<meta charset="UTF-8">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">

<style>
body { background: black; color: white; font-family: Arial; }
h2 { text-align: center; margin: 30px; color: #d4af37; }
table { width: 90%; margin: auto; border-collapse: collapse; }
th { background: #3a2f0b; padding: 12px; }
td { padding: 10px; border-bottom: 1px solid #333; text-align: center; }
tr:hover { background: rgba(255,255,255,0.1); }
a { color: #d4af37; text-decoration: none; font-weight: bold; }
a:hover { text-decoration: underline; }

.btn-add {
display: block;
width: fit-content;
margin: 20px auto;
background: #d4af37;
color: black;
padding: 10px 20px;
border-radius: 20px;
text-decoration: none;
}
.btn-add:hover { background: white; }
</style>
</head>

<body>

<h2>🐾 Animal Records</h2>
<a href="add_animal.jsp" class="btn-add">+ Add Animal</a>

<table>
<tr>
<th>ID</th>
<th>Name</th>
<th>Species</th>
<th>Gender</th>
<th>Age</th>
</tr>

<%
Connection conn = null;
Statement st = null;
ResultSet rs = null;

try {
    conn = DBConnection.getConnection();
    st = conn.createStatement();

    rs = st.executeQuery(
        "SELECT a.animal_id, a.animal_name, s.species_name, a.gender, a.age " +
        "FROM animals a JOIN species s ON a.species_id = s.species_id " +
        "ORDER BY a.animal_id DESC"
    );

    while (rs.next()) {
%>

<tr>
<td><%= rs.getInt("animal_id") %></td>

<td>
<a href="AnimalProfileServlet?animal_id=<%= rs.getInt("animal_id") %>">
<%= rs.getString("animal_name") %>
</a>
</td>

<td><%= rs.getString("species_name") %></td>
<td><%= rs.getString("gender") %></td>
<td><%= rs.getInt("age") %></td>
</tr>

<%
    }

} catch (Exception e) {
%>
<tr>
<td colspan="5">Error: <%= e.getMessage() %></td>
</tr>
<%
} finally {
    if (rs != null) rs.close();
    if (st != null) st.close();
    if (conn != null) conn.close();
}
%>

</table>

</body>
</html>