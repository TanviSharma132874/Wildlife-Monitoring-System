package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AnimalProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("animal_id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.getWriter().println("❌ Animal ID missing");
            return;
        }

        int animalId = Integer.parseInt(idParam);

        try (Connection con = DBConnection.getConnection()) {

            Map<String,Object> animal = new HashMap<>();
            List<Map<String,Object>> history = new ArrayList<>();
            List<Double> confidenceList = new ArrayList<>();

            Timestamp lastSeen = null;
            String lastLocation = null;

            // =========================
            // 🔥 ANIMAL DETAILS
            // =========================
            PreparedStatement ps = con.prepareStatement(
                "SELECT a.*, s.species_name FROM animals a " +
                "JOIN species s ON a.species_id=s.species_id " +
                "WHERE a.animal_id=?"
            );
            ps.setInt(1, animalId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                animal.put("id", rs.getInt("animal_id"));
                animal.put("name", rs.getString("animal_name"));
                animal.put("species", rs.getString("species_name"));
                animal.put("gender", rs.getString("gender"));
                animal.put("age", rs.getInt("age"));
            }

            // =========================
            // 🔥 DETECTION HISTORY
            // =========================
            PreparedStatement ps2 = con.prepareStatement(
                "SELECT d.*, c.location FROM detections d " +
                "LEFT JOIN cameras c ON d.camera_id=c.id " +
                "WHERE d.animal_id=? ORDER BY d.detected_at DESC"
            );
            ps2.setInt(1, animalId);

            ResultSet rs2 = ps2.executeQuery();

            while(rs2.next()){

                Map<String,Object> m = new HashMap<>();

                m.put("confidence", rs2.getDouble("confidence"));
                m.put("time", rs2.getTimestamp("detected_at"));
                m.put("location", rs2.getString("location"));

                String img = rs2.getString("image_path");
                if(img != null && !img.startsWith("detections/")){
                    img = "detections/" + img;
                }
                m.put("image", img);

                history.add(m);
                confidenceList.add(rs2.getDouble("confidence"));

                if(lastSeen == null){
                    lastSeen = rs2.getTimestamp("detected_at");
                    lastLocation = rs2.getString("location");
                }
            }

            animal.put("last_seen", lastSeen);
            animal.put("location", lastLocation);

            // =========================
            // 🔥 STATUS LOGIC (FINAL)
            // =========================
            String status;

            if(lastSeen == null){
                status = "NO DATA";
            } else {

                long diff = System.currentTimeMillis() - lastSeen.getTime();

                if(diff > 3 * 60 * 60 * 1000){
                    status = "MISSING";

                    // 🔥 IMPORTANT: remove graph
                    confidenceList.clear();

                } else {

                    double avg = confidenceList.stream()
                            .mapToDouble(d -> d)
                            .average()
                            .orElse(0);

                    if(avg < 0.5){
                        status = "LOW ACTIVITY";
                    } else {
                        status = "ACTIVE";
                    }
                }
            }

            request.setAttribute("animal", animal);
            request.setAttribute("history", history);
            request.setAttribute("confidenceList", confidenceList);
            request.setAttribute("status", status);

            request.getRequestDispatcher("animal_profile.jsp")
                   .forward(request, response);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}