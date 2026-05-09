<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.wildlife.dao.DBConnection" %>

<!DOCTYPE html>
<html>
<head>
<title>Alerts Dashboard</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">

<style>
body{background:black;color:white;font-family:Poppins}

.cards{
display:flex;
flex-wrap:wrap;
gap:20px;
padding:30px;
justify-content:center;
}

.card{
width:260px;
background:#1a1a1a;
padding:18px;
border-radius:15px;
color:white;
}

.high{border-left:5px solid red}
.medium{border-left:5px solid orange}
.low{border-left:5px solid green}
</style>
</head>

<body>

<h2 style="text-align:center;color:#d4af37">🚨 Alerts Dashboard</h2>

<div class="cards">

<%
try(Connection conn = DBConnection.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM alerts ORDER BY created_at DESC")){

    while(rs.next()){
        String level = rs.getString("level");
        if(level == null) level="low";
        level = level.toLowerCase();
%>

<div class="card <%= level %>">
<b>Camera: <%= rs.getInt("camera_id") %></b><br>
<%= rs.getString("message") %><br>
<small><%= rs.getTimestamp("created_at") %></small>
</div>

<%
    }
}catch(Exception e){
    out.println(e);
}
%>

</div>

</body>
</html>