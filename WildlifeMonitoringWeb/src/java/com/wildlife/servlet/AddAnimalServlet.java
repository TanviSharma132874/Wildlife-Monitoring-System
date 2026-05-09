package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import org.json.JSONObject;

public class AddAnimalServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try(Connection con = DBConnection.getConnection()){

            // =========================
            // 🔥 SUPPORT BOTH FORM + JSON
            // =========================
            String contentType = request.getContentType();

            String name = null;
            int speciesId = 0;
            String detectionParam = null;
            String embedding = null;

            if(contentType != null && contentType.contains("application/json")) {

                // ===== JSON REQUEST (FROM PYTHON)
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = request.getReader();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject json = new JSONObject(sb.toString());

                speciesId = json.getInt("species_id");
                embedding = json.getJSONArray("embedding").toString();

            } else {

                // ===== FORM REQUEST (FROM UI)
                name = request.getParameter("animal_name");
                speciesId = Integer.parseInt(request.getParameter("species_id"));
                detectionParam = request.getParameter("detection_id");
            }

            int animalId;

            // =========================
            // 🔥 CASE 1: PYTHON AUTO CREATE
            // =========================
            if(embedding != null){

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO animals(species_id, embedding, last_seen_time) VALUES(?,?,NOW())",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setInt(1, speciesId);
                ps.setString(2, embedding);

                ps.executeUpdate();

                ResultSet key = ps.getGeneratedKeys();
                key.next();
                animalId = key.getInt(1);

                response.getWriter().print(animalId);
                return;
            }

            // =========================
            // 🔥 CASE 2: MANUAL REGISTER (UI)
            // =========================
            PreparedStatement check = con.prepareStatement(
                "SELECT animal_id FROM animals WHERE animal_name=?"
            );
            check.setString(1, name);

            ResultSet rs = check.executeQuery();

            if(rs.next()){
                animalId = rs.getInt("animal_id"); // reuse existing
            } else {

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO animals(animal_name,species_id,last_seen_time) VALUES(?,?,NOW())",
                    Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1,name);
                ps.setInt(2,speciesId);

                ps.executeUpdate();

                ResultSet key = ps.getGeneratedKeys();
                key.next();
                animalId = key.getInt(1);
            }

            // =========================
            // 🔥 LINK DETECTION
            // =========================
            if(detectionParam != null){

                PreparedStatement update = con.prepareStatement(
                    "UPDATE detections SET animal_id=? WHERE detection_id=?"
                );

                update.setInt(1,animalId);
                update.setInt(2,Integer.parseInt(detectionParam));
                update.executeUpdate();
            }

            response.sendRedirect("DetectionServlet");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}