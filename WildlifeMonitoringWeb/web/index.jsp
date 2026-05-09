<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Wildlife Monitoring System</title>

<!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Google Font: Poppins -->
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">

<style>
body, html {
    height: 100%;
    margin: 0;
    font-family: 'Poppins', sans-serif;
    color: #fff;
    overflow: hidden;
}

/* 🎥 Background Video */
#bg-video {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    z-index: -2;
    filter: brightness(55%) blur(3px);
}

/* 🌿 Overlay */
.overlay {
    position: fixed;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.4);
    z-index: -1;
}

/* 🧭 Navbar */
.navbar {
    background: rgba(0,0,0,0.85);
    border-bottom: 2px solid #d4af37;
}

.navbar-brand {
    color: #d4af37 !important;
    font-weight: 700;
}

/* 🏠 Main Section */
.home-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
}

/* 🌟 Title */
.title {
    font-size: 2.7rem;
    font-weight: 700;
    margin-bottom: 20px;
}

/* 🐾 Subtitle */
.subtitle {
    font-size: 1.1rem;
    margin-bottom: 35px;
    opacity: 0.85;
}

/* 🔘 Buttons */
.btn-custom {
    margin: 10px;
    padding: 14px 32px;
    background: linear-gradient(45deg, #d4af37, #c49b31);
    color: #000;
    border-radius: 40px;
    font-weight: 600;
    text-decoration: none;
    transition: 0.3s;
    box-shadow: 0 4px 12px rgba(0,0,0,0.4);
}

.btn-custom:hover {
    background: #fff;
    color: #000;
    transform: scale(1.08);
}

/* 📝 Footer Note */
.small-note {
    margin-top: 30px;
    opacity: 0.8;
    font-size: 0.9rem;
}
</style>
</head>

<body>

<!-- 🎥 Background Video -->
<video autoplay muted loop id="bg-video">
    <source src="image/video.mp4" type="video/mp4">
</video>

<div class="overlay"></div>

<!-- 🧭 Navbar -->
<nav class="navbar navbar-dark">
<div class="container-fluid">
    <span class="navbar-brand">
        <img src="image/logo.png" width="40" class="me-2">
        Wildlife Monitoring System
    </span>
</div>
</nav>

<!-- 🏠 Main Content -->
<div class="home-container">

    <h1 class="title">🌿 Smart Wildlife Monitoring</h1>
    <p class="subtitle">AI-Powered Animal Detection & Forest Safety System</p>

    <div class="d-flex flex-wrap justify-content-center">

        <a href="add_camera.jsp" class="btn-custom">📷 Add Camera</a>
        <a href="add_alert.jsp" class="btn-custom">🚨 Issue Alert</a>
        <a href="view_alerts.jsp" class="btn-custom">📢 View Alerts</a>

        <a href="add_animal.jsp" class="btn-custom">🐾 Add Animal</a>
        <a href="view_animals.jsp" class="btn-custom">🦁 View Animals</a>

        <!-- ✅ FIXED LINK -->
        <a href="DetectionServlet" class="btn-custom">📸 View Detections</a>

        <a href="dashboard" class="btn-custom">📊 Dashboard</a>

    </div>

    <p class="small-note">
        Protecting Wildlife with AI • Smart Forest Surveillance 🌍
    </p>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>