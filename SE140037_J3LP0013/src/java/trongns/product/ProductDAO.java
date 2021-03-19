/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import trongns.utilities.MyConnection;

/**
 *
 * @author TrongNS
 */
public class ProductDAO implements Serializable {

    private int totalProducts = 0;

    /**
     * @return the totalActiveProducts
     */
    public int getTotalProducts() {
        return totalProducts;
    }

    public void countAllProducts(String role) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(Id) AS total "
                        + "FROM Product ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Status = ? AND quantity > 0";
                }
                stm = con.prepareStatement(sql);
                if (!"Admin".equals(role)) {
                    stm.setBoolean(1, true);
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalProducts = rs.getInt("total");
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

    private int totalSearchedProducts = 0;

    /**
     * @return the totalSearchedProducts
     */
    public int getTotalSearchedProducts() {
        return totalSearchedProducts;
    }

    public void countProductsWithConditions(String searchProduct, BigDecimal startMoneyRange, BigDecimal endMoneyRange, String ctgory, String role) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(Id) AS total "
                        + "FROM Product ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Status = ? AND quantity > 0 ";
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        sql += "AND (Name LIKE ? ";
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "OR Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price <= " + endMoneyRange + " ";
                        }
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            sql += "OR Category = ? ";
                        }
                        sql += ") ";
                    } else {
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "AND (Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "AND (Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "AND (Price <= " + endMoneyRange + " ";
                        } else {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "AND Category = ? ";
                            }
                        }

                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 || endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "OR Category = ? ) ";
                            } else {
                                sql += ") ";
                            }
                        }
                    }
                } else {
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        sql += "WHERE Name LIKE ? ";
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "OR Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price <= " + endMoneyRange + " ";
                        }
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            sql += "OR Category = ? ";
                        }
                    } else {
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "WHERE Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "WHERE Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "WHERE Price <= " + endMoneyRange + " ";
                        } else {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "WHERE Category = ? ";
                            }
                        }

                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 || endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "OR Category = ? ";
                            }
                        }
                    }
                }
                stm = con.prepareStatement(sql);
                if (!"Admin".equals(role)) {
                    stm.setBoolean(1, true);
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        stm.setString(2, "%" + searchProduct + "%");
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(3, ctgory);
                        }
                    } else {
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(2, ctgory);
                        }
                    }
                } else {
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        stm.setString(1, "%" + searchProduct + "%");
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(2, ctgory);
                        }
                    } else {
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(1, ctgory);
                        }
                    }
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalSearchedProducts = rs.getInt("total");
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

    private ArrayList<ProductDTO> listHomeProducts = null;

    /**
     * @return the listHomeProducts
     */
    public ArrayList<ProductDTO> getListHomeProducts() {
        return listHomeProducts;
    }

    public void getListFirstProducts(int size, String role) throws NamingException, SQLException, ParseException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT TOP(" + size + ") Id, Name, ImageLink, Quantity, Description, Price, Category, CreatedDate, Status "
                        + "FROM Product ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Status = ? AND quantity > 0";
                }
                sql += "ORDER BY CreatedDate DESC";
                stm = con.prepareStatement(sql);
                if (!"Admin".equals(role)) {
                    stm.setBoolean(1, true);
                }
                rs = stm.executeQuery();

                String id;
                String name, imageLink, description, category;
                boolean status;
                int quantity;
                BigDecimal price;
                Date createdDate;

                while (rs.next()) {
                    status = rs.getBoolean("Status");
                    quantity = rs.getInt("Quantity");
                    id = rs.getString("Id");
                    name = rs.getString("Name");
                    imageLink = rs.getString("ImageLink");
                    description = rs.getString("Description");
                    price = rs.getBigDecimal("Price");
                    category = rs.getString("Category");
                    createdDate = rs.getTimestamp("CreatedDate");

                    if (this.listHomeProducts == null) {
                        this.listHomeProducts = new ArrayList<>();
                    }

                    ProductDTO product = new ProductDTO(id, name, imageLink, quantity, description, price, category);
                    product.setCreatedDate(createdDate);
                    product.setActive(status);
                    this.listHomeProducts.add(product);
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

    private ArrayList<ProductDTO> listSearchedProducts = null;

    /**
     * @return the listSearchedProducts
     */
    public ArrayList<ProductDTO> getListSearchedProducts() {
        return listSearchedProducts;
    }

    public void searchProductsWithConditions(String searchProduct, BigDecimal startMoneyRange, BigDecimal endMoneyRange, String ctgory, int pageNo, int pageSize, String role) throws SQLException, NamingException, ParseException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int offset = pageSize * (pageNo - 1);

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Id, Name, ImageLink, Quantity, Description, Price, Category, CreatedDate, Status "
                        + "FROM Product ";
                if (!"Admin".equals(role)) {
                    sql += "WHERE Status = ? AND quantity > 0 ";
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        sql += "AND (Name LIKE ? ";
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "OR Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price <= " + endMoneyRange + " ";
                        }
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            sql += "OR Category = ? ";
                        }
                        sql += ") ";
                    } else {
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "AND (Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "AND (Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "AND (Price <= " + endMoneyRange + " ";
                        } else {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "AND Category = ? ";
                            }
                        }

                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 || endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "OR Category = ? ) ";
                            } else {
                                sql += ") ";
                            }
                        }
                    }
                } else {
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        sql += "WHERE Name LIKE ? ";
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "OR Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "OR Price <= " + endMoneyRange + " ";
                        }
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            sql += "OR Category = ? ";
                        }
                    } else {
                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "WHERE Price BETWEEN " + startMoneyRange + " AND " + endMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 && endMoneyRange.compareTo(BigDecimal.ZERO) != 1) {
                            sql += "WHERE Price >= " + startMoneyRange + " ";
                        } else if (startMoneyRange.compareTo(BigDecimal.ZERO) != 1 && endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            sql += "WHERE Price <= " + endMoneyRange + " ";
                        } else {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "WHERE Category = ? ";
                            }
                        }

                        if (startMoneyRange.compareTo(BigDecimal.ZERO) == 1 || endMoneyRange.compareTo(BigDecimal.ZERO) == 1) {
                            if (ctgory != null && !ctgory.trim().isEmpty()) {
                                sql += "OR Category = ? ";
                            }
                        }
                    }
                }

                sql += "ORDER BY CreatedDate DESC "
                        + "OFFSET " + offset + " ROWS "
                        + "FETCH NEXT " + pageSize + " ROWS ONLY";

                stm = con.prepareStatement(sql);
                if (!"Admin".equals(role)) {
                    stm.setBoolean(1, true);
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        stm.setString(2, "%" + searchProduct + "%");
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(3, ctgory);
                        }
                    } else {
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(2, ctgory);
                        }
                    }
                } else {
                    if (searchProduct != null && !searchProduct.trim().isEmpty()) {
                        stm.setString(1, "%" + searchProduct + "%");
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(2, ctgory);
                        }
                    } else {
                        if (ctgory != null && !ctgory.trim().isEmpty()) {
                            stm.setString(1, ctgory);
                        }
                    }
                }

                rs = stm.executeQuery();

                String id;
                String name, imageLink, description, category;
                boolean status;
                int quantity;
                BigDecimal price;
                Date createdDate;

                while (rs.next()) {
                    status = rs.getBoolean("Status");
                    quantity = rs.getInt("Quantity");
                    id = rs.getString("Id");
                    name = rs.getString("Name");
                    imageLink = rs.getString("ImageLink");
                    description = rs.getString("Description");
                    price = rs.getBigDecimal("Price");
                    category = rs.getString("Category");
                    createdDate = rs.getTimestamp("CreatedDate");

                    if (this.listSearchedProducts == null) {
                        this.listSearchedProducts = new ArrayList<>();
                    }

                    ProductDTO product = new ProductDTO(id, name, imageLink, quantity, description, price, category);
                    product.setActive(status);
                    product.setCreatedDate(createdDate);
                    this.listSearchedProducts.add(product);
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

    public boolean deleteProductById(String id, String updatedUser) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE Product "
                        + "SET Status = ?, UpdatedDate = GETDATE(), UpdatedUserId = ? "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setBoolean(1, false);
                stm.setString(2, updatedUser);
                stm.setString(3, id);

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

    public boolean updateProductById(ProductDTO dto) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE Product "
                        + "SET Name = ?, ImageLink = ?, Quantity = ?, Description = ?, Price = ?, Category = ?, UpdatedDate = GETDATE(), UpdatedUserId = ?, Status = ? "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getName());
                stm.setString(2, dto.getImageLink());
                stm.setInt(3, dto.getQuantity());
                stm.setString(4, dto.getDescription());
                stm.setBigDecimal(5, dto.getPrice());
                stm.setString(6, dto.getCategory());
                stm.setString(7, dto.getUpdatedUserId());
                stm.setBoolean(8, dto.isActive());
                stm.setString(9, dto.getId());

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

    public boolean insertProduct(ProductDTO dto) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO Product (Id, Name, ImageLink, Quantity, Description, Price, Category, CreatedDate, CreatedUserId, Status) "
                        + "VALUES (?,?,?,?,?,?,?,GETDATE(),?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getId());
                stm.setString(2, dto.getName());
                stm.setString(3, dto.getImageLink());
                stm.setInt(4, dto.getQuantity());
                stm.setString(5, dto.getDescription());
                stm.setBigDecimal(6, dto.getPrice());
                stm.setString(7, dto.getCategory());
                stm.setString(8, dto.getCreatedUserId());
                stm.setBoolean(9, dto.isActive());

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

    public boolean checkDuplicateProductId(String id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Name "
                        + "FROM Product "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, id);

                rs = stm.executeQuery();

                if (rs.next()) {
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

    private ProductDTO updatedProduct = null;

    /**
     * @return the updatedProduct
     */
    public ProductDTO getUpdatedProduct() {
        return updatedProduct;
    }

    public void getUpdatedProductById(String id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Name, ImageLink, Quantity, Description, Price, Category, CreatedDate, Status "
                        + "FROM Product "
                        + "WHERE Id = ?";

                stm = con.prepareStatement(sql);
                stm.setString(1, id);

                rs = stm.executeQuery();

                String name, imageLink, description, category;
                int quantity;
                BigDecimal price;
                Date createdDate;
                boolean status;

                if (rs.next()) {
                    status = rs.getBoolean("Status");
                    quantity = rs.getInt("Quantity");
                    name = rs.getString("Name");
                    imageLink = rs.getString("ImageLink");
                    description = rs.getString("Description");
                    price = rs.getBigDecimal("Price");
                    category = rs.getString("Category");
                    createdDate = rs.getTimestamp("CreatedDate");

                    ProductDTO product = new ProductDTO(id, name, imageLink, quantity, description, price, category);
                    product.setActive(status);
                    product.setCreatedDate(createdDate);
                    this.updatedProduct = product;
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

    private int oldQuantity = 0;

    /**
     * @return the oldQuantity
     */
    public int getOldQuantity() {
        return oldQuantity;
    }

    public void getOldQuantityOfProductById(String id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT Quantity "
                        + "FROM Product "
                        + "WHERE Id = ?";

                stm = con.prepareStatement(sql);
                stm.setString(1, id);

                rs = stm.executeQuery();

                if (rs.next()) {
                    this.oldQuantity = rs.getInt("Quantity");
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

    public boolean updateQuantityOfProductById(String id, int quantity) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE Product "
                        + "SET Quantity = ? "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, quantity);
                stm.setString(2, id);
                
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

}
