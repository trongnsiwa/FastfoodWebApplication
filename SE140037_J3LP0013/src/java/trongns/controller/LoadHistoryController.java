/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "LoadHistoryController", urlPatterns = {"/LoadHistoryController"})
public class LoadHistoryController extends HttpServlet {

    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String SHOPPING_HISTORY_PAGE = "shoppingHistory.jsp";

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
                String role = (String) session.getAttribute("ROLE");
                if ("Admin".equals(role)) {
                    isAdmin = true;
                    response.sendRedirect(url);
                    return;
                }

                url = SHOPPING_HISTORY_PAGE;

                if (user != null) {
                    int pageSize = 5;
                    int pageNo = 1;

                    OrderDAO orderDAO = new OrderDAO();
                    orderDAO.searchOrdersWithConditions(user, pageSize, null, pageNo, 0, 0, 0);
                    ArrayList<OrderDTO> listHistoryOrders = orderDAO.getListSearchedOrders();

                    if (listHistoryOrders != null) {
                        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();
                        for (OrderDTO order : listHistoryOrders) {
                            orderDetailsDAO.loadListOrderDetailByOrderId(order.getId());
                            ArrayList<OrderDetailsDTO> listHistoryOrderDetails = orderDetailsDAO.getListHistoryOrderDetals();
                            order.setOrderItems(listHistoryOrderDetails);
                        }
                        
                        orderDAO.countAllOrdersByUserId(user);
                        int totalOrders = orderDAO.getTotalOrders();

                        request.setAttribute("HISTORY_ORDERS", listHistoryOrders);
                        request.setAttribute("pageNo", pageNo);
                        if (((pageSize * pageNo) >= totalOrders) || listHistoryOrders.size() < pageSize) {
                            request.setAttribute("isLastList", "TRUE");
                        }
                    }
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHistoryController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHistoryController _ SQLException : " + msg);
        } finally {
            if (!isAdmin) {
                request.getRequestDispatcher(url).forward(request, response);
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
