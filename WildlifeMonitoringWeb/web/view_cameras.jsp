<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.wildlife.dao.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
<title>Registered Cameras</title>
<meta charset="UTF-8">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body, html {
margin:0;
height:100%;
color:#fff;
font-family:'Poppins';
background:#000;
}

#bg-video{
position:fixed;
width:100%;
height:100%;
object-fit:cover;
z-index:-1;
filter:brightness(60%);
}

.navbar{
background:rgba(0,0,0,0.85);
border-bottom:2px solid #d4af37;
}

.header{
padding:20px;
text-align:center;
color:#d4af37;
}

.table-wrap{
width:90%;
margin:auto;
background:rgba(0,0,0,0.6);
padding:20px;
border-radius:15px;
}

table{
width:100%;
text-align:center;
}

th{
background:rgba(212,175,55,0.2);
padding:10px;
}
td{
padding:8px;
}
</style>
</head>

<body>

<video autoplay muted loop id="bg-video">
<source src="images/video.mp4">
</video>

<nav class="navbar navbar-dark px-3">
<span class="navbar-brand">? Wildlife Monitoring</span>
</nav>

<h1 class="header">Registered Cameras</h1>

<div class="table-wrap">
<table>
<tr>
<th>ID</th>
<th>Location</th>
<th>Latitude</th>
<th>Longitude</th>
</tr>

<%
Connection conn = DBConnection.getConnection();
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery("SELECT * FROM cameras ORDER BY id DESC");

while(rs.next()){
%>

<tr>
<td><%= rs.getInt("id") %></td>
<td><%= rs.getString("location") %></td>
<td><%= rs.getString("latitude") %></td>
<td><%= rs.getString("longitude") %></td>
</tr>

<% } %>

</table>
</div>

</body>
</html>