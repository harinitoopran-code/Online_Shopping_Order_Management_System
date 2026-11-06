package edu.student.shop;
import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import java.io.*;
@WebServlet("/logout") public class LogoutServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws IOException{if(q.getSession(false)!=null)q.getSession(false).invalidate();p.sendRedirect("login.jsp");}
}
