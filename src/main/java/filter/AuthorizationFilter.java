package filter;

import model.User;
import model.Role;
import model.Feature;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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

        String requestURL = req.getServletPath();
        Set<String> userFeatures = (Set<String>) session.getAttribute("userFeatures");

        boolean hasPermission = false;

        // Trường hợp đặc biệt: Cho phép truy cập trang gốc (nếu có)
        // Đây là logic bạn đã thêm, chúng ta sẽ sửa nó để bao quát hơn
        if (requestURL.equals("/request/") || requestURL.equals("/request") ||
            requestURL.equals("/division/") || requestURL.equals("/division")) {
            hasPermission = true;
        }

        // Kiểm tra các quyền cụ thể trong DB
        if (!hasPermission && userFeatures != null) {
            for (String featureUrl : userFeatures) {
                // Kiểm tra xem URL request có BẮT ĐẦU BẰNG URL của feature không
                // Ví dụ: /request/history SẼ KHÔNG KHỚP với /request/list
                // Nhưng /request/list-all SẼ KHỚP với /request/list
                
                // SỬA LẠI LOGIC SO SÁNH:
                // URL phải khớp chính xác, HOẶC là con của feature (ví dụ: feature là /request, URL là /request/history)
                if (requestURL.equals(featureUrl) || requestURL.startsWith(featureUrl + "/")) {
                    hasPermission = true;
                    break; // Dừng ngay khi tìm thấy quyền
                }
            }
        }
        
        // VẤN ĐỀ: /request/history không có trong DB và không khớp logic trên
        // Chúng ta cần một logic "mềm" hơn cho các trang con.
        // NẾU chúng ta vẫn chưa có quyền, hãy thử cách 2:
        if (!hasPermission && userFeatures != null) {
             // Ví dụ: URL là "/request/history". 
             // Chúng ta kiểm tra xem user có quyền "/request/list" không? 
             // Nếu có thì cho qua.
             if (requestURL.equals("/request/history") && userFeatures.contains("/request/list")) {
                 hasPermission = true;
             }
             if (requestURL.equals("/request/edit") && userFeatures.contains("/request/create")) {
                  hasPermission = true;
             }
        }

        // CHỈ GỌI chain.doFilter MỘT LẦN DUY NHẤT TẠI ĐÂY
        if (hasPermission) {
            chain.doFilter(request, response); // Có quyền, cho đi tiếp
        } else {
            // Không có quyền, trả về trang lỗi Access Denied
            res.setContentType("text/html; charset=UTF-8");
            res.getWriter().println("<html><head><title>Lỗi</title><link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/css/style.css\"></head><body>");
            res.getWriter().println("<div class='container login-container'>");
            res.getWriter().println("<h1>Access Denied (Truy cập bị từ chối)</h1>");
            res.getWriter().println("<p>Bạn không có quyền truy cập chức năng này (URL: " + requestURL + ")</p>"); // Thêm URL để debug
            res.getWriter().println("<a href='" + req.getContextPath() + "/request/list' class='btn btn-primary'>Quay về trang chính</a>");
            res.getWriter().println("</div></body></html>");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // (Giữ nguyên init của bạn)
    }

    @Override
    public void destroy() {
        // (Giữ nguyên destroy của bạn)
    }
}