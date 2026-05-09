<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<style>
body{background:black;color:white;font-family:Poppins}

table{
width:80%;
margin:auto;
margin-top:50px;
border-collapse:collapse;
}

th{
background:#d4af37;
color:black;
padding:10px;
}

td{
padding:10px;
border-bottom:1px solid #333;
text-align:center;
}
</style>
</head>

<body>

<h2 style="text-align:center;color:#d4af37">Animal Records</h2>

<table>

<tr>
<th>ID</th>
<th>Name</th>
<th>Gender</th>
<th>Age</th>
<th>View</th>
</tr>

<%
List<Map<String,Object>> animals =
(List<Map<String,Object>>)request.getAttribute("animals");

for(Map<String,Object> a : animals){
%>

<tr>

<td><%=a.get("id")%></td>
<td><%=a.get("name")%></td>
<td><%=a.get("gender")%></td>
<td><%=a.get("age")%></td>

<td>
<a href="animal-profile?animal_id=<%=a.get("id")%>">
View Profile
</a>
</td>

</tr>

<% } %>

</table>

</body>
</html>