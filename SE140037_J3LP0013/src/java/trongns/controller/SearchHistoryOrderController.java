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
@WebServlet(name = "SearchHistoryOrderController", urlPatterns = {"/SearchHistoryOrderController"})
public class SearchHistoryOrderController extends HttpServlet {

    private final String LOAD_HISTORY_CONTROLLER = "LoadHistoryController";
    private final String SHOPPING_HISTORY_PAGE = "shoppingHistory.jsp";
    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";

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
                    String admin = (String) session.getAttribute("ROLE");
                    if ("Admin".equals(admin)) {
                        isAdmin = true;
                        response.sendRedirect(url);
                        return;
                    }
                }

                url = LOAD_HISTORY_CONTROLLER;

                int pageNo;
                int pageSize = 5;
                String searchOrder = request.getParameter("txtSearchOrder");
                String currentPage = request.getParameter("pageNo");
                String sday = request.getParameter("cbDay");
                String smonth = request.getParameter("cbMonth");
                String syear = request.getParameter("cbYear");

                if (currentPage == null) {
                    pageNo = 1;
                } else {
                    pageNo = Integer.parseInt(currentPage);
                }

                String action = request.getParameter("action");

                if ("Prev".equals(action)) {
                    if (pageNo > 1) {
                        pageNo--;
                    }
                } else if ("Next".equals(action)) {
                    pageNo++;
                }

                if ((searchOrder != null && !searchOrder.trim().isEmpty())
                        || (sday != null && !sday.trim().isEmpty())
                        || (smonth != null && !smonth.trim().isEmpty())
                        || (syear != null && !syear.trim().isEmpty())
                        || (!"Search History".equals(action))) {
                    url = SHOPPING_HISTORY_PAGE;

                    int day = 0, month = 0, year = 0;
                    if (sday != null && !sday.trim().isEmpty()) {
                        day = Integer.parseInt(sday);
                    }

                    if (smonth != null && !smonth.trim().isEmpty()) {
                        month = Integer.parseInt(smonth);
                    }

                    if (syear != null && !syear.trim().isEmpty()) {
                        year = Integer.parseInt(syear);
                    }

                    OrderDAO orderDAO = new OrderDAO();
                    orderDAO.searchOrdersWithConditions(user, pageSize, searchOrder, pageNo, day, month, year);
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

                        orderDAO.countOrdersWithConditions(user, searchOrder, day, month, year);
                        int totalSearchedOrders = orderDAO.getTotalSearchedOrders();

                        request.setAttribute("HISTORY_ORDERS", listHistoryOrders);
                        request.setAttribute("pageNo", pageNo);
                        if (((pageSize * pageNo) >= totalOrders) || listHistoryOrders.size() < pageSize || totalSearchedOrders == pageSize) {
                            request.setAttribute("isLastList", "TRUE");
                        }
                    }
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at SearchHistoryOrderController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at SearchHistoryOrderController _ SQLException : " + msg);
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
