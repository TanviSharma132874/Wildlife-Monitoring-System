package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = DBConnection.getConnection()) {

            Statement st = con.createStatement();

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM animals");
            rs1.next();
            request.setAttribute("animalCount", rs1.getInt(1));

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM cameras");
            rs2.next();
            request.setAttribute("cameraCount", rs2.getInt(1));

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM detections");
            rs3.next();
            request.setAttribute("detectionCount", rs3.getInt(1));

            ResultSet rs4 = st.executeQuery("SELECT COUNT(*) FROM alerts");
            rs4.next();
            request.setAttribute("alertCount", rs4.getInt(1));

            // ✅ CORRECT (COUNT ANIMALS)
            PreparedStatement ps = con.prepareStatement(
                "SELECT s.species_id, s.species_name, COUNT(a.animal_id) as total " +
                "FROM species s " +
                "LEFT JOIN animals a ON s.species_id = a.species_id " +
                "GROUP BY s.species_id, s.species_name"
            );

            ResultSet rs = ps.executeQuery();

            List<Map<String,Object>> species = new ArrayList<>();

            while(rs.next()){
                Map<String,Object> m = new HashMap<>();
                m.put("id", rs.getInt("species_id"));
                m.put("name", rs.getString("species_name"));
                m.put("count", rs.getInt("total"));
                species.add(m);
            }

            request.setAttribute("speciesCounts", species);

            request.getRequestDispatcher("dashboard.jsp")
                   .forward(request, response);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}