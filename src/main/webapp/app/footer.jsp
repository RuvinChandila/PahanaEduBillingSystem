</main> <%-- This closes the <main> from the header --%>
</div> <%-- This closes the <div class="row"> from the header --%>
</div> <%-- This closes the <div class="container-fluid"> from the header --%>

<script>
    // JavaScript for the live clock
    function updateClock() {
        const clockElement = document.getElementById('live-clock');
        if (clockElement) {
            const now = new Date();
            const options = {
                timeZone: 'Asia/Colombo',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: true
            };
            clockElement.textContent = now.toLocaleString('en-US', options);
        }
    }

    // Update the clock every second
    setInterval(updateClock, 1000);

    // Initial call to display the clock immediately
    updateClock();
</script>

</body>
</html>

