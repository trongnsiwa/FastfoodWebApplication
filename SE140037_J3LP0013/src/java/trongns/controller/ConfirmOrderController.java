/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.order.OrderDAO;
import trongns.order.OrderDTO;
import trongns.orderdetails.OrderDetailsDAO;
import trongns.orderdetails.OrderDetailsDTO;
import trongns.product.ProductDAO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "ConfirmOrderController", urlPatterns = {"/ConfirmOrderController"})
public class ConfirmOrderController extends HttpServlet {
    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String CHECK_OUT_PAGE = "checkOut.jsp";
    private final String THANK_YOU_PAGE = "thankYou.jsp";
    
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
                String role = (String) session.getAttribute("ROLE");
                if ("Admin".equals(role)) {
                    isAdmin = true;
                    response.sendRedirect(url);
                    return;
                }
                OrderDTO order = (OrderDTO) session.getAttribute("ORDER_INFO");
                if (order != null) {
                    boolean result = false;
                    OrderDAO dao = new OrderDAO();
                    result = dao.insertOrderedProducts(order);
                    
                    ProductDAO productDAO = new ProductDAO();
                    
                    for (OrderDetailsDTO detail : order.getOrderItems()) {
                        int amountOrderedProduct = detail.getAmount();
                        productDAO.getOldQuantityOfProductById(detail.getProductId());
                        int oldQuantity = productDAO.getOldQuantity();
                        
                        int newQuantity = oldQuantity - amountOrderedProduct;
                        result = productDAO.updateQuantityOfProductById(detail.getProductId(), newQuantity);
                    }
                    
                    if (result) {
                        OrderDetailsDAO detailDAO = new OrderDetailsDAO();
                        result = detailDAO.insertOrderedProductDetails(order);
                    }
                    
                    
                    if (result) {
                        url = THANK_YOU_PAGE;
                        session.removeAttribute("CART");
                   } else {
                        url = CHECK_OUT_PAGE;
                        request.setAttribute("checkOutFail", "Sorry for our mistake! Cannot complete your order now!");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                }
            }
            
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ConfirmOrderController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ConfirmOrderController _ SQLException : " + msg);
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
