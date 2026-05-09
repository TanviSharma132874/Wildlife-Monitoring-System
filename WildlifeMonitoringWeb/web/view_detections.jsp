<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<title>Detection Dashboard</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">

<style>
body{background:#000;color:#fff;font-family:Poppins}
h2{text-align:center;color:#d4af37}

.recent-cards{
display:flex;
gap:15px;
margin:20px;
overflow-x:auto;
}

.card{
background:#1a1a1a;
border-radius:10px;
padding:10px;
width:180px;
border-left:4px solid red;
}

.card img{
width:100%;
height:120px;
object-fit:cover;
border-radius:8px;
}

.card .info{
font-size:12px;
margin-top:5px;
}

.table{
width:90%;
margin:40px auto;
border-collapse:collapse;
}

th{
background:#d4af37;
color:black;
padding:12px;
}

td{
padding:10px;
text-align:center;
border-bottom:1px solid #333;
}

img{
width:80px;
border-radius:8px;
}
</style>
</head>

<body>

<h2>🐅 Wildlife Detection Dashboard</h2>

<%
List<Map<String,Object>> recent =
(List<Map<String,Object>>) request.getAttribute("recentDetections");
%>

<div class="recent-cards">
<% if(recent != null){
   for(Map<String,Object> r : recent){

       String img = (String) r.get("image");

       String imagePath;
       if (img == null || img.trim().isEmpty()) {
           imagePath = "detections/default.jpg";
       } else if (img.startsWith("detections/") || img.startsWith("animals/")) {
           imagePath = img;
       } else {
           imagePath = "detections/" + img;
       }
%>
    <div class="card">
        <img src="<%= request.getContextPath() %>/image?name=<%= imagePath %>">

        <div class="info">
            <b><%= r.get("species") %></b><br>
            <%= (r.get("animal") != null ? r.get("animal") : "Unknown") %><br>
            <%= r.get("confidence") %>
        </div>
    </div>
<% } } %>
</div>

<%
List<Map<String,Object>> detections =
(List<Map<String,Object>>) request.getAttribute("detections");
%>

<table class="table">

<tr>
<th>ID</th>
<th>Camera</th>
<th>Location</th>
<th>Level</th>
<th>Confidence</th>
<th>Time</th>
<th>Image</th>
<th>Animal</th>
<th>Species</th>
</tr>

<% if(detections != null){
   for(Map<String,Object> d : detections){

       String img = (String) d.get("image");

       String imagePath;
       if (img == null || img.trim().isEmpty()) {
           imagePath = "detections/default.jpg";
       } else if (img.startsWith("detections/") || img.startsWith("animals/")) {
           imagePath = img;
       } else {
           imagePath = "detections/" + img;
       }
%>

<tr>
<td><%= d.get("detectionId") %></td>
<td><%= d.get("camera") %></td>
<td><%= d.get("location") %></td>
<td><%= d.get("level") %></td>
<td><%= d.get("confidence") %></td>
<td><%= d.get("time") %></td>

<td>
<img src="<%= request.getContextPath() %>/image?name=<%= imagePath %>">
</td>

<td>
<%
if(d.get("animal") == null){
%>
<a href="add_animal.jsp?detection_id=<%= d.get("detectionId") %>&species_id=<%= d.get("species") %>"
   style="color:#ff4d4d;font-weight:bold;">
   ➕ Register
</a>
<%
}else{
%>
<%= d.get("animal") %>
<%
}
%>
</td>

<td><%= d.get("species") %></td>

</tr>

<% } } %>

</table>

</body>
</html>