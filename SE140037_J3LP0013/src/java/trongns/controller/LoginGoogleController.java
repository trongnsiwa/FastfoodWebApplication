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
import trongns.google.GooglePOJO;
import trongns.user.UserDAO;
import trongns.user.UserDTO;
import trongns.utilities.APIWrapper;

/**
 *
 * @author TrongNS
 */
@WebServlet(name = "LoginGoogleController", urlPatterns = {"/LoginGoogleController"})
public class LoginGoogleController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final String LOGIN_PAGE = "login.jsp";
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

        String code = request.getParameter("code");
        String url = LOGIN_PAGE;
        boolean haveParams = true;
        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                haveParams = false;
                response.sendRedirect(url);
                return;
            }

            if (code != null && !code.trim().isEmpty()) {
                url = LOAD_HOME_CONTROLLER;
                APIWrapper wrapper = new APIWrapper();
                String accessToken = wrapper.getAccessToken(code);
                GooglePOJO googlePojo = wrapper.getUserDTO(accessToken);

                if (googlePojo != null) {
                    String googleId = googlePojo.getId();
                    String firstname = googlePojo.getGiven_name();
                    String lastname = googlePojo.getFamily_name();

                    UserDAO dao = new UserDAO();
                    dao.checkLoginViaGoogle(googleId);
                    UserDTO dto = dao.getCheckedLoginGoogleUser();
                    
                    HttpSession session = request.getSession();
                    if (dto == null) {
                        dao.registerAccountViaGoogle(firstname, lastname, googleId);
                        session.setAttribute("USER", googleId);
                        session.setAttribute("FULLNAME", firstname + lastname);
                        session.setAttribute("ROLE", "Customer");
                    } else {
                        session.setAttribute("USER", dto.getUsername());
                        session.setAttribute("FULLNAME", dto.getFirstname() + " " + dto.getLastname());
                        session.setAttribute("ROLE", dto.getRole());
                    }
                }
            }
        } catch (IOException ex) {
            String msg = ex.getMessage();
            log("Error at LoginGoogleController _ IOException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoginGoogleController _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoginGoogleController _ SQLException : " + msg);
        } finally {
            if (haveParams) {
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
