<jsp:include page="header.jsp">
    <jsp:param name="title" value="${not empty customer ? 'Edit Customer' : 'Add Customer'}"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="customer-form-container">
    <h1 class="h2 mb-4"><c:out value="${not empty customer ? 'Edit Customer' : 'Add New Customer'}" /></h1>

    <form action="${pageContext.request.contextPath}/app/customers" method="post">
        <c:if test="${not empty customer}">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="id" value="${customer.customerId}" />
        </c:if>
        <c:if test="${empty customer}">
            <input type="hidden" name="action" value="insert" />
        </c:if>

        <div class="mb-3">
            <label for="accountNumber" class="form-label">Account Number</label>
            <input type="text" class="form-control" id="accountNumber"
                   name="accountNumber" value="${customer.accountNumber}" required>
        </div>
        <div class="mb-3">
            <label for="name" class="form-label">Full Name</label>
            <input type="text" class="form-control" id="name" name="name" value="${customer.name}" required>
        </div>
        <div class="mb-3">
            <label for="address" class="form-label">Address</label>
            <textarea class="form-control" id="address" name="address" rows="3" required>${customer.address}</textarea>
        </div>
        <div class="mb-3">
            <label for="telephone" class="form-label">Telephone</label>
            <input type="tel" class="form-control" id="telephone" name="telephone" value="${customer.telephone}" required>
        </div>
        <button type="submit" class="btn btn-primary">Save Customer</button>
        <a href="${pageContext.request.contextPath}/app/customers" class="btn btn-secondary">Cancel</a>
    </form>
</div>

<jsp:include page="footer.jsp"/>