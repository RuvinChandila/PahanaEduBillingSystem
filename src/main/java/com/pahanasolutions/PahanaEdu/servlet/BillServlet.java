package com.pahanasolutions.PahanaEdu.servlet;

import com.pahanasolutions.PahanaEdu.dao.BillDao;
import com.pahanasolutions.PahanaEdu.dao.CustomerDao;
import com.pahanasolutions.PahanaEdu.dao.ItemDao;
import com.pahanasolutions.PahanaEdu.model.Bill;
import com.pahanasolutions.PahanaEdu.model.BillItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/app/bill")
public class BillServlet extends HttpServlet {
    private final CustomerDao customerDao = new CustomerDao();
    private final ItemDao itemDao = new ItemDao();
    private final BillDao billDao = new BillDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "new" : request.getParameter("action");
        switch (action) {
            case "create_form":
                showBillCreationForm(request, response);
                break;
            case "view":
                showBillView(request, response);
                break;
            default:
                showCustomerSelectionForm(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if ("create".equals(request.getParameter("action"))) {
            createBill(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/dashboard.jsp");
        }
    }

    private void showCustomerSelectionForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("customers", customerDao.getAllCustomers());
        request.getRequestDispatcher("/app/bill-create-step1.jsp").forward(request, response);
    }

    private void showBillCreationForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            request.setAttribute("customer", customerDao.getCustomerById(customerId));
            request.setAttribute("items", itemDao.getAllItems());
            request.getRequestDispatcher("/app/bill-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid customer selection.");
            showCustomerSelectionForm(request, response);
        }
    }

    private void showBillView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int billId = Integer.parseInt(request.getParameter("billId"));
            Bill bill = billDao.getBillById(billId);
            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/app/bill-view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Bill ID.");
            request.getRequestDispatcher("/app/dashboard.jsp").forward(request, response);
        }
    }

    private void createBill(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int customerId = -1;
        try {
            customerId = Integer.parseInt(request.getParameter("customerId"));
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");

            if (itemIds == null || itemIds.length == 0) {
                throw new Exception("No items were submitted with the bill.");
            }

            List<BillItem> billItems = new ArrayList<>();
            int totalUnits = 0;
            for (int i = 0; i < itemIds.length; i++) {
                // --- MORE SPECIFIC VALIDATION ---
                String itemIdStr = itemIds[i];
                if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
                    throw new Exception("An item was submitted with a missing Item ID.");
                }

                String quantityStr = quantities[i];
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    throw new Exception("An item was submitted with a missing Quantity.");
                }

                String priceStr = prices[i];
                if (priceStr == null || priceStr.trim().isEmpty()) {
                    throw new Exception("An item was submitted with a missing Price.");
                }
                // --- END OF VALIDATION ---

                BillItem billItem = new BillItem();
                billItem.setItemId(Integer.parseInt(itemIdStr));
                int quantity = Integer.parseInt(quantityStr);
                billItem.setQuantity(quantity);
                billItem.setPriceAtPurchase(new BigDecimal(priceStr));
                billItems.add(billItem);
                totalUnits += quantity;
            }

            Bill bill = new Bill();
            bill.setCustomerId(customerId);
            bill.setBillItems(billItems);
            bill.setUnitsConsumed(totalUnits);

            int billId = billDao.createBill(bill);

            if (billId != -1) {
                response.sendRedirect(request.getContextPath() + "/app/bill?action=view&billId=" + billId);
            } else {
                throw new Exception("Database transaction failed. The bill was not saved.");
            }
        } catch (Exception e) {
            System.err.println("ERROR during bill creation: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Could not create the bill. Error: " + e.getMessage());

            // Reload the form with the necessary data so the user can try again
            request.setAttribute("customer", customerDao.getCustomerById(customerId));
            request.setAttribute("items", itemDao.getAllItems());
            request.getRequestDispatcher("/app/bill-form.jsp").forward(request, response);
        }
    }
}
