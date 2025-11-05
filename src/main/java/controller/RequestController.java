/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.RequestDAO;
import model.Employee;
import model.RequestForLeave;
import model.RequestHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Acer
 */
public class RequestController extends HttpServlet {

    private RequestDAO requestDAO;

    @Override
    public void init() {
        requestDAO = new RequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        if (action == null) {
            action = "/list";
        }

        Employee employee = (Employee) req.getSession().getAttribute("employee");
        if(employee == null) {
             resp.sendRedirect(req.getContextPath() + "/login.jsp");
             return;
        }

        switch (action) {
            case "/create":
                showCreateForm(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp, employee);
                break;
            case "/list-all":
                listRequestsForManager(req, resp, employee);
                break;
            case "/review":
                showReviewForm(req, resp);
                break;
            case "/history":
                showHistory(req, resp);
                break;
            case "/list":
            default:
                listMyRequests(req, resp, employee);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Hỗ trợ tiếng Việt
        String action = req.getPathInfo();
         
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {
            case "/create":
                handleCreateOrUpdate(req, resp);
                break;
            case "/review":
                handleReview(req, resp);
                break;
        }
    }

    // ---------- Xử lý GET ----------

    private void listMyRequests(HttpServletRequest req, HttpServletResponse resp, Employee employee) throws ServletException, IOException {
        List<RequestForLeave> requests = requestDAO.getRequestsByEmployeeId(employee.getEid());
        req.setAttribute("requests", requests);
        req.setAttribute("pageTitle", "Đơn nghỉ phép của tôi");
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    private void listRequestsForManager(HttpServletRequest req, HttpServletResponse resp, Employee employee) throws ServletException, IOException {
        List<RequestForLeave> requests = requestDAO.getRequestsForManager(employee.getEid());
        req.setAttribute("requests", requests);
        req.setAttribute("pageTitle", "Đơn nghỉ phép của cấp dưới");
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("formTitle", "Tạo đơn xin nghỉ phép");
        req.setAttribute("formAction", "create");
        req.getRequestDispatcher("/create.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, Employee employee) throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("id"));
        RequestForLeave request = requestDAO.getRequestById(rid);
        
        // Chỉ cho phép sửa nếu: đơn bị reject (status==2) VÀ là chủ đơn
        if (request != null && request.getStatus() == 2 && request.getCreatedBy().getEid() == employee.getEid()) {
            req.setAttribute("request", request);
            req.setAttribute("formTitle", "Sửa và Gửi lại đơn");
            req.setAttribute("formAction", "create"); // Vẫn dùng POST /create để xử lý
            req.getRequestDispatcher("/create.jsp").forward(req, resp);
        } else {
             resp.sendRedirect(req.getContextPath() + "/request/list");
        }
    }

    private void showReviewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("id"));
        RequestForLeave request = requestDAO.getRequestById(rid);
        req.setAttribute("request", request);
        req.getRequestDispatcher("/review.jsp").forward(req, resp);
    }
    
    private void showHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("id"));
        RequestForLeave request = requestDAO.getRequestWithHistory(rid); // Lấy cả history
        req.setAttribute("request", request);
        req.getRequestDispatcher("/history.jsp").forward(req, resp);
    }

    // ---------- Xử lý POST ----------

    private void handleCreateOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Employee employee = (Employee) req.getSession().getAttribute("employee");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Lấy thông tin từ form
            String ridStr = req.getParameter("rid");
            String fromDateStr = req.getParameter("fromDate");
            String toDateStr = req.getParameter("toDate");
            String reason = req.getParameter("reason");

            Date fromDate = sdf.parse(fromDateStr);
            Date toDate = sdf.parse(toDateStr);

            RequestForLeave request;
            RequestHistory history = new RequestHistory();
            history.setChangedBy(employee);
            history.setChangeTime(new Date());
            history.setNewStatus(0); // Luôn là 'In Progress' khi tạo/sửa

            if (ridStr != null && !ridStr.isEmpty()) {
                // Đây là CẬP NHẬT (Resubmit)
                request = requestDAO.getRequestById(Integer.parseInt(ridStr));
                history.setOldStatus(request.getStatus()); // Trạng thái cũ là 'Rejected' (2)
                history.setComment("Nhân viên sửa và gửi lại đơn.");
            } else {
                // Đây là TẠO MỚI
                request = new RequestForLeave();
                request.setCreatedBy(employee);
                request.setCreatedTime(new Date());
                history.setOldStatus(null);
                history.setComment("Nhân viên tạo đơn mới.");
            }

            // Cập nhật thông tin
            request.setFromDate(fromDate);
            request.setToDate(toDate);
            request.setReason(reason);
            request.setStatus(0); // Chuyển về 'In Progress'
            request.setProcessedBy(null); // Xóa người duyệt cũ
            request.setManagerComment(null); // Xóa comment cũ
            
            requestDAO.saveOrUpdate(request, history);
            resp.sendRedirect(req.getContextPath() + "/request/list");

        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý lỗi (ví dụ: quay lại form báo lỗi)
            resp.sendRedirect(req.getContextPath() + "/request/create");
        }
    }
    
    private void handleReview(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Employee manager = (Employee) req.getSession().getAttribute("employee");
        
        int rid = Integer.parseInt(req.getParameter("rid"));
        String action = req.getParameter("action"); // "approve" hoặc "reject"
        String comment = req.getParameter("managerComment");
        
        RequestForLeave request = requestDAO.getRequestById(rid);
        if(request == null || request.getStatus() != 0) {
             resp.sendRedirect(req.getContextPath() + "/request/list-all");
             return;
        }

        RequestHistory history = new RequestHistory();
        history.setChangedBy(manager);
        history.setChangeTime(new Date());
        history.setOldStatus(request.getStatus()); // 0
        history.setComment(comment);
        
        if ("approve".equals(action)) {
            request.setStatus(1); // Approved
            history.setNewStatus(1);
        } else {
            request.setStatus(2); // Rejected
            history.setNewStatus(2);
        }
        
        request.setProcessedBy(manager);
        request.setManagerComment(comment);
        
        requestDAO.saveOrUpdate(request, history);
        
        resp.sendRedirect(req.getContextPath() + "/request/list-all");
    }
}
