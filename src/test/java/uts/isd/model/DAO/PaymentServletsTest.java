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
import uts.isd.model.Person.User;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;
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

        verify(session).setAttribute(eq("loggedInUser"), any());
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

        verify(session).setAttribute(eq("loggedInUser"), eq(null));
    }


    @Test
    public void testValidEditPayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        User user = (User) session.getAttribute("loggedInUser");
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");

        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser"), any());
        User newUser = (User) session.getAttribute("loggedInUser");
        assertNotEquals(user.getPaymentInfo().getCardNo(), newUser.getPaymentInfo().getCardNo());
        assertNotEquals(user.getPaymentInfo().getCVV(), newUser.getPaymentInfo().getCVV());
        assertNotEquals(user.getPaymentInfo().getCardHolder(), newUser.getPaymentInfo().getCardHolder());
    }

    @Test
    public void testInvalidEditPayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        User user = (User) session.getAttribute("loggedInUser");
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");

        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser"), any());
        User newUser = (User) session.getAttribute("loggedInUser");
        assertEquals(user.getPaymentInfo().getCardNo(), newUser.getPaymentInfo().getCardNo());
        assertEquals(user.getPaymentInfo().getCVV(), newUser.getPaymentInfo().getCVV());
        assertEquals(user.getPaymentInfo().getCardHolder(), newUser.getPaymentInfo().getCardHolder());
    }

    @Test
    public void testValidDeletePayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");

        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser"), any());
        User newUser = (User) session.getAttribute("loggedInUser");
        assertNull(newUser.getPaymentInfo().getCardNo());
        assertNull(newUser.getPaymentInfo().getCVV());
        assertNull(newUser.getPaymentInfo().getCardHolder());
    }

    @Test
    public void testInvalidDeletePayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("cardNo")).thenReturn("4501266849504");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("David Lee");

        CheckoutServlet servlet = new CheckoutServlet();
        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser"), any());
        User newUser = (User) session.getAttribute("loggedInUser");
        assertNotNull(newUser.getPaymentInfo().getCardNo());
        assertNotNull(newUser.getPaymentInfo().getCVV());
        assertNotNull(newUser.getPaymentInfo().getCardHolder());
    }
}