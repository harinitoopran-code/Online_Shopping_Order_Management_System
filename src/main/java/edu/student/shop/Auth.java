package edu.student.shop;
import javax.servlet.http.*;
public class Auth {
 public static Integer userId(HttpServletRequest r){ Object x=r.getSession(false)==null?null:r.getSession(false).getAttribute("userId"); return x instanceof Integer?(Integer)x:null; }
 public static boolean admin(HttpServletRequest r){ return r.getSession(false)!=null && "ADMIN".equals(r.getSession(false).getAttribute("role")); }
}
