/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.cart.CartObj;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "UpdateCartController", urlPatterns = {"/UpdateCartController"})
public class UpdateCartController extends HttpServlet {

    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String CART_PAGE = "cart.jsp";

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

        String url = LOAD_HOME_CONTROLLER;
        boolean isAdmin = false;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String user = (String) session.getAttribute("USER");
                if (user != null) {
                    String role = (String) session.getAttribute("ROLE");
                    if ("Admin".equals(role)) {
                        isAdmin = true;
                        response.sendRedirect(url);
                        return;
                    }
                }

                url = CART_PAGE;

                CartObj cart = (CartObj) session.getAttribute("CART");
                if (cart != null) {
                    String productId = request.getParameter("txtProductId");
                    ProductDTO product = cart.getProductInCartById(productId);
                    int oldQuantity = cart.getItems().get(product);
                    int amount = 0;
                    String action = request.getParameter("action");
                    if ("increaseAmount".equals(action)) {
                        amount = oldQuantity + 1;
                    } else if ("decreaseAmount".equals(action)) {
                        if (oldQuantity > 1) {
                            amount = oldQuantity - 1;
                        } else {
                            amount = 1;
                        }
                    }

                    cart.updateProductInCart(product, amount);
                    session.setAttribute("CART", cart);
                }
            }
        } finally {
            if (!isAdmin) {
                response.sendRedirect(url);
            }
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
