package edu.student.shop;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import javax.servlet.*;import java.io.*;import java.sql.*;import java.math.*;
@WebServlet("/checkout") public class CheckoutServlet extends HttpServlet {
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  Integer uid=Auth.userId(q);if(uid==null){p.sendRedirect("login.jsp");return;}
  String address=q.getParameter("address");if(address==null||address.trim().isEmpty()){p.sendRedirect("checkout.jsp?error=address");return;}
  try(Connection c=DB.get()){
   c.setAutoCommit(false);
   try{
    int cart;try(PreparedStatement s=c.prepareStatement("SELECT cart_id FROM carts WHERE user_id=?")){s.setInt(1,uid);try(ResultSet r=s.executeQuery()){if(!r.next())throw new Exception("Cart is empty");cart=r.getInt(1);}}
    BigDecimal total=BigDecimal.ZERO;
    try(PreparedStatement s=c.prepareStatement("SELECT ci.product_id,ci.quantity,p.product_name,p.price,p.stock_qty FROM cart_items ci JOIN products p ON p.product_id=ci.product_id WHERE ci.cart_id=? FOR UPDATE")){s.setInt(1,cart);try(ResultSet r=s.executeQuery()){
     while(r.next()){int qty=r.getInt("quantity");if(qty>r.getInt("stock_qty"))throw new Exception("Insufficient stock for "+r.getString("product_name"));total=total.add(r.getBigDecimal("price").multiply(BigDecimal.valueOf(qty)));}}
    }
    if(total.signum()<=0)throw new Exception("Cart is empty");
    int orderId;try(PreparedStatement s=c.prepareStatement("INSERT INTO orders(user_id,delivery_address,total_amount) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){s.setInt(1,uid);s.setString(2,address);s.setBigDecimal(3,total);s.executeUpdate();try(ResultSet k=s.getGeneratedKeys()){k.next();orderId=k.getInt(1);}}
    try(PreparedStatement s=c.prepareStatement("SELECT ci.product_id,ci.quantity,p.product_name,p.price FROM cart_items ci JOIN products p ON p.product_id=ci.product_id WHERE ci.cart_id=?")){s.setInt(1,cart);try(ResultSet r=s.executeQuery()){while(r.next()){
     int pid=r.getInt(1),qty=r.getInt(2);try(PreparedStatement i=c.prepareStatement("INSERT INTO order_items(order_id,product_id,product_name_snapshot,unit_price,quantity) VALUES(?,?,?,?,?)")){i.setInt(1,orderId);i.setInt(2,pid);i.setString(3,r.getString(3));i.setBigDecimal(4,r.getBigDecimal(4));i.setInt(5,qty);i.executeUpdate();}
     try(PreparedStatement u=c.prepareStatement("UPDATE products SET stock_qty=stock_qty-? WHERE product_id=? AND stock_qty>=?")){u.setInt(1,qty);u.setInt(2,pid);u.setInt(3,qty);if(u.executeUpdate()!=1)throw new Exception("Stock changed; retry checkout");}
    }}}
    try(PreparedStatement d=c.prepareStatement("DELETE FROM cart_items WHERE cart_id=?")){d.setInt(1,cart);d.executeUpdate();}
    c.commit();p.sendRedirect("orders.jsp?placed="+orderId);
   }catch(Exception e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
  }catch(Exception x){throw new ServletException(x);}
 }
}
