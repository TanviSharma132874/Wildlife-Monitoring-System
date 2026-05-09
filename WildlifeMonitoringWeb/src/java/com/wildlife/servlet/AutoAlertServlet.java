package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AutoAlertServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getConnection()) {

            int cameraId = Integer.parseInt(request.getParameter("camera_id"));
            int speciesId = Integer.parseInt(request.getParameter("species_id"));
            float confidence = Float.parseFloat(request.getParameter("confidence"));
            String imageName = request.getParameter("image_name");

            // optional location from camera
            String location = request.getParameter("location"); // add this from Python later

            String level =
                    confidence >= 0.7 ? "High" :
                    confidence >= 0.45 ? "Medium" : "Low";

            // 🔥 INSERT DETECTION
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO detections(camera_id, species_id, confidence, alert_level, image_path, location, timestamp) VALUES(?,?,?,?,?,?,NOW())"
            );

            ps.setInt(1, cameraId);
            ps.setInt(2, speciesId);
            ps.setFloat(3, confidence);
            ps.setString(4, level);
            ps.setString(5, imageName);
            ps.setString(6, location);

            ps.executeUpdate();

            // 🔥 INSERT ALERT
            PreparedStatement alert = conn.prepareStatement(
                "INSERT INTO alerts(camera_id, level, message, created_at) VALUES(?,?,?,NOW())"
            );

            alert.setInt(1, cameraId);
            alert.setString(2, level);
            alert.setString(3, "Animal detected with confidence: " + confidence);

            alert.executeUpdate();

            response.getWriter().print("OK");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}