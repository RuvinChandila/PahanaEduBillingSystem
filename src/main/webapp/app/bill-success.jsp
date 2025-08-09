<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp">
  <jsp:param name="title" value="Bill Created Successfully"/>
</jsp:include>

<div class="success-container">
  <div class="success-icon">
    <i class="bi bi-check-circle-fill"></i>
  </div>
  <h1 class="h2 font-bold text-gray-800 mb-2">Bill Created Successfully!</h1>
  <p class="text-muted mb-4 fs-5">
    A new bill has been generated with Bill ID:
    <span class="badge bg-secondary fs-6">${billDetails.billId}</span>
  </p>

  <c:if test="${not empty billDetails}">
    <div class="mt-4 border-top pt-4">
      <h2 class="h4 mb-3 text-center">Bill Summary</h2>
      <div class="row mb-4 text-start">
        <div class="col-md-6">
          <h5>Billed To:</h5>
          <p class="mb-0"><strong>${billDetails.customer.name}</strong></p>
          <p class="mb-0">${billDetails.customer.address}</p>
          <p class="mb-0">A/C: ${billDetails.customer.accountNumber}</p>
        </div>
        <div class="col-md-6 text-md-end mt-3 mt-md-0">
          <h5>Bill Details:</h5>
          <p class="mb-0"><strong>Bill ID:</strong> ${billDetails.billId}</p>
          <p class="mb-0"><strong>Date:</strong> <fmt:formatDate value="${billDetails.billDate}" pattern="yyyy-MM-dd HH:mm" /></p>
        </div>
      </div>

      <div class="table-responsive border rounded-3">
        <table class="table table-bordered mb-0">
          <thead class="table-light">
          <tr>
            <th>Item</th>
            <th class="text-center">Qty</th>
            <th class="text-end">Price</th>
            <th class="text-end">Subtotal</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="billItem" items="${billDetails.billItems}">
            <c:set var="subtotal" value="${billItem.quantity * billItem.priceAtPurchase}" />
            <tr>
              <td>${billItem.item.description}</td>
              <td class="text-center">${billItem.quantity}</td>
              <td class="text-end"><fmt:formatNumber value="${billItem.priceAtPurchase}" type="currency" currencySymbol="Rs."/></td>
              <td class="text-end"><fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="Rs."/></td>
            </tr>
          </c:forEach>
          </tbody>
          <tfoot class="table-secondary">
          <tr>
            <td colspan="3" class="text-end"><strong>Total Amount:</strong></td>
            <td class="text-end"><strong><fmt:formatNumber value="${billDetails.totalAmount}" type="currency" currencySymbol="Rs."/></strong></td>
          </tr>
          </tfoot>
        </table>
      </div>
    </div>
  </c:if>

  <div class="d-grid gap-2 d-sm-flex justify-content-sm-center mt-4">
    <a href="${pageContext.request.contextPath}/app/bill?action=new" class="btn btn-primary btn-lg px-4">
      <i class="bi bi-plus-circle"></i> Create Another Bill
    </a>
    <a href="${pageContext.request.contextPath}/app/dashboard.jsp" class="btn btn-secondary btn-lg px-4">
      <i class="bi bi-house-door"></i> Go to Dashboard
    </a>
  </div>
</div>

<jsp:include page="footer.jsp"/>