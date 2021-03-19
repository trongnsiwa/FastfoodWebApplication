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
import java.util.ArrayList;
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
import trongns.category.CategoryDAO;
import trongns.category.CategoryDTO;
import trongns.product.CreateNewProductError;
import trongns.product.ProductDAO;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "CreateNewProductController", urlPatterns = {"/CreateNewProductController"})
public class CreateNewProductController extends HttpServlet {

    private final String CREATE_NEW_PRODUCT_PAGE = "createNewProduct.jsp";
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
        CreateNewProductError error = new CreateNewProductError();
        boolean isAdmin = false;
        boolean haveParams = true;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String user = (String) session.getAttribute("USER");
                if (user != null) {
                    String role = (String) session.getAttribute("ROLE");
                    if ("Admin".equals(role)) {
                        isAdmin = true;
                        url = CREATE_NEW_PRODUCT_PAGE;
                        boolean isMultiPath = ServletFileUpload.isMultipartContent(request);
                        if (!isMultiPath) {

                        } else {
                            String[] validImageFileExt = {".jpg", ".jpeg", ".bmp", ".gif", ".png"};

                            FileItemFactory factory = new DiskFileItemFactory();
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            Hashtable params = new Hashtable();
                            List<FileItem> items = null;
                            String fileName = null;
                            boolean notUpload = false;
                            String productId = null;

                            ProductDAO dao = new ProductDAO();
                            try {
                                items = upload.parseRequest(request);
                                for (FileItem item : items) {
                                    if (item.isFormField()) {
                                        params.put(item.getFieldName(), item.getString());
                                    } else {
                                        productId = (String) params.get("txtProductId");

                                        if (productId != null) {
                                            boolean isDuplicate = dao.checkDuplicateProductId(productId);
                                            if (isDuplicate) {
                                                valid = false;
                                                error.setDuplicateProductId("Duplicate product Id for " + productId);
                                            }
                                        }

                                        if (valid) {
                                            String itemName = item.getName();
                                            fileName = itemName.substring(itemName.lastIndexOf("\\") + 1);

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
                                                    error.setInvalidImageLink("Invalid image link. Accept only image file.");
                                                } else {
                                                    try {
                                                        String realPath = getServletContext().getRealPath("/").replace("\\build", "") + "image\\" + fileName;
                                                        File savedFile = new File(realPath);
                                                        item.write(savedFile);
                                                    } catch (Exception ex) {
                                                        log("Error at CreateNewProductController _ Exception : " + ex.getMessage());
                                                        valid = false;
                                                        error.setInvalidImageLink("Cannot upload image file " + fileName);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (FileUploadException ex) {
                                log("Error at CreateNewProductController _ FileUploadException : " + ex.getMessage());
                                valid = false;
                                error.setInvalidImageLink("Cannot upload image file " + fileName);
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
                                        error.setInvalidImageLink("Invalid image link. Accept only image file");
                                    }
                                }
                            } else {
                                imageLink = "/image/" + fileName;
                            }

                            if (params.size() <= 0) {
                                haveParams = false;
                            } else {
                                String productName = (String) params.get("txtProductName");
                                String squantity = (String) params.get("txtQuantity");
                                String description = (String) params.get("txtDescription");
                                String sprice = (String) params.get("txtPrice");
                                String category = (String) params.get("cbCategory");

                                if (valid) {
                                    int quantity = Integer.parseInt(squantity.trim());
                                    BigDecimal price = new BigDecimal(sprice);

                                    ProductDTO dto = new ProductDTO(productId, productName, imageLink, quantity, description, price, category);
                                    dto.setActive(true);
                                    dto.setCreatedUserId(user);

                                    boolean result = dao.insertProduct(dto);

                                    if (result) {
                                        url = LOAD_HOME_CONTROLLER;
                                        request.setAttribute("createProductMsg", "Create new product succesfully");
                                    } else {
                                        request.setAttribute("createProductMsg", "Create new product fail");
                                        request.setAttribute("txtProductId", productId);
                                        request.setAttribute("txtProductName", productName);
                                        request.setAttribute("txtImageLink", imageLink);
                                        request.setAttribute("txtPrice", sprice);
                                        request.setAttribute("txtQuantity", squantity);
                                        request.setAttribute("txtDescription", description);
                                        request.setAttribute("cbCategory", category);
                                    }
                                } else {
                                    request.setAttribute("txtProductId", productId);
                                    request.setAttribute("txtProductName", productName);
                                    request.setAttribute("txtImageLink", imageLink);
                                    request.setAttribute("txtPrice", sprice);
                                    request.setAttribute("txtQuantity", squantity);
                                    request.setAttribute("txtDescription", description);
                                    request.setAttribute("cbCategory", category);
                                }
                            }
                        }
                        CategoryDAO cateDao = new CategoryDAO();
                        cateDao.getAllCategories();
                        ArrayList<CategoryDTO> listCategories = cateDao.getListCategories();
                        request.setAttribute("categoryList", listCategories);
                    }
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at CreateNewProductController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at CreateNewProductController _ SQLException : " + msg);
        } finally {
            if (!isAdmin || !haveParams) {
                response.sendRedirect(url);
            } else {
                if (!valid) {
                    request.setAttribute("createProductErrors", error);
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
