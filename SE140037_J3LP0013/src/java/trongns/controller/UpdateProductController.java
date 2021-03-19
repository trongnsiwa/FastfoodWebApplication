/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import trongns.product.ProductDAO;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "UpdateProductController", urlPatterns = {"/UpdateProductController"})
public class UpdateProductController extends HttpServlet {

    private final String LOAD_UPDATED_PRODUCT_CONTROLLER = "LoadUpdatedProductController";
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
    @SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String url = LOAD_HOME_CONTROLLER;

        boolean valid = true;
        ProductDAO dao = new ProductDAO();
        String msg = null;
        boolean isAdmin = false;
        boolean havaParams = true;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String user = (String) session.getAttribute("USER");
                if (user != null) {
                    String admin = (String) session.getAttribute("ROLE");
                    if ("Admin".equals(admin)) {
                        isAdmin = true;
                        url = LOAD_UPDATED_PRODUCT_CONTROLLER;
                        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                        if (!isMultipart) {

                        } else {
                            String[] validImageFileExt = {".jpg", ".jpeg", ".bmp", ".gif", ".png", ".tiff"};

                            FileItemFactory factory = new DiskFileItemFactory();
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            List<FileItem> items = null;
                            Hashtable params = new Hashtable();
                            String fileName = null;
                            boolean notUpload = false;

                            try {
                                items = upload.parseRequest(request);
                                for (FileItem item : items) {
                                    if (item.isFormField()) {
                                        params.put(item.getFieldName(), item.getString());
                                    } else {
                                        fileName = item.getName().substring(item.getName().lastIndexOf("\\") + 1);

                                        if (fileName == null || fileName.trim().isEmpty()) {
                                            notUpload = true;
                                        } else {
                                            boolean validImgExt = false;
                                            for (int i = 0; i < validImageFileExt.length; i++) {
                                                String currentExt = validImageFileExt[i];
                                                if (fileName.substring(fileName.length() - currentExt.length(), fileName.length()).toLowerCase().equals(currentExt)) {
                                                    validImgExt = true;
                                                    break;
                                                }
                                            }

                                            if (!validImgExt) {
                                                valid = false;
                                                msg = "Invalid image link. Accept only image file.";
                                            } else {
                                                try {
                                                    String realPath = getServletContext().getRealPath("/").replace("\\build", "") + "image\\" + fileName;
                                                    File savedFile = new File(realPath);
                                                    item.write(savedFile);
                                                } catch (Exception ex) {
                                                    log("Error at UpdateProductController _ Exception : " + ex.getMessage());
                                                    valid = false;
                                                    msg = "Cannot upload image file " + fileName;
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (FileUploadException ex) {
                                log("Error at UpdateProductController _ FileUploadException : " + ex.getMessage());
                                valid = false;
                                msg = "Cannot upload image file " + fileName;
                            }

                            String imageLink = null;
                            if (notUpload) {
                                fileName = (String) params.get("txtImageLink");
                                if (fileName != null && !fileName.trim().isEmpty()) {
                                    boolean validImgExt = false;
                                    for (int i = 0; i < validImageFileExt.length; i++) {
                                        String currentExt = validImageFileExt[i];
                                        if (fileName.length() >= currentExt.length()) {
                                            if (fileName.substring(fileName.length() - currentExt.length(), fileName.length()).toLowerCase().equals(currentExt)) {
                                                validImgExt = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (validImgExt) {
                                        imageLink = fileName;
                                    } else {
                                        valid = false;
                                        msg = "Invalid image link. Accept only image file";
                                    }
                                }
                            } else {
                                imageLink = "/image/" + fileName;
                            }

                            if (params.size() > 0) {
                                String productId = (String) params.get("txtProductId");
                                request.setAttribute("txtProductId", productId);

                                if (valid) {
                                    String productName = (String) params.get("txtProductName");
                                    String description = (String) params.get("txtDescription");
                                    String squantity = (String) params.get("txtQuantity");
                                    String sprice = (String) params.get("txtPrice");
                                    String category = (String) params.get("cbCtgory");
                                    String status = (String) params.get("cbActive");

                                    int quantity = Integer.parseInt(squantity.trim());
                                    BigDecimal price = new BigDecimal(sprice);

                                    boolean active = false;
                                    if ("Active".equals(status)) {
                                        active = true;
                                    }

                                    ProductDTO dto = new ProductDTO(productId, productName, imageLink, quantity, description, price, category);
                                    dto.setUpdatedUserId(user);
                                    dto.setActive(active);

                                    boolean result = dao.updateProductById(dto);

                                    if (result) {
                                        request.setAttribute("updateProductMsg", "Update product " + productId + " successfully");
                                    } else {
                                        request.setAttribute("updateProductMsg", "Update product " + productId + " fail");
                                    }
                                }
                                String searchProduct = (String) params.get("txtSearchProduct");
                                String pageNo = (String) params.get("pageNo");
                                String category = (String) params.get("cbCategory");
                                String fromPrice = (String) params.get("txtFromPrice");
                                String toPrice = (String) params.get("txtToPrice");

                                request.setAttribute("txtSearchProduct", searchProduct);
                                if (pageNo != null && !pageNo.trim().isEmpty()) {
                                    request.setAttribute("pageNo", pageNo);
                                }
                                request.setAttribute("cbCategory", category);
                                request.setAttribute("txtFromPrice", fromPrice);
                                request.setAttribute("txtToPrice", toPrice);
                            }
                        }
                    } else {
                        havaParams = false;
                    }
                }
            }
        } catch (NamingException ex) {
            String msgErr = ex.getMessage();
            log("Error at UpdateProductController _ NamingException : " + msgErr);
        } catch (SQLException ex) {
            String msgErr = ex.getMessage();
            log("Error at UpdateProductController _ SQLException : " + msgErr);
        } finally {
            if (!isAdmin || !havaParams) {
                response.sendRedirect(url);
            } else {
                if (!valid) {
                    request.setAttribute("invalidImageLink", msg);
                }
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
