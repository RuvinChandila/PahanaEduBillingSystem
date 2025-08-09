<jsp:include page="header.jsp">
  <jsp:param name="title" value="Create Bill"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="bill-form-container">
  <div class="text-center mb-4">
    <h1 class="h2">Create Bill for: ${customer.name}</h1>
    <p class="text-muted">Step 2 of 2: Add Items</p>
  </div>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <div class="card bill-card mb-4">
    <div class="card-header">Add Items</div>
    <div class="card-body">
      <div class="row g-3 align-items-end">
        <div class="col-md-6">
          <label for="itemSelector" class="form-label">Select Item</label>
          <select id="itemSelector" class="form-select form-select-lg">
            <option value="" disabled selected>-- Choose an item --</option>
            <c:forEach var="item" items="${items}">
              <option value="${item.itemId}" data-price="${item.unitPrice}" data-stock="${item.stockQuantity}" ${item.stockQuantity <= 0 ? 'disabled' : ''}>
                  ${item.description} (Stock: ${item.stockQuantity}) ${item.stockQuantity <= 0 ? '- OUT OF STOCK' : ''}
              </option>
            </c:forEach>
          </select>
        </div>
        <div class="col-md-3">
          <label for="itemQuantity" class="form-label">Quantity</label>
          <input type="number" id="itemQuantity" class="form-control form-control-lg" value="1" min="1">
        </div>
        <div class="col-md-3">
          <button type="button" id="addItemBtn" class="btn btn-success btn-lg w-100"><i class="bi bi-plus-circle"></i> Add</button>
        </div>
      </div>
    </div>
  </div>

  <form action="${pageContext.request.contextPath}/app/bill" method="post" id="billForm">
    <input type="hidden" name="action" value="create">
    <input type="hidden" name="customerId" value="${customer.customerId}">
    <div class="card bill-card">
      <div class="card-header">Bill Items</div>
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table mb-0" id="billItemsTable">
            <thead><tr class="table-light"><th>Item</th><th>Qty</th><th>Unit Price</th><th>Total</th><th>Action</th></tr></thead>
            <tbody></tbody>
            <tfoot><tr class="table-secondary"><th colspan="3" class="text-end">Grand Total:</th><th id="grandTotal">0.00</th><th></th></tr></tfoot>
          </table>
        </div>
      </div>
    </div>
    <div class="mt-4 d-grid">
      <button type="submit" class="btn btn-primary btn-lg">Finalize and Create Bill</button>
    </div>
  </form>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const addItemBtn = document.getElementById('addItemBtn');
    const itemSelector = document.getElementById('itemSelector');
    const itemQuantityInput = document.getElementById('itemQuantity');
    const billItemsTableBody = document.querySelector('#billItemsTable tbody');
    const grandTotalElement = document.getElementById('grandTotal');
    const billForm = document.getElementById('billForm');

    addItemBtn.addEventListener('click', function() {
      try {
        const selectedOption = itemSelector.options[itemSelector.selectedIndex];
        const itemId = selectedOption.value;
        const quantityStr = itemQuantityInput.value;

        // Validation checks
        if (!itemId) {
          alert('Please select a valid item from the list.');
          return;
        }

        if (!quantityStr || isNaN(quantityStr) || parseInt(quantityStr) <= 0) {
          alert('Please enter a valid quantity greater than zero.');
          return;
        }

        const itemName = selectedOption.text.split(' (Stock:')[0];
        const itemPrice = parseFloat(selectedOption.dataset.price);
        const maxStock = parseInt(selectedOption.dataset.stock);
        const quantity = parseInt(quantityStr);

        if (quantity > maxStock) {
          alert('Cannot add ' + quantity + ' items. Only ' + maxStock + ' available in stock.');
          return;
        }

        if (document.querySelector('input[name="itemId"][value="' + itemId + '"]')) {
          alert('This item is already in the bill. Please remove it first to change the quantity.');
          return;
        }

        const total = quantity * itemPrice;
        const newRow = document.createElement('tr');


        newRow.innerHTML =
                '<td>' + itemName + '<input type="hidden" name="itemId" value="' + itemId + '"><input type="hidden" name="price" value="' + itemPrice.toFixed(2) + '"></td>' +
                '<td><input type="hidden" name="quantity" value="' + quantity + '">' + quantity + '</td>' +
                '<td>' + itemPrice.toFixed(2) + '</td>' +
                '<td>' + total.toFixed(2) + '</td>' +
                '<td><button type="button" class="btn btn-danger btn-sm" onclick="removeItem(this)">Remove</button></td>';

        billItemsTableBody.appendChild(newRow);

        updateGrandTotal();
        itemSelector.selectedIndex = 0;
        itemQuantityInput.value = 1;
      } catch (error) {
        console.error("Error adding item:", error);
        alert("An unexpected error occurred while adding the item. Please check the console for details.");
      }
    });

    // Function to remove an item from the table
    window.removeItem = function(button) {
      button.closest('tr').remove();
      updateGrandTotal();
    }

    // Function to update the grand total
    function updateGrandTotal() {
      let total = 0;
      billItemsTableBody.querySelectorAll('tr').forEach(row => {
        const cellValue = row.cells[3].textContent;
        if (cellValue && !isNaN(parseFloat(cellValue))) {
          total += parseFloat(cellValue);
        }
      });
      grandTotalElement.textContent = total.toFixed(2);
    }

    // Listener to prevent submitting an empty bill
    billForm.addEventListener('submit', function(e) {
      if (billItemsTableBody.rows.length === 0) {
        e.preventDefault();
        alert('Cannot create an empty bill. Please add at least one item.');
      }
    });
  });
</script>

<jsp:include page="footer.jsp"/>