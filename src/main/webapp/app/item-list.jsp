<jsp:include page="header.jsp">
  <jsp:param name="title" value="Item Management"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="item-header d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center">
  <h1 class="h2">Item Management</h1>
  <a href="${pageContext.request.contextPath}/app/items?action=new" class="btn btn-primary">Add New Item</a>
</div>

<div class="table-responsive">
  <table class="table item-table">
    <thead>
    <tr>
      <th>Item Code</th>
      <th>Description</th>
      <th>Unit Price</th>
      <th>Stock</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${itemList}">
      <tr>
        <td>${item.itemCode}</td>
        <td>${item.description}</td>
        <td><fmt:formatNumber type="currency" currencySymbol="LKR " value="${item.unitPrice}" /></td>
        <td>${item.stockQuantity}</td>
        <td>
          <a href="${pageContext.request.contextPath}/app/items?action=edit&id=${item.itemId}" class="btn btn-sm btn-info">Edit</a>
          <a href="${pageContext.request.contextPath}/app/items?action=delete&id=${item.itemId}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp"/>