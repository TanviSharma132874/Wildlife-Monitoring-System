package com.wildlife.servlet;

import com.wildlife.dao.DBConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AnimalListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int speciesId = Integer.parseInt(request.getParameter("species_id"));

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM animals WHERE species_id=?"
            );
            ps.setInt(1, speciesId);

            ResultSet rs = ps.executeQuery();

            List<Map<String,Object>> list = new ArrayList<>();

            while(rs.next()){
                Map<String,Object> m = new HashMap<>();
                m.put("id", rs.getInt("animal_id"));
                m.put("name", rs.getString("animal_name"));
                m.put("gender", rs.getString("gender"));
                m.put("age", rs.getInt("age"));
                list.add(m);
            }

            request.setAttribute("animals", list);

            request.getRequestDispatcher("animal_list.jsp")
                   .forward(request, response);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}