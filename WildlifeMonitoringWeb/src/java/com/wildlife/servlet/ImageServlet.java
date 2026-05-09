package com.wildlife.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ImageServlet extends HttpServlet {

    private static final String BASE_PATH = "C:/wildlife_storage/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        if (name == null || name.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image name missing");
            return;
        }

        // 🔒 Security
        name = name.replace("..", "").replace("\\", "/");

        File file = new File(BASE_PATH, name);

        System.out.println("📷 Serving: " + file.getAbsolutePath());

        if (!file.exists() || file.isDirectory()) {
            System.out.println("❌ NOT FOUND: " + file.getAbsolutePath());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) mime = "image/jpeg";

        response.setContentType(mime);
        response.setContentLengthLong(file.length());

        // 🚀 Performance
        response.setHeader("Cache-Control", "public, max-age=86400");

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}