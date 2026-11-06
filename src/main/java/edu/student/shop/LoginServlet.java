package edu.student.shop;
import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import javax.servlet.*; import java.io.*; import java.sql.*; import org.mindrot.jbcrypt.BCrypt;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  try(Connection c=DB.get();PreparedStatement s=c.prepareStatement("SELECT user_id,full_name,password_hash,role FROM users WHERE email=?")){
   s.setString(1,q.getParameter("email").trim().toLowerCase());
   try(ResultSet r=s.executeQuery()){if(r.next()&&BCrypt.checkpw(q.getParameter("password"),r.getString("password_hash"))){
    HttpSession h=q.getSession(true);h.setAttribute("userId",r.getInt("user_id"));h.setAttribute("fullName",r.getString("full_name"));h.setAttribute("role",r.getString("role"));p.sendRedirect("products");return;}}
   p.sendRedirect("login.jsp?error=1");
  }catch(SQLException x){throw new ServletException(x);}
 }
}
