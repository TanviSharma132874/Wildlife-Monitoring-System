package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import org.json.JSONObject;

public class DetectionServlet extends HttpServlet {

    // =========================
    // DASHBOARD VIEW (NO CHANGE)
    // =========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String,Object>> detections = new ArrayList<>();
        List<Map<String,Object>> recent = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT d.*, a.animal_name, s.species_name " +
                "FROM detections d " +
                "LEFT JOIN animals a ON d.animal_id = a.animal_id " +
                "JOIN species s ON d.species_id = s.species_id " +
                "ORDER BY d.detected_at DESC"
            );

            ResultSet rs = ps.executeQuery();

            int count = 0;

            while(rs.next()){

                Map<String,Object> m = new HashMap<>();

                m.put("detectionId", rs.getInt("detection_id"));
                m.put("camera", rs.getInt("camera_id"));
                m.put("location", rs.getString("location"));
                m.put("level", rs.getString("alert_level"));
                m.put("confidence", rs.getDouble("confidence"));
                m.put("time", rs.getTimestamp("detected_at"));

                String img = rs.getString("image_path");
                if(img != null && !img.startsWith("detections/")){
                    img = "detections/" + img;
                }
                m.put("image", img);

                m.put("animal", rs.getString("animal_name"));
                m.put("species", rs.getString("species_name"));

                detections.add(m);

                if(count < 6){
                    recent.add(m);
                    count++;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        request.setAttribute("detections", detections);
        request.setAttribute("recentDetections", recent);

        request.getRequestDispatcher("view_detections.jsp")
               .forward(request, response);
    }

    // =========================
    // 🔥 API (UPDATED WITH MULTI-EMBEDDING)
    // =========================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = DBConnection.getConnection()) {

            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());

            int cameraId = json.getInt("camera_id");
            int speciesId = json.getInt("species_id");
            double confidence = json.getDouble("confidence");
            String imageName = json.getString("image_name");
            String location = json.getString("location");

            // =========================
            // 🔥 EMBEDDING (SAFE)
            // =========================
            String embedding = null;
            if(json.has("embedding") && !json.isNull("embedding")){
                embedding = json.getJSONArray("embedding").toString();
            }

            Integer animalId = json.has("animal_id") ? json.getInt("animal_id") : null;

            // =========================
            // ALERT LEVEL
            // =========================
            String level = "LOW";
            if (confidence >= 0.7) level = "HIGH";
            else if (confidence >= 0.4) level = "MEDIUM";

            PreparedStatement ps;

            // =========================
            // INSERT DETECTION
            // =========================
            if(animalId != null){

                ps = con.prepareStatement(
                    "INSERT INTO detections (camera_id,species_id,animal_id,confidence,image_path,location,alert_level,detected_at) " +
                    "VALUES (?,?,?,?,?,?,?,NOW())"
                );

                ps.setInt(1, cameraId);
                ps.setInt(2, speciesId);
                ps.setInt(3, animalId);
                ps.setDouble(4, confidence);
                ps.setString(5, "detections/" + imageName);
                ps.setString(6, location);
                ps.setString(7, level);

                // ✅ UPDATE LAST SEEN
                PreparedStatement upd = con.prepareStatement(
                    "UPDATE animals SET last_seen_time=NOW(), last_seen_location=? WHERE animal_id=?"
                );
                upd.setString(1, location);
                upd.setInt(2, animalId);
                upd.executeUpdate();

            } else {

                ps = con.prepareStatement(
                    "INSERT INTO detections (camera_id,species_id,confidence,image_path,location,alert_level,detected_at) " +
                    "VALUES (?,?,?,?,?,?,NOW())"
                );

                ps.setInt(1, cameraId);
                ps.setInt(2, speciesId);
                ps.setDouble(3, confidence);
                ps.setString(4, "detections/" + imageName);
                ps.setString(5, location);
                ps.setString(6, level);
            }

            ps.executeUpdate();

            // =========================
            // 🔥 MULTI-EMBEDDING STORE (NEW)
            // =========================
            if (animalId != null && embedding != null) {

                try {
                    PreparedStatement embPs = con.prepareStatement(
                        "INSERT INTO animal_embeddings(animal_id, embedding) VALUES (?, ?)"
                    );

                    embPs.setInt(1, animalId);
                    embPs.setString(2, embedding);
                    embPs.executeUpdate();

                    System.out.println("✅ Embedding stored for animal: " + animalId);

                } catch (Exception e) {
                    System.out.println("⚠ Embedding insert failed");
                }
            }

            response.getWriter().print("OK");

        } catch(Exception e){
            e.printStackTrace();
            response.getWriter().print("ERROR");
        }
    }
}