/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TrongNS
 */
public class DispatchController extends HttpServlet {

    private final String HOME_PAGE = "home.jsp";
    private final String LOGIN_CONTROLLER = "LoginController";
    private final String SEARCH_PRODUCT_CONTROLLER = "SearchProductController";
    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String DELETE_PRODUCT_CONTROLLER = "DeleteProductController";
    private final String CREATE_NEW_PRODUCT_CONTROLLER = "CreateNewProductController";
    private final String ADD_TO_CART_CONTROLLER = "AddToCartController";
    private final String REMOVE_PRODUCT_FROM_CART_CONTROLLER = "RemoveProductFromCartController";
    private final String CHECK_OUT_CART_CONTROLLER = "CheckOutCartController";
    private final String CONFIRM_ORDER_CONTROLLER = "ConfirmOrderController";
    private final String LOAD_HISTORY_CONTROLLER = "LoadHistoryController";
    private final String SEARCH_HISTORY_ORDER_CONTROLLER = "SearchHistoryOrderController";
    private final String LOGOUT_CONTROLLER = "LogoutController";
    private final String LOAD_UPDATED_PRODUCT_CONTROLLER = "LoadUpdatedProductController";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String url = HOME_PAGE;

        String action = request.getParameter("action");

        try {
            if (action == null) {
                url = LOAD_HOME_CONTROLLER;
            } else {
                switch (action) {
                    case "Login":
                        url = LOGIN_CONTROLLER;
                        break;
                    case "Search":
                        url = SEARCH_PRODUCT_CONTROLLER;
                        break;
                    case "Delete":
                        url = DELETE_PRODUCT_CONTROLLER;
                        break;
                    case "Create New Product":
                        url = CREATE_NEW_PRODUCT_CONTROLLER;
                        break;
                    case "Add To Cart":
                        url = ADD_TO_CART_CONTROLLER;
                        break;
                    case "Remove From Cart":
                        url = REMOVE_PRODUCT_FROM_CART_CONTROLLER;
                        break;
                    case "Order Now":
                        url = CHECK_OUT_CART_CONTROLLER;
                        break;
                    case "Confirm":
                        url = CONFIRM_ORDER_CONTROLLER;
                        break;
                    case "History":
                        url = LOAD_HISTORY_CONTROLLER;
                        break;
                    case "Logout":
                        url = LOGOUT_CONTROLLER;
                        break;
                    case "Search History":
                        url = SEARCH_HISTORY_ORDER_CONTROLLER;
                        break;
                    case "Load Updated Product":
                        url = LOAD_UPDATED_PRODUCT_CONTROLLER;
                        break;
                }
            }
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
