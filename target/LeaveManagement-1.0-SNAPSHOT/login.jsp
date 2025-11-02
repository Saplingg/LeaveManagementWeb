<%-- 
    Document   : login
    Created on : Nov 2, 2025, 8:58:13 PM
    Author     : Acer
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container login-container">
        <h1>Đăng nhập</h1>
        <form action="login" method="post">
            <div>
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div>
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Đăng nhập</button>
        </form>
        <p style="color: red;">${requestScope.error}</p>
    </div>
</body>
</html>
