<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register - Pahana Edu Bookshop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background-color: #f0f2f5;
    }
    .register-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem 0;
    }
    .register-card {
      max-width: 500px;
      width: 100%;
      border: none;
      border-radius: 1rem;
      box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    }
    .register-header {
      background-color: #198754; /* A green color for registration */
      color: white;
      border-top-left-radius: 1rem;
      border-top-right-radius: 1rem;
    }
  </style>
</head>
<body>
<div class="register-container">
  <div class="card register-card">
    <div class="card-header text-center p-4 register-header">
      <h2>Create an Account</h2>
      <p class="mb-0">Join Pahana Edu Bookshop Today</p>
    </div>
    <div class="card-body p-5">
      <%-- Display any error messages --%>
      <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
          <c:out value="${errorMessage}"/>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="mb-3">
          <label for="fullName" class="form-label">Full Name</label>
          <input type="text" class="form-control form-control-lg" id="fullName" name="fullName" required>
        </div>
        <div class="mb-3">
          <label for="username" class="form-label">Username</label>
          <input type="text" class="form-control form-control-lg" id="username" name="username" required>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control form-control-lg" id="password" name="password" required>
        </div>
        <div class="mb-4">
          <label for="confirmPassword" class="form-label">Confirm Password</label>
          <input type="password" class="form-control form-control-lg" id="confirmPassword" name="confirmPassword" required>
        </div>
        <div class="d-grid">
          <button type="submit" class="btn btn-success btn-lg">Register</button>
        </div>
      </form>
      <div class="text-center mt-4">
        <p>Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login here</a></p>
      </div>
    </div>
  </div>
</div>
</body>
</html>
