<%-- 
    Document   : create
    Created on : Nov 2, 2025, 8:58:39 PM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${requestScope.formTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container login-container">
        <h1>${requestScope.formTitle}</h1>
        
        <form action="${pageContext.request.contextPath}/request/${requestScope.formAction}" method="post">
            
            <c:if test="${requestScope.request != null}">
                <input type="hidden" name="rid" value="${requestScope.request.rid}">
            </c:if>

            <div>
                <label>Người tạo:</label>
                <input type="text" value="${sessionScope.employee.ename}" disabled>
            </div>
            
            <div>
                <label for="fromDate">Từ ngày:</label>
                <fmt:formatDate value="${requestScope.request.fromDate}" pattern="yyyy-MM-dd" var="fDate"/>
                <input type="date" id="fromDate" name="fromDate" value="${fDate}" required>
            </div>
            
            <div>
                <label for="toDate">Đến ngày:</label>
                <fmt:formatDate value="${requestScope.request.toDate}" pattern="yyyy-MM-dd" var="tDate"/>
                <input type="date" id="toDate" name="toDate" value="${tDate}" required>
            </div>
            
            <div>
                <label for="reason">Lý do:</label>
                <textarea id="reason" name="reason" required>${requestScope.request.reason}</textarea>
            </div>

            <c:if test="${requestScope.request != null && requestScope.request.status == 2}">
                <div style="background: #fffbe6; padding: 10px; border-radius: 4px; border: 1px solid #ffe58f;">
                    <strong>Lý do bị từ chối (bởi ${requestScope.request.processedBy.ename}):</strong>
                    <p>${requestScope.request.managerComment}</p>
                </div>
            </c:if>

            <button type="submit" class="btn btn-primary">Gửi đơn</button>
            <a href="${pageContext.request.contextPath}/request/list" class="btn">Hủy</a>
        </form>
    </div>
</body>
</html>
