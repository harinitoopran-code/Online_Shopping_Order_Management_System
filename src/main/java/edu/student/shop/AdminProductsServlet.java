package edu.student.shop;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import javax.servlet.*;import java.io.*;import java.sql.*;
@WebServlet("/admin/products") public class AdminProductsServlet extends HttpServlet {
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  if(!Auth.admin(q)){p.sendError(403,"Admin access required");return;}
  String name=q.getParameter("name"),cat=q.getParameter("category"),desc=q.getParameter("description");
  try(Connection c=DB.get();PreparedStatement s=c.prepareStatement("INSERT INTO products(product_name,category,description,price,stock_qty) VALUES(?,?,?,?,?)")){
   s.setString(1,name);s.setString(2,cat);s.setString(3,desc);s.setBigDecimal(4,new java.math.BigDecimal(q.getParameter("price")));s.setInt(5,Integer.parseInt(q.getParameter("stock")));s.executeUpdate();p.sendRedirect("../admin.jsp?added=1");
  }catch(SQLException x){throw new ServletException(x);}
 }
}
