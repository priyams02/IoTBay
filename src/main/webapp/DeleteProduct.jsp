<%@ page import="uts.isd.model.DAO.DAO" %>
<%@ page import="uts.isd.model.DAO.ProductDBManager" %>
<%@ page import="uts.isd.model.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String id  = request.getParameter("id");
    if (id == null || id.isEmpty()) {
        response.sendRedirect(ctx + "/ListProducts?err=Missing+ID");
        return;
    }

    // Load the DAO from session (must have been set at login or lazily)
    DAO dao = (DAO) session.getAttribute("dao");
    if (dao == null) {
        dao = new DAO();               // fallback if you prefer
        session.setAttribute("dao", dao);
    }

    ProductDBManager mgr = dao.products();
    Product p;
    try {
        p = mgr.findProduct(id);
    } catch (Exception e) {
        response.sendRedirect(ctx + "/ListProducts?err=Error+loading+product");
        return;
    }
    if (p == null) {
        response.sendRedirect(ctx + "/ListProducts?err=Product+not+found");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Delete Product – <%= p.getName() %></title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { width: 360px; margin: 60px auto; text-align: center; }
        .btn { padding: 8px 16px; margin: 0 8px; border: none; border-radius: 4px; color: #fff; }
        .btn-danger { background: #dc3545; }
        .btn-cancel { background: #6c757d; }
        .btn a { color: #fff; text-decoration: none; }
    </style>
</head>
<body>
<div class="container">
    <h2>Confirm Deletion</h2>
    <p>Are you sure you want to delete the product:</p>
    <p><strong><%= p.getName() %> (ID: <%= p.getProductId() %>)</strong>?</p>

    <form method="post" action="<%= ctx %>/DeleteProduct">
        <!-- Pass the real ProductID on POST -->
        <input type="hidden" name="ProductID" value="<%= p.getProductId() %>"/>
        <button type="submit" class="btn btn-danger">Yes, Delete</button>
        <button type="button" class="btn btn-cancel"
                onclick="window.location.href='<%= ctx %>/ListProducts';">
            Cancel
        </button>
    </form>
</div>
</body>
</html>
