/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.category.CategoryDAO;
import trongns.category.CategoryDTO;
import trongns.product.ProductDAO;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "LoadHomeController", urlPatterns = {"/LoadHomeController"})
public class LoadHomeController extends HttpServlet {
    private final String HOME_PAGE = "home.jsp";

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

        try {
            HttpSession session = request.getSession(false);
            String role = null;
            if (session != null) {
                role = (String) session.getAttribute("ROLE");
            }

            ArrayList<ProductDTO> listHomeProducts = null;
            ProductDAO dao = new ProductDAO();
            if ("Admin".equals(role)) {
                String currentPage = request.getParameter("pageNo");
                int pageNo;

                if (currentPage == null) {
                    pageNo = 1;
                } else {
                    pageNo = Integer.parseInt(currentPage);
                }

                dao.countAllProducts(role);
                int totalProducts = dao.getTotalProducts();

                dao.searchProductsWithConditions("", BigDecimal.ZERO, BigDecimal.ZERO, null, pageNo, 20, role);
                listHomeProducts = dao.getListSearchedProducts();
                request.setAttribute("pageNo", pageNo);

                if (((20 * pageNo) >= totalProducts) || listHomeProducts.size() < 20) {
                    request.setAttribute("isLastList", "TRUE");
                }
            } else {
                dao.getListFirstProducts(20, role);
                listHomeProducts = dao.getListHomeProducts();
            }

            CategoryDAO cateDAO = new CategoryDAO();
            cateDAO.getAllCategories();
            ArrayList<CategoryDTO> listCategories = cateDAO.getListCategories();

            if (listHomeProducts != null) {
                request.setAttribute("listProducts", listHomeProducts);
                request.setAttribute("categoryList", listCategories);
            }

            if (session != null) {
                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHomeController _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHomeController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHomeController _ SQLException : " + msg);
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
