package uts.isd.Controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import uts.isd.model.DAO.DAO;

@WebListener
public class StartupListener implements ServletContextListener {
    private DAO dao;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server Started");
        try {
            dao = new DAO();
            ServletContext ctx = sce.getServletContext();
            ctx.setAttribute("dao", dao);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DAO", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Server Stopped");
        if (dao != null) {
            try { dao.close(); }
            catch (Exception ignored) {}
        }
    }
}
