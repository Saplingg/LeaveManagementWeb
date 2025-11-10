<%-- 
    Document   : agenda
    Created on : Nov 10, 2025, 6:35:44 AM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="vi_VN"/>

<html>
<head>
    <title>Agenda</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* CSS riêng cho trang Agenda */
        table {
            table-layout: fixed; /* Giúp các cột đều nhau */
        }
        th.date-header, td.agenda-cell {
            text-align: center;
            width: 60px; /* Đặt độ rộng cố định */
        }
        .agenda-cell {
            height: 40px;
        }
        .cell-work {
            background-color: #d4edda; /* Màu xanh lá nhạt */
        }
        .cell-leave {
            background-color: #f8d7da; /* Màu đỏ nhạt */
            font-weight: bold;
            color: #721c24;
        }
        .date-picker-form {
            display: flex;
            gap: 15px;
            align-items: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    
    <div class="container">
        <h1>Agenda</h1>
        
        <form action="${pageContext.request.contextPath}/division/agenda" method="get" class="date-picker-form">
            <div>
                <label for="from">Từ ngày:</label>
                <input type.="date" id="from" name="from" value="${requestScope.fromDate}">
            </div>
             <div>
                <label for="to">Đến ngày:</label>
                <input type="date" id="to" name="to" value="${requestScope.toDate}">
            </div>
            <button type="submit" class="btn btn-primary">Xem</button>
            <a href="${pageContext.request.contextPath}/request/list" class="btn">Quay lại danh sách</a>
        </form>
        
        <table>
            <thead>
                <tr>
                    <th>Nhân sự</th>
                    <c:forEach var="date" items="${requestScope.dateRange}">
                        <th class="date-header">
                            <fmt:formatDate value="${date}" pattern="dd/MM"/>
                        </th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="emp" items="${requestScope.employees}">
                    <tr>
                        <td>${emp.ename}</td>
                        
                        <c:forEach var="date" items="${requestScope.dateRange}">
                            
                            <c:set var="isOnLeave" value="${requestScope.leaveMap[emp.eid].contains(date)}"/>

                            <c:choose>
                                <c:when test="${isOnLeave}">
                                    <td class="agenda-cell cell-leave">Nghỉ</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="agenda-cell cell-work"></td>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Đăng xuất</a>
    </div>
</body>
</html>
