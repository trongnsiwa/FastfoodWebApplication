/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.category;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import trongns.category.CategoryDTO;
import trongns.utilities.MyConnection;

/**
 *
 * @author TrongNS
 */
public class CategoryDAO implements Serializable {

    private ArrayList<CategoryDTO> listCategories = null;

    /**
     * @return the listCategories
     */
    public ArrayList<CategoryDTO> getListCategories() {
        return listCategories;
    }

    public void getAllCategories() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Id, Name "
                        + "FROM Category";
                stm = con.prepareStatement(sql);

                rs = stm.executeQuery();

                while (rs.next()) {
                    if (this.listCategories == null) {
                        this.listCategories = new ArrayList<>();
                    }
                    
                    this.listCategories.add(new CategoryDTO(rs.getString("Id"), rs.getString("Name")));
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
    
    private String category = null;

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    
    public void getCategoryById(int id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Name "
                        + "FROM Category "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();

                if (rs.next()) {
                    this.category = rs.getString("Name");
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
