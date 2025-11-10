/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import dao.EmployeeDAO;
import dao.RequestDAO;
import model.Employee;
import model.RequestForLeave;
/**
 *
 * @author Acer
 */
public class DivisionController extends HttpServlet {

    private EmployeeDAO employeeDAO;
    private RequestDAO requestDAO;

    @Override
    public void init() {
        employeeDAO = new EmployeeDAO();
        requestDAO = new RequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        if (action == null || action.equals("/agenda")) {
            showAgenda(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showAgenda(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Employee manager = (Employee) req.getSession().getAttribute("employee");
        if (manager == null || manager.getDivision() == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        // 1. Lấy khoảng thời gian (Mặc định là 7 ngày tới)
        String fromDateStr = req.getParameter("from");
        String toDateStr = req.getParameter("to");

        LocalDate fromDate = (fromDateStr == null) ? today : LocalDate.parse(fromDateStr);
        LocalDate toDate = (toDateStr == null) ? fromDate.plusDays(6) : LocalDate.parse(toDateStr);

        // 2. Lấy danh sách nhân viên trong phòng ban của quản lý
        List<Employee> employeesInDivision = employeeDAO.getEmployeesByDivision(manager.getDivision().getDid());
        List<Integer> employeeIds = employeesInDivision.stream().map(Employee::getEid).collect(Collectors.toList());

        // 3. Lấy tất cả đơn nghỉ đã duyệt trong khoảng thời gian đó
        java.sql.Date from = java.sql.Date.valueOf(fromDate);
java.sql.Date to = java.sql.Date.valueOf(toDate);

List<RequestForLeave> leaves = requestDAO.getApprovedLeaveForEmployees(employeeIds, from, to);

        // 4. Xử lý dữ liệu: Tạo một Map để JSP dễ dàng kiểm tra
        // Map<EmployeeID, Set<NgàyNghỉ>>
        Map<Integer, Set<LocalDate>> leaveMap = new HashMap<>();
        for (RequestForLeave leave : leaves) {
            LocalDate leaveStart = ((java.sql.Date) leave.getFromDate()).toLocalDate();
            LocalDate leaveEnd = ((java.sql.Date) leave.getToDate()).toLocalDate();

            // Lấy tất cả các ngày trong khoảng nghỉ
            for (LocalDate date = leaveStart; !date.isAfter(leaveEnd); date = date.plusDays(1)) {
                // Chỉ quan tâm những ngày nằm trong khoảng agenda
                if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                    leaveMap.computeIfAbsent(leave.getCreatedBy().getEid(), k -> new HashSet<>()).add(date);
                }
            }
        }

        // 5. Tạo danh sách các ngày cho header của bảng
        List<LocalDate> dateRange = fromDate.datesUntil(toDate.plusDays(1)).collect(Collectors.toList());

        // 6. Gửi dữ liệu tới JSP
        req.setAttribute("employees", employeesInDivision);
        req.setAttribute("dateRange", dateRange);
        req.setAttribute("leaveMap", leaveMap);
        req.setAttribute("fromDate", fromDate.toString());
        req.setAttribute("toDate", toDate.toString());

        req.getRequestDispatcher("/agenda.jsp").forward(req, resp);
    }
}
