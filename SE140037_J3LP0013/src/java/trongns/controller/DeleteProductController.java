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
import trongns.product.ProductDAO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "DeleteProductController", urlPatterns = {"/DeleteProductController"})
public class DeleteProductController extends HttpServlet {
    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String SEARCH_PRODUCT_CONTROLLER = "SearchProductController";

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
        boolean haveParams = true;

        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                haveParams = false;
            }

            if (haveParams) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String user = (String) session.getAttribute("USER");
                    if (user != null) {
                        String admin = (String) session.getAttribute("ROLE");
                        if ("Admin".equals(admin)) {
                            url = SEARCH_PRODUCT_CONTROLLER;
                            isAdmin = true;
                            boolean result = false;
                            ProductDAO dao = new ProductDAO();
                            String[] removedItems = request.getParameterValues("chkItem");
                            String msg = "";

                            if (removedItems != null) {
                                for (String removedItem : removedItems) {
                                    result = dao.deleteProductById(removedItem, user);
                                }
                                for (int i = 0; i < removedItems.length - 1; i++) {
                                    msg += removedItems[i] + ", ";
                                }
                                msg += removedItems[removedItems.length - 1];
                            }

                            if (result) {
                                request.setAttribute("productDeleteMsg", "Delete products " + msg + " successfully");
                            } else {
                                request.setAttribute("productDeleteMsg", "Delete product " + msg + " fail");
                            }
                        }
                    }
                }
            }
            String pageNo = request.getParameter("pageNo");
            if (pageNo != null) {
                request.setAttribute("pageNo", pageNo);
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at DeleteProductController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at DeleteProductController _ SQLException : " + msg);
        } finally {
            if (!isAdmin || !haveParams) {
                response.sendRedirect(url);
            } else {
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
