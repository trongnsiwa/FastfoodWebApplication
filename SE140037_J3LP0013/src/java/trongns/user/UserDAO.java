/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import trongns.utilities.MyConnection;

/**
 *
 * @author TrongNS
 */
public class UserDAO implements Serializable {

    private UserDTO checkedLoginUser = null;

    /**
     * @return the checkedLoginUser
     */
    public UserDTO getCheckedLoginUser() {
        return checkedLoginUser;
    }

    public void checkLogin(String username, String password) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Firstname, Lastname, IsAdmin "
                        + "FROM [User] "
                        + "WHERE Id = ? AND Password = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);

                rs = stm.executeQuery();
                
                String firstname, lastname, role;
                if (rs.next()) {
                    firstname = rs.getString("Firstname");
                    lastname = rs.getString("Lastname");
                    boolean isAdmin = rs.getBoolean("IsAdmin");
                    if (isAdmin) {
                        role = "Admin";
                    } else {
                        role = "Customer";
                    }
                    this.checkedLoginUser = new UserDTO(username, password, firstname, lastname, role);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean registerAccountViaGoogle(String firstname, String lastname, String googleId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO [User](Id, Firstname, Lastname, IsAdmin, GoogleId) "
                        + "VALUES(?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, googleId);
                stm.setString(2, firstname);
                stm.setString(3, lastname);
                stm.setBoolean(4, false);
                stm.setString(5, googleId);

                int row = stm.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
    
    private UserDTO checkedLoginGoogleUser = null;
    
    public UserDTO getCheckedLoginGoogleUser() {
        return checkedLoginGoogleUser;
    }
    
    public void checkLoginViaGoogle(String googleId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Firstname, Lastname, IsAdmin, GoogleId "
                        + "FROM [User] "
                        + "WHERE GoogleId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, googleId);

                rs = stm.executeQuery();
                
                String firstname, lastname, role;
                if (rs.next()) {
                    firstname = rs.getString("Firstname");
                    lastname = rs.getString("Lastname");
                    boolean isAdmin = rs.getBoolean("IsAdmin");
                    if (isAdmin) {
                        role = "Admin";
                    } else {
                        role = "Customer";
                    }
                    googleId = rs.getString("GoogleId");
                    this.checkedLoginGoogleUser = new UserDTO(googleId, lastname, firstname, lastname, role, googleId);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
