<jsp:include page="header.jsp">
    <jsp:param name="title" value="Help"/>
</jsp:include>

<div class="help-container">
    <div class="text-center mb-5">
        <h1 class="h2">Help & Documentation</h1>
        <p class="lead text-muted">Welcome to the Pahana Edu Bookshop Management System. Here's how to get started.</p>
    </div>

    <div class="help-topic">
        <div class="help-topic-icon"><i class="bi bi-house-door-fill"></i></div>
        <div class="help-topic-content">
            <h5>Dashboard</h5>
            <p>After logging in, you will see the main dashboard which provides quick access to all features.</p>
        </div>
    </div>

    <div class="help-topic">
        <div class="help-topic-icon"><i class="bi bi-people-fill"></i></div>
        <div class="help-topic-content">
            <h5>Manage Customers</h5>
            <p>Click on the "Customers" to view, add, edit or delete customer records.</p>
        </div>
    </div>

    <div class="help-topic">
        <div class="help-topic-icon"><i class="bi bi-book-fill"></i></div>
        <div class="help-topic-content">
            <h5>Manage Items</h5>
            <p>Click on the "Items" to view, add, edit or delete items.</p>
        </div>
    </div>

    <div class="help-topic">
        <div class="help-topic-icon"><i class="bi bi-receipt-cutoff"></i></div>
        <div class="help-topic-content">
            <h5>Create Bill</h5>
            <p>Click on the "Create Bill" to generate bills for customers based on the items they have purchased.</p>
        </div>
    </div>

    <div class="help-topic">
        <div class="help-topic-icon"><i class="bi bi-printer-fill"></i></div>
        <div class="help-topic-content">
            <h5>Print Bill</h5>
            <p>After creating a bill, you can use the "Print Bill" option to generate a printable copy of the invoice for the customer.</p>
        </div>
    </div>

</div>

<jsp:include page="footer.jsp"/>