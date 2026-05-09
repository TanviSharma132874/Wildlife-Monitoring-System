package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AssignAnimalServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String detectionParam = request.getParameter("detection_id");
        String animalParam = request.getParameter("animal_id");

        if (detectionParam == null || detectionParam.isEmpty() ||
            animalParam == null || animalParam.isEmpty()) {

            response.sendRedirect("DetectionServlet");
            return;
        }

        try {
            int detectionId = Integer.parseInt(detectionParam);
            int animalId = Integer.parseInt(animalParam);

            try (Connection conn = DBConnection.getConnection()) {

                conn.setAutoCommit(false);

                // UPDATE DETECTION
                PreparedStatement ps1 = conn.prepareStatement(
                        "UPDATE detections SET animal_id=? WHERE detection_id=?"
                );
                ps1.setInt(1, animalId);
                ps1.setInt(2, detectionId);
                ps1.executeUpdate();

                // GET DETECTION DATA + EMBEDDING
                PreparedStatement ps2 = conn.prepareStatement(
                        "SELECT d.detected_at, c.location, d.embedding " +
                        "FROM detections d " +
                        "LEFT JOIN cameras c ON d.camera_id=c.id " +
                        "WHERE d.detection_id=?"
                );
                ps2.setInt(1, detectionId);
                ResultSet rs = ps2.executeQuery();

                if (rs.next()) {

                    Timestamp time = rs.getTimestamp("detected_at");
                    String location = rs.getString("location");
                    String embedding = rs.getString("embedding");

                    // UPDATE ANIMAL WITH EMBEDDING
                    PreparedStatement ps3 = conn.prepareStatement(
                            "UPDATE animals SET last_seen_time=?, last_seen_location=?, embedding=? WHERE animal_id=?"
                    );

                    ps3.setTimestamp(1, time);
                    ps3.setString(2, location);
                    ps3.setString(3, embedding);
                    ps3.setInt(4, animalId);

                    ps3.executeUpdate();
                }

                conn.commit();
            }

            response.sendRedirect("DetectionServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}