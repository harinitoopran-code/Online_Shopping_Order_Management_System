package edu.student.shop;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import javax.servlet.*;import java.io.*;import java.sql.*;
@WebServlet("/cart") public class CartServlet extends HttpServlet {
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  Integer uid=Auth.userId(q);if(uid==null){p.sendRedirect("login.jsp");return;}
  try(Connection c=DB.get()){
   int cart;
   try(PreparedStatement s=c.prepareStatement("INSERT INTO carts(user_id) VALUES(?) ON DUPLICATE KEY UPDATE cart_id=LAST_INSERT_ID(cart_id)")){s.setInt(1,uid);s.executeUpdate();try(ResultSet k=s.getGeneratedKeys()){k.next();cart=k.getInt(1);}}
   String action=q.getParameter("action");int pid=Integer.parseInt(q.getParameter("productId"));
   if("add".equals(action)){int qty=Math.max(1,Integer.parseInt(q.getParameter("quantity")==null?"1":q.getParameter("quantity")));
    try(PreparedStatement s=c.prepareStatement("INSERT INTO cart_items(cart_id,product_id,quantity) VALUES(?,?,?) ON DUPLICATE KEY UPDATE quantity=quantity+VALUES(quantity)")){s.setInt(1,cart);s.setInt(2,pid);s.setInt(3,qty);s.executeUpdate();}
   } else if("remove".equals(action)){try(PreparedStatement s=c.prepareStatement("DELETE FROM cart_items WHERE cart_id=? AND product_id=?")){s.setInt(1,cart);s.setInt(2,pid);s.executeUpdate();}}
   else if("update".equals(action)){int qty=Integer.parseInt(q.getParameter("quantity"));if(qty<1)throw new IllegalArgumentException("Quantity must be positive");try(PreparedStatement s=c.prepareStatement("UPDATE cart_items SET quantity=? WHERE cart_id=? AND product_id=?")){s.setInt(1,qty);s.setInt(2,cart);s.setInt(3,pid);s.executeUpdate();}}
   p.sendRedirect("cart.jsp");
  }catch(Exception x){throw new ServletException(x);}
 }
}
