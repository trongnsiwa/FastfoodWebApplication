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
import trongns.user.UserDAO;
import trongns.user.UserDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String CHECK_OUT_PAGE = "checkOut.jsp";
    private final String CART_PAGE = "cart.jsp";
    private final String INVALID_PAGE = "login.jsp";

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

        String url = INVALID_PAGE;

        boolean success = false;
        boolean haveParams = true;
        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                haveParams = false;
                response.sendRedirect(url);
                return;
            }

            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");
            String goBackPage = request.getParameter("goBackPage");

            UserDAO dao = new UserDAO();
            dao.checkLogin(username, password);
            UserDTO dto = dao.getCheckedLoginUser();

            if (dto != null) {
                if ("home".equals(goBackPage)) {
                    String searchProduct = request.getParameter("txtSearchProduct");
                    String pageNo = request.getParameter("pageNo");
                    String category = request.getParameter("cbCategory");
                    String fromPrice = request.getParameter("txtFromPrice");
                    String toPrice = request.getParameter("txtToPrice");
                    if (searchProduct != null && !searchProduct.trim().isEmpty()
                            || category != null && !category.trim().isEmpty()
                            || fromPrice != null && !fromPrice.trim().isEmpty()
                            || toPrice != null && !toPrice.trim().isEmpty()) {
                        url = "SearchProductController"
                                + "?txtSearchProduct=" + searchProduct;
                        if (pageNo != null && !pageNo.trim().isEmpty()) {
                            url += "&pageNo=" + pageNo;
                        }
                        url += "&cbCategory=" + category
                                + "&txtFromPrice=" + fromPrice
                                + "&txtToPrice=" + toPrice;
                    } else {
                        url = LOAD_HOME_CONTROLLER;
                    }
                } else if ("cart".equals(goBackPage)) {
                    url = CART_PAGE;
                } else if ("checkOut".equals(goBackPage)) {
                    url = CHECK_OUT_PAGE;
                } else {
                    url = LOAD_HOME_CONTROLLER;
                }

                HttpSession session = request.getSession();
                session.setAttribute("USER", dto.getUsername());
                session.setAttribute("FULLNAME", dto.getFirstname() + " " + dto.getLastname());
                session.setAttribute("ROLE", dto.getRole());

                success = true;
            } else {
                request.setAttribute("INVALID", "Invalid username or password");
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoginController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoginController _ SQLException : " + msg);
        } finally {
            if (haveParams) {
                if (success) {
                    response.sendRedirect(url);
                } else {
                    request.getRequestDispatcher(url).forward(request, response);
                }
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
