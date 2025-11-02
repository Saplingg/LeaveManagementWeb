/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import model.Feature;
import model.Role;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy URL người dùng đang truy cập
        String requestURL = req.getServletPath(); // Ví dụ: /request/create

        // Lấy tất cả các features user có quyền
        Set<String> userFeatures = (Set<String>) session.getAttribute("userFeatures");

        // Kiểm tra quyền
        boolean hasPermission = false;
        if (userFeatures != null) {
             for (String featureUrl : userFeatures) {
                // Kiểm tra xem URL request có BẮT ĐẦU BẰNG 
                // URL của feature không (ví dụ: /request/list bắt đầu bằng /request/list)
                if (requestURL.equals(featureUrl) || requestURL.startsWith(featureUrl + "/")) {
                    hasPermission = true;
                    break;
                }
            }
        }
       
        // Trường hợp đặc biệt: /request/ (trang gốc) thì cho qua (thường sẽ redirect)
        if(requestURL.equals("/request/") || requestURL.equals("/request")) {
            hasPermission = true;
        }


        if (hasPermission) {
            chain.doFilter(request, response); // Có quyền, cho đi tiếp
        } else {
            // Không có quyền, trả về trang lỗi Access Denied
            res.setContentType("text/html; charset=UTF-8");
            res.getWriter().println("<html><head><title>Lỗi</title><link rel=\"stylesheet\" href=\""+ req.getContextPath() +"/css/style.css\"></head><body>");
            res.getWriter().println("<div class='container login-container'>");
            res.getWriter().println("<h1>Access Denied (Truy cập bị từ chối)</h1>");
            res.getWriter().println("<p>Bạn không có quyền truy cập chức năng này.</p>");
            res.getWriter().println("<a href='" + req.getContextPath() + "/request/list' class='btn btn-primary'>Quay về trang chính</a>");
            res.getWriter().println("</div></body></html>");
        }
    }
    // ... init() and destroy() ...

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void destroy() {
        Filter.super.destroy(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
}
