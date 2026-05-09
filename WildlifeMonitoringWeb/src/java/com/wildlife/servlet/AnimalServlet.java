package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AnimalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Prevent 405 error
        response.sendRedirect("add_animal.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("animal_name");
        String species = request.getParameter("species_id");
        String gender = request.getParameter("gender");
        String ageStr = request.getParameter("age");

        try (Connection conn = DBConnection.getConnection()) {

            // =========================
            // ✅ VALIDATION
            // =========================
            if (name == null || name.trim().isEmpty()) {
                response.getWriter().println("Animal name required");
                return;
            }

            int speciesId;
            int age = 0;

            try {
                speciesId = Integer.parseInt(species);
            } catch (Exception e) {
                response.getWriter().println("Invalid species");
                return;
            }

            try {
                age = Integer.parseInt(ageStr);
            } catch (Exception e) {
                age = 0;
            }

            // =========================
            // ✅ INSERT
            // =========================
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO animals(species_id, animal_name, gender, age) VALUES (?, ?, ?, ?)"
            );

            ps.setInt(1, speciesId);
            ps.setString(2, name);
            ps.setString(3, gender);
            ps.setInt(4, age);

            ps.executeUpdate();

            System.out.println("✅ Manual Animal Added: " + name);

            response.sendRedirect("view_animals.jsp");

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}