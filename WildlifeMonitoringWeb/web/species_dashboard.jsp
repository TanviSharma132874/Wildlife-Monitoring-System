<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<style>
body{background:#000;color:#fff;font-family:Poppins}

.cards{
display:flex;
gap:20px;
padding:20px;
overflow-x:auto;
}

.card{
width:220px;
background:#1c1c1c;
padding:10px;
border-radius:10px;
border:2px solid red;
}

.card img{
width:100%;
height:140px;
object-fit:cover;
}

table{
width:90%;
margin:30px auto;
}

th{background:#d4af37;color:black}
td{padding:10px;text-align:center}
</style>
</head>

<body>

<h2 style="text-align:center;color:#d4af37">Detection Dashboard</h2>

<div class="cards">

<%
List<Map<String,Object>> cards =
(List<Map<String,Object>>)request.getAttribute("cards");

for(Map<String,Object> c : cards){
%>

<div class="card">

Cam #<%=c.get("camera")%><br>
<%=c.get("location")%><br>
<%=c.get("level")%> | <%=c.get("confidence")%>

<img src="image?name=<%=c.get("image")%>">

</div>

<% } %>

</div>

<table>

<tr>
<th>ID</th>
<th>Camera</th>
<th>Location</th>
<th>Level</th>
<th>Confidence</th>
<th>Time</th>
<th>Image</th>
<th>Animal</th>
</tr>

<%
List<Map<String,Object>> table =
(List<Map<String,Object>>)request.getAttribute("table");

for(Map<String,Object> t : table){
%>

<tr>

<td><%=t.get("id")%></td>
<td><%=t.get("camera")%></td>
<td><%=t.get("location")%></td>
<td><%=t.get("level")%></td>
<td><%=t.get("confidence")%></td>
<td><%=t.get("time")%></td>

<td>
<img src="image?name=<%=t.get("image")%>" width="60">
</td>

<td>
<a href="animal-profile?animal_id=<%=t.get("animal_id")%>">
<%=t.get("animal")%>
</a>
</td>

</tr>

<% } %>

</table>

</body>
</html>