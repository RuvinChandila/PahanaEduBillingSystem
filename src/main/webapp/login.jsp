<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Pahana Edu Bookshop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: rgb(220, 220, 220);
        }
        .login-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            max-width: 450px;
            width: 100%;
            border: none;
            border-radius: 1rem;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
        .login-header {
            background-color: #0d6efd;
            color: white;
            border-top-left-radius: 1rem;
            border-top-right-radius: 1rem;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="card login-card">
        <div class="card-header text-center p-4 login-header">
            <h2>Pahana Edu Bookshop</h2>
            <p class="mb-0">Please sign in to continue</p>
        </div>
        <div class="card-body p-5">
            <%-- Display error message from login failure --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${errorMessage}"/>
                </div>
            </c:if>

            <%-- Display success message from registration, if it exists in the session --%>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success" role="alert">
                    <c:out value="${sessionScope.successMessage}"/>
                </div>
                <%-- Important: Remove the attribute from session to prevent it from showing again on refresh --%>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control form-control-lg" id="username" name="username" required>
                </div>
                <div class="mb-4">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control form-control-lg" id="password" name="password" required>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg">Login</button>
                </div>
            </form>
            <div class="text-center mt-4">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register here</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
