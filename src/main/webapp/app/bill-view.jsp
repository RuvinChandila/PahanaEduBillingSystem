<jsp:include page="header.jsp">
    <jsp:param name="title" value="View Bill"/>
</jsp:include>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<style>
    @media print {
        header,
        nav,
        .sidebar,
        footer,
        .d-flex.justify-content-between {
            display: none !important;
        }

        body, main, .container, .container-fluid {
            padding: 0 !important;
            margin: 0 !important;
            width: 100% !important;
            max-width: none !important;
        }

        .invoice-card {
            box-shadow: none;
            border: none;
            margin: 0;
            padding: 0;
        }
    }
</style>

<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3">
    <h1 class="h2">Invoice</h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <a href="${pageContext.request.contextPath}/app/bill?action=new" class="btn btn-sm btn-outline-secondary me-2"><i class="bi bi-plus-circle"></i> New Bill</a>
        <button class="btn btn-sm btn-primary" onclick="window.print();"><i class="bi bi-printer"></i> Print</button>
    </div>
</div>

<div class="invoice-card p-4">
    <div class="row align-items-center mb-4 pb-4 border-bottom">
        <div class="col-md-6">
            <h2 class="mb-0">INVOICE</h2>
        </div>
        <div class="col-md-6 text-md-end">
            <div><strong>Invoice #:</strong> PAHANA EDU BOOK SHOP-${bill.billId}</div>
            <div><strong>Date:</strong> <fmt:formatDate value="${bill.billDate}" type="both" dateStyle="long" timeStyle="short"/></div>
        </div>
    </div>
    <div class="row mb-4">
        <div class="col-sm-6">
            <h5 class="mb-3">To:</h5>
            <div><strong>${bill.customer.name}</strong></div>
            <div>${bill.customer.address}</div>
            <div>Phone: ${bill.customer.telephone}</div>
            <div>Account: ${bill.customer.accountNumber}</div>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead class="table-dark"><tr><th class="text-center">#</th><th>Item Code</th><th>Description</th><th class="text-end">Unit Cost</th><th class="text-center">Qty</th><th class="text-end">Total</th></tr></thead>
            <tbody>
            <c:forEach var="billItem" items="${bill.billItems}" varStatus="status">
                <tr>
                    <td class="text-center">${status.count}</td>
                    <td class="fw-bold">${billItem.item.itemCode}</td>
                    <td>${billItem.item.description}</td>
                    <td class="text-end"><fmt:formatNumber value="${billItem.priceAtPurchase}" type="currency" currencySymbol="Rs."/></td>
                    <td class="text-center">${billItem.quantity}</td>
                    <td class="text-end"><fmt:formatNumber value="${billItem.priceAtPurchase * billItem.quantity}" type="currency" currencySymbol="Rs."/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="row mt-4">
        <div class="col-lg-4 col-sm-5 ms-auto">
            <table class="table table-clear">
                <tbody><tr><td class="text-start fs-5"><strong>Grand Total</strong></td><td class="text-end fs-5"><strong><fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs."/></strong></td></tr></tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
