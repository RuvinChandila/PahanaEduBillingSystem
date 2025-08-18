<jsp:include page="header.jsp">
    <jsp:param name="title" value="Dashboard"/>
</jsp:include>

<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2"><b>Dashboard</b></h1>
</div>
<p>Welcome, <strong>${sessionScope.loggedInUser.fullName}</strong>!</p>

<div class="row">
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-people-fill"></i> Customer Management</h5>
                <p class="card-text">View, add or edit customer accounts.</p>
                <a href="${pageContext.request.contextPath}/app/customers" class="btn btn-primary">Customers</a>
            </div>
        </div>
    </div>
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-book-half"></i> Inventory Control</h5>
                <p class="card-text">View, add or edit items.</p>
                <a href="${pageContext.request.contextPath}/app/items" class="btn btn-primary">Items</a>
            </div>
        </div>
    </div>
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body">
                <h5 class="card-title"><i class="bi bi-receipt-cutoff"></i> Billing</h5>
                <p class="card-text">Create new bills for customer purchases.</p>
                <a href="${pageContext.request.contextPath}/app/bill?action=new" class="btn btn-primary">Create Bill</a>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Sales Overview</h5>
                <canvas id="salesChart" width="400" height="200"></canvas>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Sample data for the chart
    const salesData = {
        labels: ['March', 'April', 'May', 'June', 'July' , 'August'],
        datasets: [{
            label: 'Sales',
            data: [1200, 1900, 3000, 5000, 2300, 3200],
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    };

    // Chart configuration
    const config = {
        type: 'line',
        data: salesData,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    };

    // Create the chart
    var myChart = new Chart(
        document.getElementById('salesChart'),
        config
    );
</script>