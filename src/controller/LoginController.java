package controller;

import db.DataBase;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.dao.GuildDao;
import model.dao.UserDao;

@WebServlet(urlPatterns = "/Login")
public class LoginController extends HttpServlet {

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

    String error = "<div class=\"alert alert-danger\" role=\"alert\">\n"
        + "Account not found, try again or create a new one\n"
        + "</div>";
    String userName = request.getParameter("username");
    String password = request.getParameter("password");
    User user = null;
    try {
      user = new User(userName, DataBase.encryptPassword(password));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    UserDao userDao = new UserDao();

    try {
      if (userDao.getAllUsers().contains(user)) {
        HttpSession session = request.getSession(true);
        session.setAttribute("userName", userName);
        session.setAttribute("userId" , userDao.getUserIdByName(userName));
        response.sendRedirect("index");


      } else {
        request.setAttribute("error", error);
        request.getRequestDispatcher("login.jsp").forward(request, response);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }


  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("login.jsp").forward(request, response);


  }
}
