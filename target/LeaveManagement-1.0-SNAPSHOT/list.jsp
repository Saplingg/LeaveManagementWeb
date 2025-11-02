<%-- 
    Document   : list
    Created on : Nov 2, 2025, 8:58:29 PM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Danh sách đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>Xin chào, ${sessionScope.user.displayName}</h2>
        
        <c:if test="${sessionScope.userFeatures.contains('/request/create')}">
            <a href="${pageContext.request.contextPath}/request/create" class="btn btn-primary">Tạo đơn mới</a>
        </c:if>

        <c:if test="${sessionScope.userFeatures.contains('/request/list-all')}">
            <a href="${pageContext.request.contextPath}/request/list-all" class="btn btn-primary">Xem đơn cấp dưới</a>
        </c:if>

        <h1>${requestScope.pageTitle}</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Ngày tạo</th>
                    <th>Người tạo</th>
                    <th>Từ ngày</th>
                    <th>Đến ngày</th>
                    <th>Trạng thái</th>
                    <th>Người xử lý</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="req" items="${requestScope.requests}">
                    <tr>
                        <td>${req.rid}</td>
                        <td><fmt:formatDate value="${req.createdTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>${req.createdBy.ename}</td>
                        <td><fmt:formatDate value="${req.fromDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${req.toDate}" pattern="dd/MM/yyyy"/></td>
                        <td>
                            <span class="status-${req.statusText.toLowerCase()}">${req.statusText}</span>
                        </td>
                        <td>${req.processedBy != null ? req.processedBy.ename : 'N/A'}</td>
                        <td>
                            <c:if test="${req.status == 2 && req.createdBy.user.uid == sessionScope.user.uid && sessionScope.userFeatures.contains('/request/create')}">
                                <a href="${pageContext.request.contextPath}/request/edit?id=${req.rid}" class="btn btn-primary">Sửa và Gửi lại</a>
                            </c:if>

                            <c:if test="${sessionScope.userFeatures.contains('/request/review') && req.status == 0}">
                                <a href="${pageContext.request.contextPath}/request/review?id=${req.rid}" class="btn btn-success">Duyệt đơn</a>
                            </c:if>
                            
                             <a href="${pageContext.request.contextPath}/request/history?id=${req.rid}" class="btn">Xem Lịch sử</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
