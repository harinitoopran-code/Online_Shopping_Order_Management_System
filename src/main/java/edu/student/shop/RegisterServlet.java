package edu.student.shop;
import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import javax.servlet.*; import java.io.*; import java.sql.*; import org.mindrot.jbcrypt.BCrypt;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
  String n=q.getParameter("name"),e=q.getParameter("email"),pw=q.getParameter("password");
  if(n==null||e==null||pw==null||n.trim().isEmpty()||!e.contains("@")||pw.length()<8){p.sendRedirect("register.jsp?error=invalid");return;}
  try(Connection c=DB.get();PreparedStatement s=c.prepareStatement("INSERT INTO users(full_name,email,password_hash) VALUES(?,?,?)")){
   s.setString(1,n.trim());s.setString(2,e.trim().toLowerCase());s.setString(3,BCrypt.hashpw(pw,BCrypt.gensalt()));s.executeUpdate();p.sendRedirect("login.jsp?registered=1");
  }catch(SQLException x){p.sendRedirect("register.jsp?error=duplicate");}
 }
}
