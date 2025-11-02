/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import model.Employee;
import model.Feature;
import model.Role;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LoginController extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        User loggedInUser = userDAO.getUserByUsernameAndPassword(user, pass);

        if (loggedInUser != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", loggedInUser);

            // Lấy Employee tương ứng
            Employee employee = userDAO.getEmployeeByUserId(loggedInUser.getUid());
            session.setAttribute("employee", employee);

            // Ghi nhớ các quyền (features) của user vào session
            Set<String> userFeatures = new HashSet<>();
            for (Role role : loggedInUser.getRoles()) {
                for (Feature feature : role.getFeatures()) {
                    userFeatures.add(feature.getUrl());
                }
            }
            session.setAttribute("userFeatures", userFeatures);
            session.setAttribute("userRoles", loggedInUser.getRoles());

            // Chuyển đến trang danh sách
            resp.sendRedirect(req.getContextPath() + "/request/list");
        } else {
            // Đăng nhập thất bại
            req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Nếu cố tình vào /login qua GET, cho về trang login
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
