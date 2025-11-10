<%-- 
    Document   : review
    Created on : Nov 2, 2025, 8:58:54 PM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Duyệt đơn nghỉ phép</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    
    <div class="container login-container">
        <h1>Duyệt đơn nghỉ phép</h1>
        
        <form action="${pageContext.request.contextPath}/request/review" method="post">
            <input type="hidden" name="rid" value="${requestScope.request.rid}">

            <div>
                <label>Người tạo:</label>
                <input type="text" value="${requestScope.request.createdBy.ename}" disabled>
            </div>
            <div>
                <label>Từ ngày:</label>
                <fmt:formatDate value="${requestScope.request.fromDate}" pattern="yyyy-MM-dd" var="fDate"/>
                <input type="date" value="${fDate}" disabled>
            </div>
            <div>
                <label>Đến ngày:</label>
                <fmt:formatDate value="${requestScope.request.toDate}" pattern="yyyy-MM-dd" var="tDate"/>
                <input type="date" value="${tDate}" disabled>
            </div>
            <div>
                <label>Lý do:</label>
                <textarea disabled>${requestScope.request.reason}</textarea>
            </div>
            
            <hr>
            
            <div>
                <label>Duyệt bởi:</label>
                <input type="text" value="${sessionScope.employee.ename}" disabled>
            </div>
            
             <div>
                <label for="managerComment">Ghi chú (Lý do duyệt/từ chối):</label>
                <textarea id="managerComment" name="managerComment"></textarea>
            </div>

            <div class="action-buttons">
                <button type="submit" name="action" value="approve" class="btn btn-success">Approve (Duyệt)</button>
                
                <button type="submit" name="action" value="reject" class="btn btn-danger">Reject (Từ chối)</button>
            </div>
            <a href="${pageContext.request.contextPath}/request/list-all" class="btn">Quay lại</a>
        </form>
    </div>
</body>
</html>
