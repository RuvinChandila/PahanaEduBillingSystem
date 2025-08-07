package com.pahanasolutions.PahanaEdu.servlet;

import com.pahanasolutions.PahanaEdu.dao.ItemDao;
import com.pahanasolutions.PahanaEdu.model.Item;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/app/items")
public class ItemServlet extends HttpServlet {
    private ItemDao itemDao = new ItemDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                listItems(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertItem(request, response);
        } else if ("update".equals(action)) {
            updateItem(request, response);
        } else {
            listItems(request, response);
        }
    }

    private void listItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Item> itemList = itemDao.getAllItems();
        request.setAttribute("itemList", itemList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/app/item-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/app/item-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Item existingItem = itemDao.getItemById(id);
        request.setAttribute("item", existingItem);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/app/item-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = new Item();
        item.setItemCode(request.getParameter("itemCode"));
        item.setDescription(request.getParameter("description"));
        item.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
        item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        itemDao.addItem(item);
        response.sendRedirect(request.getContextPath() + "/app/items?action=list");
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = new Item();
        item.setItemId(Integer.parseInt(request.getParameter("id")));
        item.setItemCode(request.getParameter("itemCode"));
        item.setDescription(request.getParameter("description"));
        item.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
        item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        itemDao.updateItem(item);
        response.sendRedirect(request.getContextPath() + "/app/items?action=list");
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        itemDao.deleteItem(id);
        response.sendRedirect(request.getContextPath() + "/app/items?action=list");
    }
}