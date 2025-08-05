<jsp:include page="header.jsp">
    <jsp:param name="title" value="Customer Management"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="customer-header d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center">
    <h1 class="h2">Customer Management</h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <a href="${pageContext.request.contextPath}/app/customers?action=new" class="btn btn-primary">Add New Customer</a>
    </div>
</div>

<div class="table-responsive">
    <table class="table customer-table">
        <thead>
        <tr>
            <th>Account #</th>
            <th>Name</th>
            <th>Address</th>
            <th>Telephone</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="customer" items="${customerList}">
            <tr>
                <td>${customer.accountNumber}</td>
                <td>${customer.name}</td>
                <td>${customer.address}</td>
                <td>${customer.telephone}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/app/customers?action=edit&id=${customer.customerId}" class="btn btn-sm btn-info">Edit</a>
                    <a href="${pageContext.request.contextPath}/app/customers?action=delete&id=${customer.customerId}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this customer?')">Delete</a>
                </td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp"/>