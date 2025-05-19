import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import org.junit.Test;
import uts.isd.Controller.*;
import uts.isd.model.AccessLog;
import uts.isd.model.Person.*;
import uts.isd.model.DAO.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class ServletTests {

    // --- UpdateProfileServlet ---
    @Test
    public void testCustomerUpdateEmail() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        Customer customer = mock(Customer.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("loggedInUser")).thenReturn(customer);
        when(request.getContextPath()).thenReturn("/app");
        when(request.getParameter("Attribute")).thenReturn("Email");
        when(request.getParameter("Email")).thenReturn("updated@example.com");
        when(request.getParameter("bIsCustomer")).thenReturn("yes");

        when(dao.customers()).thenReturn(customerDB);
        doNothing().when(customerDB).update(any(), any());

        UpdateProfileServlet servlet = new UpdateProfileServlet() {
            @Override
            protected DAO createDAO() { return dao; }
        };

        servlet.doPost(request, response);

        verify(customer).setEmail("updated@example.com");
        verify(session).setAttribute("loggedInUser", customer);
        verify(response).sendRedirect(contains("/Profile.jsp?upd="));
    }

    // --- ViewProfileServlet ---
    @Test
    public void testViewProfileLoadsAccessLogs() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        DAO dao = mock(DAO.class);
        User user = mock(User.class);
        List<AccessLog> mockLogs = List.of(mock(AccessLog.class));

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("loggedInUser")).thenReturn(user);
        when(user.getEmail()).thenReturn("test@example.com");
        when(request.getRequestDispatcher("/ViewProfile.jsp")).thenReturn(dispatcher);
        when(dao.AccessLogs().findByEmail("test@example.com")).thenReturn(mockLogs);

        ViewProfileServlet servlet = new ViewProfileServlet() {
            private DAO createDAO() { return dao; }
        };

        servlet.doGet(request, response);
        verify(request).setAttribute("logs", mockLogs);
        verify(dispatcher).forward(request, response);
    }

    // --- RegisterServlet ---
    @Test
    public void testRegisterSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);

        when(request.getParameter("First")).thenReturn("Alice");
        when(request.getParameter("Last")).thenReturn("Smith");
        when(request.getParameter("Email")).thenReturn("alice@example.com");
        when(request.getParameter("PhoneNumber")).thenReturn("0412345678");
        when(request.getParameter("Pass1")).thenReturn("pass");
        when(request.getParameter("Pass2")).thenReturn("pass");
        when(request.getParameter("addNum")).thenReturn("123");
        when(request.getParameter("addStreetName")).thenReturn("Main");
        when(request.getParameter("addSuburb")).thenReturn("Suburbia");
        when(request.getParameter("addPostcode")).thenReturn("2000");
        when(request.getParameter("addCity")).thenReturn("Sydney");
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/app");

        when(dao.customers()).thenReturn(customerDB);
        when(customerDB.findCustomer("alice@example.com")).thenReturn(null);
        doNothing().when(customerDB).add(any());

        RegisterServlet servlet = new RegisterServlet() {
            private DAO createDAO() { return dao; }
        };

        servlet.doPost(request, response);

        verify(session).setAttribute(eq("loggedInUser"), any());
        verify(response).sendRedirect(contains("/Redirector.jsp"));
    }

    // --- LoginServlet ---
    @Test
    public void testLoginAsCustomerSuccess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        CustomerDBManager customerDB = mock(CustomerDBManager.class);
        Customer customer = mock(Customer.class);

        when(request.getParameter("Email")).thenReturn("customer@example.com");
        when(request.getParameter("Password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/app");
        when(customer.getPassword()).thenReturn("password");

        when(dao.customers()).thenReturn(customerDB);
        when(customerDB.findCustomer("customer@example.com")).thenReturn(customer);

        LoginServlet servlet = new LoginServlet() {
            private DAO createDAO() { return dao; }
        };

        servlet.doPost(request, response);

        verify(session).setAttribute("loggedInUser", customer);
        verify(response).sendRedirect(contains("/Redirector.jsp"));
    }

    // --- DeleteServlet ---
    @Test
    public void testDeleteCustomerAccount() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        DAO dao = mock(DAO.class);
        Customer customer = mock(Customer.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("User")).thenReturn(customer);
        when(customer.getType()).thenReturn(User.UserType.CUSTOMER);

        doNothing().when(dao.customers()).delete(customer);

        DeleteServlet servlet = new DeleteServlet() {
            private DAO createDAO() { return dao; }
        };

        servlet.init();
        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("index.jsp");
    }

    // --- LogoutServlet ---
    @Test
    public void testLogoutViaPost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/app");

        LogoutServlet servlet = new LogoutServlet();
        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/app/index.jsp");
    }
}
