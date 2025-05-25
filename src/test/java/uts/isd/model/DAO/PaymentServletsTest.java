package uts.isd.model.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.mockito.Mockito;
import uts.isd.Controller.CheckoutServlet;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.DBConnector;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class PaymentServletsTest {

    @Test
    public void testValidCheckout() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        DAO dao = new DAO();

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("db")).thenReturn(dao);
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");

        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser").getPaymentInformation(), any());
    }
    
    @Test
    public void testInvalidCheckout() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");


        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser").getPaymentInformation(), eq(null));
    }


}