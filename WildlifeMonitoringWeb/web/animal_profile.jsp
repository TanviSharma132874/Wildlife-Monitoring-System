<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<title>Animal Profile</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body{background:#000;color:#fff;font-family:Poppins}

.card{
    width:450px;
    margin:40px auto;
    padding:25px;
    border:1px solid gold;
    border-radius:12px;
    background:#0a0a0a;
}

img{
    width:100%;
    border-radius:10px;
    margin-top:10px;
}

.section{ margin-top:15px; }

.history{
    background:#1c1c1c;
    padding:12px;
    margin:10px 0;
    border-radius:8px;
}

.status-active{ color:lightgreen; }
.status-low{ color:orange; }
.status-missing{ color:red; }
.status-nodata{ color:gray; }

hr{
    border:0;
    height:1px;
    background:#333;
    margin:15px 0;
}
</style>
</head>

<body>

<%
Map<String,Object> a = (Map<String,Object>)request.getAttribute("animal");
List<Map<String,Object>> history =
(List<Map<String,Object>>)request.getAttribute("history");
List<Double> conf =
(List<Double>)request.getAttribute("confidenceList");

String status = (String)request.getAttribute("status");

if(a == null){
    out.println("❌ No Data Found");
    return;
}

if(history == null) history = new ArrayList<>();
if(conf == null) conf = new ArrayList<>();
if(status == null) status = "UNKNOWN";

String statusClass = "status-active";
if(status.equals("MISSING")) statusClass = "status-missing";
else if(status.equals("LOW ACTIVITY")) statusClass = "status-low";
else if(status.equals("NO DATA")) statusClass = "status-nodata";
%>

<div class="card">

<h2>🐾 <%=a.get("name")%></h2>

<div class="section">
<b>ID:</b> <%=a.get("id")%><br>
<b>Species:</b> <%=a.get("species")%><br>
<b>Gender:</b> <%=a.get("gender")%><br>
<b>Age:</b> <%=a.get("age")%><br>
<b>Last Seen:</b> <%=a.get("last_seen")%><br>
<b>Location:</b> <%=a.get("location")%><br>
</div>

<hr>

<div class="section">
<b>Status:
<span class="<%=statusClass%>"><%=status%></span>
</b>
</div>

<hr>

<div class="section">
<h3>📈 Confidence Trend</h3>

<% if(!"MISSING".equals(status) && conf.size() > 0){ %>
<canvas id="chart"></canvas>
<% } else { %>
<p style="color:gray">No graph data available</p>
<% } %>

</div>

<hr>

<div class="section">
<h3>📍 Detection History</h3>

<% if(history.size() > 0){
   for(Map<String,Object> h : history){ %>

<div class="history">

<b>Confidence:</b> <%=h.get("confidence")%><br>
<b>Time:</b> <%=h.get("time")%><br>
<b>Location:</b> <%=h.get("location")%><br>

<%
String img = (String)h.get("image");
if(img != null && !img.trim().isEmpty()){
%>
<img src="<%= request.getContextPath() %>/image?name=<%= img %>">
<% } %>

</div>

<% } } else { %>

<p style="color:gray">No detection history available</p>

<% } %>

</div>

</div>

<script>
<% if(!"MISSING".equals(status) && conf.size() > 0){ %>

new Chart(document.getElementById('chart'), {
    type: 'line',
    data: {
        labels: [<% for(int i=0;i<conf.size();i++){ %>'<%=i%>',<% } %>],
        datasets: [{
            label: 'Confidence',
            data: [<% for(Double d:conf){ %><%=d%>,<% } %>],
            borderColor: 'yellow',
            fill: false
        }]
    }
});

<% } %>
</script>

</body>
</html>