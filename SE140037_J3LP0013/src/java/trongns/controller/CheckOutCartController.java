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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.cart.CartObj;
import trongns.order.OrderDAO;
import trongns.order.OrderDTO;
import trongns.orderdetails.OrderDetailsDAO;
import trongns.orderdetails.OrderDetailsDTO;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "CheckOutCartController", urlPatterns = {"/CheckOutCartController"})
public class CheckOutCartController extends HttpServlet {

    private final String LOAD_HOME_CONTROLLER = "LoadHomeController";
    private final String CHECK_OUT_PAGE = "checkOut.jsp";

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

        boolean valid = true;

        HttpSession session = request.getSession(false);
        boolean isAdmin = false;
        try {
            if (session != null) {
                String user = (String) session.getAttribute("USER");
                if (user != null) {
                    String admin = (String) session.getAttribute("ROLE");
                    if ("Admin".equals(admin)) {
                        isAdmin = true;
                        response.sendRedirect(url);
                        return;
                    }

                    url = CHECK_OUT_PAGE;

                    CartObj cart = (CartObj) session.getAttribute("CART");
                    if (cart != null) {
                        HashMap<ProductDTO, Integer> items = cart.getItems();
                        if (items != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date orderedDate = new Date();
                            orderedDate = dateFormat.parse(dateFormat.format(orderedDate));

                            String customerName = request.getParameter("txtCustomerName");
                            String address = request.getParameter("txtAddress");
                            String phoneNumber = request.getParameter("txtPhoneNumber");
                            String note = request.getParameter("txtNote");
                            String method = request.getParameter("rdMethod");

                            if (valid) {
                                String orderId = null, name, imageLink, id;
                                int quantity, orderingAmount, orderedAmount;
                                BigDecimal price;
                                BigDecimal totalPrice = BigDecimal.ZERO;
                                BigDecimal shippingFee = BigDecimal.valueOf(2);
                                ArrayList<OrderDetailsDTO> orderedList = new ArrayList<>();

                                OrderDetailsDAO dao = new OrderDetailsDAO();
                                boolean isFirst = true;
                                String warningOutOfStock = null;
                                int remainingAmount;

                                OrderDAO orderDAO = new OrderDAO();

                                for (ProductDTO dto : items.keySet()) {
                                    id = dto.getId();
                                    name = dto.getName();
                                    imageLink = dto.getImageLink();
                                    price = dto.getPrice();
                                    orderingAmount = items.get(dto);

                                    orderedAmount = dao.getAmountOrderedProduct();
                                    quantity = dto.getQuantity();
                                    remainingAmount = quantity - orderedAmount;

                                    price = price.multiply(new BigDecimal(orderingAmount));
                                    totalPrice = totalPrice.add(price);

                                    if (orderingAmount > remainingAmount) {
                                        if (isFirst) {
                                            warningOutOfStock = "Out of stock: " + name + " (" + remainingAmount + " left)";
                                        } else {
                                            warningOutOfStock += ", " + name + " (" + remainingAmount + " left)";
                                        }
                                    }

                                    if (orderId == null) {
                                        orderId = createOrderId(14);
                                        try {
                                            boolean duplicated = orderDAO.checkDuplicatedOrderId(orderId);
                                            while (duplicated) {
                                                orderId = createOrderId(14);
                                                duplicated = orderDAO.checkDuplicatedOrderId(orderId);
                                            }
                                        } catch (NamingException ex) {
                                            ex.printStackTrace();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                    OrderDetailsDTO details = new OrderDetailsDTO(orderId, id, name, orderingAmount, price);
                                    details.setImageLink(imageLink);
                                    orderedList.add(details);
                                }

                                if (warningOutOfStock != null) {
                                    valid = false;
                                    request.setAttribute("outOfStock", warningOutOfStock);
                                }

                                totalPrice = totalPrice.add(shippingFee);
                                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

                                OrderDTO order = new OrderDTO();
                                order.setId(orderId);
                                order.setUserId(user);
                                order.setCustomerName(customerName);
                                order.setAddress(address);
                                order.setPhone(phoneNumber);
                                order.setCreatedDate(orderedDate);
                                order.setPaymentMethod(method);
                                order.setDeliveryFee(shippingFee);
                                order.setOrderItems(orderedList);
                                order.setTotalPrice(totalPrice);
                                order.setContent(note);

                                if (valid) {
                                    session.setAttribute("ORDER_INFO", order);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            String msg = ex.getMessage();
            log("Error at CheckOutCartController _ IOException : " + msg);
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at CheckOutCartController _ ParseException : " + msg);
        } finally {
            if (!valid) {
                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }
            if (!isAdmin) {
                request.getRequestDispatcher(url).forward(request, response);
            }
            out.close();
        }
    }

    private String createOrderId(int size) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String date = dateFormat.format(now);

        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size - 6; i++) {
            int index = (int) (letters.length() * Math.random());
            sb.append(letters.charAt(index));
        }

        return date + sb.toString();
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
