<jsp:include page="header.jsp">
  <jsp:param name="title" value="${not empty item ? 'Edit Item' : 'Add Item'}"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="item-form-container">
  <h1 class="h2 mb-4"><c:out value="${not empty item ? 'Edit Item' : 'Add New Item'}" /></h1>

  <form action="${pageContext.request.contextPath}/app/items" method="post">
    <input type="hidden" name="action" value="${not empty item ? 'update' : 'insert'}" />
    <c:if test="${not empty item}"><input type="hidden" name="id" value="${item.itemId}" /></c:if>

    <div class="mb-3">
      <label for="itemCode" class="form-label">Item Code</label>
      <input type="text" id="itemCode" class="form-control" name="itemCode" value="${item.itemCode}" required>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">Description</label>
      <input type="text" id="description" class="form-control" name="description" value="${item.description}" required>
    </div>
    <div class="mb-3">
      <label for="unitPrice" class="form-label">Unit Price (LKR)</label>
      <input type="number" id="unitPrice" step="0.01" class="form-control" name="unitPrice" value="${item.unitPrice}" required>
    </div>
    <div class="mb-3">
      <label for="stockQuantity" class="form-label">Stock Quantity</label>
      <input type="number" id="stockQuantity" class="form-control" name="stockQuantity" value="${item.stockQuantity}" required>
    </div>
    <button type="submit" class="btn btn-primary">Save Item</button>
    <a href="${pageContext.request.contextPath}/app/items" class="btn btn-secondary">Cancel</a>
  </form>
</div>

<jsp:include page="footer.jsp"/>