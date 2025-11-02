<%-- 
    Document   : history
    Created on : Nov 2, 2025, 8:59:15 PM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Lịch sử đơn #${requestScope.request.rid}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>Lịch sử đơn #${requestScope.request.rid} (Tạo bởi: ${requestScope.request.createdBy.ename})</h2>
        <p>
            <strong>Từ:</strong> <fmt:formatDate value="${requestScope.request.fromDate}" pattern="dd/MM/yyyy"/>
            <strong>Đến:</strong> <fmt:formatDate value="${requestScope.request.toDate}" pattern="dd/MM/yyyy"/>
        </p>
         <p><strong>Lý do:</strong> ${requestScope.request.reason}</p>

        <h3>Lịch sử thay đổi</h3>
        <table>
            <thead>
                <tr>
                    <th>Thời gian</th>
                    <th>Người thay đổi</th>
                    <th>Trạng thái cũ</th>
                    <th>Trạng thái mới</th>
                    <th>Ghi chú</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="h" items="${requestScope.request.history}">
                    <tr>
                        <td><fmt:formatDate value="${h.changeTime}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td>${h.changedBy.ename}</td>
                        <td>
                            <c:choose>
                                <c:when test="${h.oldStatus == 0}">In Progress</c:when>
                                <c:when test="${h.oldStatus == 1}">Approved</c:when>
                                <c:when test="${h.oldStatus == 2}">Rejected</c:when>
                                <c:otherwise>N/A (Tạo mới)</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                             <c:choose>
                                <c:when test="${h.newStatus == 0}"><span class="status-inprogress">In Progress</span></c:when>
                                <c:when test="${h.newStatus == 1}"><span class="status-approved">Approved</span></c:when>
                                <c:when test="${h.newStatus == 2}"><span class="status-rejected">Rejected</span></c:when>
                            </c:choose>
                        </td>
                        <td>${h.comment}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <br>
        <a href="javascript:history.back()" class="btn btn-primary">Quay lại</a>
    </div>
</body>
</html>
