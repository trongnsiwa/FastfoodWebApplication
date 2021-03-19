/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@WebServlet(name = "AddToCartController", urlPatterns = {"/AddToCartController"})
public class AddToCartController extends HttpServlet {

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
            HttpSession session = request.getSession();
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
            }

            String searchProduct = request.getParameter("txtSearchProduct");
            String currentPage = request.getParameter("pageNo");
            String searchedCategory = request.getParameter("cbCategory");
            String fromPrice = request.getParameter("txtFromPrice");
            String toPrice = request.getParameter("txtToPrice");

            url = "SearchProductController"
                    + "?txtSearchProduct=" + searchProduct;
            if (currentPage != null) {
                url += "&pageNo=" + currentPage;
            }
            url += "&cbCategory=" + searchedCategory
                    + "&txtFromPrice=" + fromPrice
                    + "&txtToPrice=" + toPrice;

            CartObj cart = (CartObj) session.getAttribute("CART");
            if (cart == null) {
                cart = new CartObj();
            }

            String productId = request.getParameter("txtProductId");
            String imageLink = request.getParameter("txtImageLink");
            String productName = request.getParameter("txtProductName");
            String description = request.getParameter("txtDescription");
            String squantity = request.getParameter("txtQuantity");
            String sprice = request.getParameter("txtPrice");
            String screatedDate = request.getParameter("txtCreatedDate");
            String category = request.getParameter("txtCategory");

            int quantity = Integer.parseInt(squantity);
            BigDecimal price = new BigDecimal(sprice);

            ProductDTO product = new ProductDTO(productId, productName, imageLink, quantity, description, price, category);

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdDate = sf.parse(screatedDate);
            product.setCreatedDate(createdDate);

            cart.addProductToCart(product);

            session.setAttribute("CART", cart);

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at AddToCartController _ ParseException : " + msg);
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
