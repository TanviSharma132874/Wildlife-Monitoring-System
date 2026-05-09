<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Add Alert</title>
<meta charset="UTF-8">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
<style>
body{background:black;color:white;font-family:Poppins}
.box{
width:400px;margin:80px auto;padding:25px;
background:rgba(255,255,255,0.1);
border-radius:10px;
}
input,select,button{
width:100%;margin:10px 0;padding:10px;border-radius:8px;
}
button{background:#d4af37}
</style>
</head>

<body>

<div class="box">
<h2>? Add Alert</h2>

<form action="AlertServlet" method="post">

<input name="cameraId" placeholder="Camera ID" required>

<select name="level">
<option>Low</option>
<option>Medium</option>
<option>High</option>
</select>

<input name="message" placeholder="Message" required>

<button>Submit</button>

</form>
</div>

</body>
</html>