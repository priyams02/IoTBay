package uts.isd.model.DAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import uts.isd.Controller.CheckoutServlet;
import uts.isd.Controller.EditPaymentDetailsServlet;
import uts.isd.Controller.DeletePaymentDetailsServlet;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.DBConnector;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
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
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        Customer existingCustomer = mock(Customer.class);

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("addNum")).thenReturn("12");
        when(request.getParameter("addStreetName")).thenReturn("Main");
        when(request.getParameter("addSuburb")).thenReturn("Central");
        when(request.getParameter("addPostcode")).thenReturn("2000");
        when(request.getParameter("addCity")).thenReturn("Sydney");
        when(request.getParameter("CardNo")).thenReturn("1234567890123456");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("John Doe");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(dao.customers()).thenReturn(customerDB);

        when(customerDB.findCustomer("user@example.com")).thenReturn(existingCustomer);
        when(existingCustomer.getFirstName()).thenReturn("John");
        when(existingCustomer.getLastName()).thenReturn("Doe");
        when(existingCustomer.getPassword()).thenReturn("password");
        when(existingCustomer.getEmail()).thenReturn("user@example.com");
        when(existingCustomer.getPhoneNumber()).thenReturn("123456789");

        new EditPaymentDetailsServlet().doPost(request, response);

        verify(customerDB).update(eq(existingCustomer), any(Customer.class));
    }
    
    @Test
    public void testInvalidCheckout() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("addNum")).thenReturn("12");
        when(request.getParameter("addStreetName")).thenReturn("Main");
        when(request.getParameter("addSuburb")).thenReturn("Central");
        when(request.getParameter("addPostcode")).thenReturn("2000");
        when(request.getParameter("addCity")).thenReturn("Sydney");
        when(request.getParameter("CardNo")).thenReturn("ABC123");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("John Doe");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(dao.customers()).thenReturn(customerDB);

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        new EditPaymentDetailsServlet().doPost(request, response);

        verify(customerDB, never()).update(any(), any());
    }


    @Test
    public void testValidEditPayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        Customer existingCustomer = mock(Customer.class);

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("addNum")).thenReturn("12");
        when(request.getParameter("addStreetName")).thenReturn("Main");
        when(request.getParameter("addSuburb")).thenReturn("Central");
        when(request.getParameter("addPostcode")).thenReturn("2000");
        when(request.getParameter("addCity")).thenReturn("Sydney");
        when(request.getParameter("CardNo")).thenReturn("1234567890123456");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("John Doe");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(dao.customers()).thenReturn(customerDB);
        when(customerDB.findCustomer("user@example.com")).thenReturn(existingCustomer);

        when(existingCustomer.getFirstName()).thenReturn("John");
        when(existingCustomer.getLastName()).thenReturn("Doe");
        when(existingCustomer.getPassword()).thenReturn("password");
        when(existingCustomer.getEmail()).thenReturn("user@example.com");
        when(existingCustomer.getPhoneNumber()).thenReturn("123456789");

        new EditPaymentDetailsServlet().doPost(request, response);
        verify(customerDB).update(eq(existingCustomer), any(Customer.class));
    }

    @Test
    public void testInvalidEditPayments() throws ServletException, IOException, SQLException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("addNum")).thenReturn("12");
        when(request.getParameter("addStreetName")).thenReturn("Main");
        when(request.getParameter("addSuburb")).thenReturn("Central");
        when(request.getParameter("addPostcode")).thenReturn("2000");
        when(request.getParameter("addCity")).thenReturn("Sydney");
        when(request.getParameter("CardNo")).thenReturn("ABC123456789");
        when(request.getParameter("CVV")).thenReturn("123");
        when(request.getParameter("CardHolder")).thenReturn("John Doe");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(dao.customers()).thenReturn(customerDB);

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        new EditPaymentDetailsServlet().doPost(request, response);
        verify(customerDB, never()).update(any(), any());
    }

    @Test
    public void testValidDeletePayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        Customer existingCustomer = mock(Customer.class);

        String email = "test@example.com";
        Address address = new Address("123", "StreetName", "SuburbName", "2000", "Sydney");

        DeletePaymentDetailsServlet servlet = new DeletePaymentDetailsServlet();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(request.getAttribute("Email")).thenReturn(email);
        when(dao.customers()).thenReturn(customerDB);
        when(customerDB.findCustomer(email)).thenReturn(existingCustomer);

        when(existingCustomer.getFirstName()).thenReturn("John");
        when(existingCustomer.getLastName()).thenReturn("Doe");
        when(existingCustomer.getPassword()).thenReturn("secret");
        when(existingCustomer.getEmail()).thenReturn(email);
        when(existingCustomer.getAddress()).thenReturn(address);
        when(existingCustomer.getPhoneNumber()).thenReturn("0412345678");

        servlet.doPost(request, response);

        verify(session).setAttribute("cardNo", null);
        verify(session).setAttribute("CVV", null);
        verify(session).setAttribute("cardHolder", null);

        ArgumentCaptor<Customer> updatedCustomerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDB).update(eq(existingCustomer), updatedCustomerCaptor.capture());

        Customer updatedCustomer = updatedCustomerCaptor.getValue();
        assertNull(updatedCustomer.getPaymentInfo());
    }

    @Test
    public void testInvalidDeletePayments() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);

        String email = "test@example.com";

        DeletePaymentDetailsServlet servlet = new DeletePaymentDetailsServlet();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("dao")).thenReturn(dao);
        when(request.getAttribute("Email")).thenReturn(email);
        when(dao.customers()).thenReturn(customerDB);

        when(customerDB.findCustomer(email)).thenThrow(new SQLException("Database error"));

        ServletException thrown = assertThrows(ServletException.class, () -> {
            servlet.doPost(request, response);
        });

        assertTrue(thrown.getMessage().contains("Delete failed"));
        assertTrue(thrown.getCause() instanceof SQLException);
    }
}