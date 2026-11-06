package edu.student.shop;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import javax.servlet.*;import java.io.*;import java.sql.*;import java.util.*;
@WebServlet("/products") public class ProductsServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  String term=q.getParameter("q");if(term==null)term="";
  List<Map<String,Object>> list=new ArrayList<>();
  try(Connection c=DB.get();PreparedStatement s=c.prepareStatement("SELECT * FROM products WHERE active=1 AND (product_name LIKE ? OR category LIKE ?) ORDER BY product_name")){
   s.setString(1,"%"+term+"%");s.setString(2,"%"+term+"%");
   try(ResultSet r=s.executeQuery()){while(r.next()){Map<String,Object> m=new HashMap<>();m.put("id",r.getInt("product_id"));m.put("name",r.getString("product_name"));m.put("category",r.getString("category"));m.put("description",r.getString("description"));m.put("price",r.getBigDecimal("price"));m.put("stock",r.getInt("stock_qty"));list.add(m);}}
   q.setAttribute("products",list);q.getRequestDispatcher("/products.jsp").forward(q,p);
  }catch(SQLException x){throw new ServletException(x);}
 }
}
