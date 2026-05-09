<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Add Animal</title>
<meta charset="UTF-8">

<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">

<style>
body{background:#000;color:#fff;font-family:Poppins;text-align:center}

.box{
width:420px;
margin:auto;
margin-top:80px;
padding:30px;
background:#111;
border-radius:15px;
}

input,select{
width:100%;
padding:12px;
margin:10px 0;
border-radius:8px;
border:none;
}

button{
width:100%;
padding:12px;
background:#d4af37;
border:none;
border-radius:25px;
font-weight:bold;
}
</style>
</head>

<body>

<div class="box">

<h2>➕ Register Animal</h2>

<form action="AddAnimalServlet" method="post">

<input type="hidden" name="detection_id" value="<%= request.getParameter("detection_id") %>">

<input name="animal_name" placeholder="Enter Name (Luna, Sita...)" required>

<select name="species_id" required>
<option value="1">Tiger</option>
<option value="2">Elephant</option>
<option value="3">Leopard</option>
<option value="4">Deer</option>
<option value="5">Lion</option>
<option value="6">Bear</option>
<option value="7">Monkey</option>
<option value="8">Rhino</option>
</select>

<input name="gender" placeholder="Gender">
<input name="age" type="number" placeholder="Age">

<button>Save Animal</button>

</form>

</div>

</body>
</html>