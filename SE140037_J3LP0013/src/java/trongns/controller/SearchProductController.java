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
import trongns.product.ProductDAO;
import trongns.category.CategoryDTO;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "SearchProductController", urlPatterns = {"/SearchProductController"})
public class SearchProductController extends HttpServlet {

    private final String HOME_PAGE = "home.jsp";
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
        try {
            int pageNo;
            int pageSize = 20;
            String sfromPrice = request.getParameter("txtFromPrice");
            String stoPrice = request.getParameter("txtToPrice");
            String categoryId = request.getParameter("cbCategory");
            String searchProduct = request.getParameter("txtSearchProduct");
            String currentPage = request.getParameter("pageNo");

            if (currentPage == null) {
                pageNo = 1;
            } else {
                pageNo = Integer.parseInt(currentPage);
            }

            HttpSession session = request.getSession(false);
            String role = null;
            if (session != null) {
                role = (String) session.getAttribute("ROLE");
            }
            String action = request.getParameter("action");

            if ("Prev".equals(action)) {
                if (pageNo > 1) {
                    pageNo--;
                }
            } else if ("Next".equals(action)) {
                pageNo++;
            }

            boolean valid = true;
            BigDecimal fromPrice = BigDecimal.ZERO;
            BigDecimal toPrice = BigDecimal.ZERO;
            boolean haveFromPrice = false;
            boolean haveToPrice = false;
            if (sfromPrice != null && !sfromPrice.trim().isEmpty()) {
                fromPrice = new BigDecimal(sfromPrice);
                haveFromPrice = true;
            }
            if (stoPrice != null && !stoPrice.trim().isEmpty()) {
                toPrice = new BigDecimal(stoPrice);
                haveToPrice = true;
            }

            if (haveFromPrice && haveToPrice) {
                if (fromPrice.compareTo(toPrice) == 1) {
                    valid = false;
                    request.setAttribute("RANGE_ERROR", "Please fill in a valid range of money");
                }
            }

            CategoryDAO cateDAO = new CategoryDAO();

            if (valid) {
                if ((searchProduct != null && !searchProduct.trim().isEmpty())
                        || (sfromPrice != null && !sfromPrice.trim().isEmpty())
                        || (stoPrice != null && !stoPrice.trim().isEmpty())
                        || (categoryId != null && !categoryId.trim().isEmpty() || "Admin".equals(role))) {
                    String category = null;
                    if (categoryId != null && !categoryId.trim().isEmpty()) {
                    int id = Integer.parseInt(categoryId);
                        cateDAO.getCategoryById(id);
                        category = cateDAO.getCategory();
                    }

                    ProductDAO dao = new ProductDAO();
                    dao.countAllProducts(role);
                    int totalProducts = dao.getTotalProducts();
                    
                    dao.countProductsWithConditions(searchProduct, fromPrice, toPrice, category, role);
                    int totalSearchedProducts = dao.getTotalSearchedProducts();

                    dao.searchProductsWithConditions(searchProduct, fromPrice, toPrice, category, pageNo, pageSize, role);
                    ArrayList<ProductDTO> listProducts = dao.getListSearchedProducts();

                    if (listProducts != null) {
                        request.setAttribute("pageNo", pageNo);
                        request.setAttribute("listProducts", listProducts);
                        if (((pageSize * pageNo) >= totalProducts) || listProducts.size() < pageSize || totalSearchedProducts == pageSize) {
                            request.setAttribute("isLastList", "TRUE");
                        }
                    }

                    url = HOME_PAGE;
                }
            }

            cateDAO.getAllCategories();
            ArrayList<CategoryDTO> categoryList = cateDAO.getListCategories();
            request.setAttribute("categoryList", categoryList);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at SearchProductController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at SearchProductController _ SQLException : " + msg);
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at SearchProductController _ ParseException : " + msg);
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
