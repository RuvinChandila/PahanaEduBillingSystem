<jsp:include page="header.jsp">
  <jsp:param name="title" value="Create Bill - Select Customer"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="bill-form-container">
  <div class="text-center mb-4">
    <h1 class="h2">Create a New Bill</h1>
    <p class="text-muted">Step 1 of 2: Select a Customer</p>
  </div>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/app/bill" method="get" class="mx-auto" style="max-width: 500px;">
    <input type="hidden" name="action" value="create_form">
    <div class="mb-3">
      <label for="customerId" class="form-label fs-5">Customer</label>
      <select class="form-select form-select-lg" id="customerId" name="customerId" required>
        <option value="" disabled selected>-- Choose a customer --</option>
        <c:forEach var="customer" items="${customers}">
          <option value="${customer.customerId}">${customer.name} (${customer.accountNumber})</option> [cite: 3]
        </c:forEach>
      </select>
    </div>
    <div class="d-grid gap-2">
      <button type="submit" class="btn btn-primary btn-lg">Proceed to Add Items <i class="bi bi-arrow-right"></i></button>
    </div>
  </form>
</div>

<jsp:include page="footer.jsp"/>