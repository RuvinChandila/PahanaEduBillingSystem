<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=2.0">
  <title>${param.title} - Pahana Edu Book Shop</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard_style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer_style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/item_style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/help_style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bill_style.css">

  <style>
    body {
      background-color: rgb(220, 220, 220);
    }
    .sidebar {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      z-index: 100;
      padding: 48px 0 0;
      box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
      width: 220px;
    }
    .sidebar-sticky {
      position: relative;
      top: 0;
      height: calc(100vh - 48px);
      padding-top: .5rem;
      overflow-x: hidden;
      overflow-y: auto;
    }
    .nav-link {
      font-weight: 500;
      color: #333;
    }
    .nav-link.active {
      color: #0d6efd;
    }
    .main-content {
      margin-left: 240px;
      padding: 2rem;
    }
    .navbar-info {
      font-size: 1rem;
    }
  </style>
</head>
<body>

<nav class="navbar navbar-dark bg-dark fixed-top flex-md-nowrap p-0 shadow">
  <a class="navbar-brand col-sm-3 col-md-2 mr-0 p-2" href="${pageContext.request.contextPath}/app/dashboard.jsp">Pahana Edu Book Shop</a>

  <div class="d-flex align-items-center text-white ms-auto navbar-info">
    <span class="me-3"><i class="bi bi-geo-alt-fill"></i> Negombo, Sri Lanka</span>
    <span id="live-clock" class="me-3"></span>
  </div>

  <ul class="navbar-nav px-3">
    <li class="nav-item text-nowrap">
      <a class="btn btn-danger btn-sm" href="${pageContext.request.contextPath}/logout">Sign out</a>
    </li>
  </ul>
</nav>

<div class="container-fluid">
  <div class="row">
    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
      <div class="sidebar-sticky">
        <ul class="nav flex-column">
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/app/dashboard.jsp"><i class="bi bi-house-door"></i> Dashboard</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/app/customers"><i class="bi bi-people"></i> Customers</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/app/items"><i class="bi bi-book"></i> Items</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/app/bill?action=new"><i class="bi bi-receipt"></i> Create Bill</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/app/help.jsp"><i class="bi bi-question-circle"></i> Help</a></li>
        </ul>
      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-md-4 main-content">
      <script>
        // JavaScript for the live clock
        function updateClock() {
          const clockElement = document.getElementById('live-clock');
          if (clockElement) {
            const now = new Date();
            const options = {
              timeZone: 'Asia/Colombo',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: true
            };
            clockElement.textContent = now.toLocaleString('en-US', options);
          }
        }
        // Update the clock every second
        setInterval(updateClock, 1000);
        // Initial call to display the clock immediately
        updateClock();
      </script>
