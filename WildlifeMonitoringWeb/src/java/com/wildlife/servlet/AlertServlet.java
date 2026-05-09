package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AlertServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cameraId = request.getParameter("cameraId");
        String level = request.getParameter("level");
        String message = request.getParameter("message");

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO alerts(camera_id, level, message) VALUES (?, ?, ?)"
            );

            ps.setInt(1, Integer.parseInt(cameraId));
            ps.setString(2, level);
            ps.setString(3, message);

            ps.executeUpdate();

            response.sendRedirect("view_alerts.jsp");

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}