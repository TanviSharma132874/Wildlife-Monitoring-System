<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<title>Wildlife Dashboard</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">

<style>
body{
    background:#000;
    color:#fff;
    font-family:Poppins;
}

h2{
    text-align:center;
    color:#d4af37;
}

.top{
    display:flex;
    justify-content:center;
    gap:20px;
    margin-top:30px;
}

.stat{
    background:#1c1c1c;
    padding:20px;
    border-radius:12px;
    width:180px;
    text-align:center;
    box-shadow:0 0 15px rgba(212,175,55,0.2);
}

.grid{
    display:flex;
    flex-wrap:wrap;
    gap:25px;
    justify-content:center;
    margin-top:40px;
}

.card{
    width:220px;
    padding:20px;
    border:1px solid gold;
    border-radius:12px;
    text-align:center;
    cursor:pointer;
    transition:0.3s;
}

.card:hover{
    background:#1a1a1a;
    transform:scale(1.05);
}
</style>
</head>

<body>

<h2>🐾 Wildlife Monitoring Dashboard</h2>

<div class="top">
<div class="stat">Animals<br>${animalCount}</div>
<div class="stat">Cameras<br>${cameraCount}</div>
<div class="stat">Detections<br>${detectionCount}</div>
<div class="stat">Alerts<br>${alertCount}</div>
</div>

<div class="grid">

<%
List<Map<String,Object>> species =
(List<Map<String,Object>>)request.getAttribute("speciesCounts");

for(Map<String,Object> s : species){
%>

<div class="card"
onclick="location.href='AnimalListServlet?species_id=<%=s.get("id")%>'">

<h3><%=s.get("name")%></h3>
Animals: <%=s.get("count")%>

</div>

<% } %>

</div>

</body>
</html>