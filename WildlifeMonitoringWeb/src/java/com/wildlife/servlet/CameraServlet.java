package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class CameraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Prevent 405 error
        response.sendRedirect("add_camera.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String location = request.getParameter("location");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO cameras(location, latitude, longitude) VALUES (?, ?, ?)"
            );

            ps.setString(1, location);
            ps.setString(2, latitude);
            ps.setString(3, longitude);

            ps.executeUpdate();

            response.sendRedirect("view_cameras.jsp");

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}