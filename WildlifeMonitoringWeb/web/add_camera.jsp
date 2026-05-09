<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Add Camera</title>
<meta charset="UTF-8">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{background:black;color:white;font-family:Poppins}
.form-box{
width:400px;margin:80px auto;padding:30px;
background:rgba(255,255,255,0.1);
border-radius:12px;
}
input,button{
width:100%;margin:10px 0;padding:10px;border-radius:8px;
}
button{background:#d4af37;font-weight:bold}
</style>
</head>

<body>

<div class="form-box">
<h2>Add Camera</h2>

<form action="CameraServlet" method="post">
<input name="location" placeholder="Location" required>
<input name="latitude" placeholder="Latitude" required>
<input name="longitude" placeholder="Longitude" required>

<button>Save</button>
</form>

</div>

</body>
</html>