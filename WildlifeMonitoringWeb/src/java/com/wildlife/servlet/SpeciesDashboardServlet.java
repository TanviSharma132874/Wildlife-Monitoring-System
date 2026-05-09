package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.io.File;

public class SpeciesDashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String speciesParam = request.getParameter("species_id");

        if (speciesParam == null) {
            response.sendError(400, "Species ID required");
            return;
        }

        int speciesId = Integer.parseInt(speciesParam);

        List<Map<String,Object>> detections = new ArrayList<>();
        List<Map<String,Object>> missingAnimals = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            // ================= DETECTIONS =================
            String sql =
                "SELECT d.*, a.animal_name, c.location " +
                "FROM detections d " +
                "LEFT JOIN animals a ON d.animal_id = a.animal_id " +
                "LEFT JOIN cameras c ON d.camera_id = c.id " +
                "WHERE d.species_id = ? " +
                "ORDER BY d.detected_at DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, speciesId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String,Object> row = new HashMap<>();

                String image = rs.getString("image_path");
                String fileName = (image != null) ? new File(image).getName() : null;

                row.put("id", rs.getInt("detection_id"));
                row.put("animal", rs.getString("animal_name"));
                row.put("confidence", rs.getFloat("confidence"));
                row.put("level", rs.getString("alert_level"));
                row.put("image", fileName);
                row.put("time", rs.getTimestamp("detected_at"));
                row.put("location", rs.getString("location"));

                detections.add(row);
            }

            // ================= MISSING ANIMALS =================
            String missingSQL =
                "SELECT * FROM animals " +
                "WHERE species_id = ? " +
                "AND (last_seen_time IS NULL OR last_seen_time < NOW() - INTERVAL 3 HOUR)";

            PreparedStatement ps2 = conn.prepareStatement(missingSQL);
            ps2.setInt(1, speciesId);

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                Map<String,Object> m = new HashMap<>();

                m.put("id", rs2.getInt("animal_id"));
                m.put("name", rs2.getString("animal_name"));
                m.put("lastSeen", rs2.getTimestamp("last_seen_time"));
                m.put("location", rs2.getString("last_seen_location"));

                missingAnimals.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("detections", detections);
        request.setAttribute("missingAnimals", missingAnimals);

        request.getRequestDispatcher("species_dashboard.jsp")
               .forward(request, response);
    }
}