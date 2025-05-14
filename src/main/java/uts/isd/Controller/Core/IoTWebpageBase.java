package uts.isd.Controller.Core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uts.isd.model.DAO.DBConnector;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The base class for any Java-controlled webpage in IoTBay.
 *
 * Refactored to use init/destroy lifecycle methods for DB setup and teardown.
 * Session attributes are set at init and shared via ServletContext.
 */
public abstract class IoTWebpageBase extends HttpServlet implements IoTWebpage {
    public static final String CSS_LINK = "<link rel=\"stylesheet\" href=\"IoTCore/IoTBayStyles.css\">";

    protected static DBConnector connector;
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize SQLite connector and DB manager
        connector = new DBConnector();
        System.out.println("IoTWebpageBase: Database initialized.");
    }

    @Override
    public void destroy() {
        super.destroy();
        // Close DB connection
        if (connector != null) {
            connector.closeConnection();
            System.out.println("IoTWebpageBase: Database connection closed.");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        // Common pre-processing can go here
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        // Common pre-processing can go here
    }

    /**
     * Utility to build URL parameters for redirects.
     */
    public final String redirectParams(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Input redirection params must have an even length: " + params.length);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            result.append(params[i]).append("=").append(params[i + 1]);
            if (i + 2 < params.length) result.append("&");
        }
        return result.toString();
    }

    /**
     * Creates an HTML tag with content and attributes.
     */
    public static  String makeTag(String tag, String content, String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Attributes must be in name/value pairs: " + params.length);
        }
        StringBuilder sb = new StringBuilder("<").append(tag);
        for (int i = 0; i < params.length; i += 2) {
            sb.append(" ").append(params[i]).append("=\"").append(params[i + 1]).append("\"");
        }
        sb.append(">").append(content).append("</").append(tag).append(">");
        return sb.toString();
    }

    /**
     * Creates an HTML tag without a closing tag.
     */
    public static  String makeTagNoClose(String tag, String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Attributes must be in name/value pairs: " + params.length);
        }
        StringBuilder sb = new StringBuilder("<").append(tag);
        for (int i = 0; i < params.length; i += 2) {
            sb.append(" ").append(params[i]).append("=\"").append(params[i + 1]).append("\"");
        }
        sb.append(">");
        return sb.toString();
    }
}
