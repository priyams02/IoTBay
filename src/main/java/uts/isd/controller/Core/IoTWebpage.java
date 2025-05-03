package uts.isd.controller.Core;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IoTWebpage {
    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}