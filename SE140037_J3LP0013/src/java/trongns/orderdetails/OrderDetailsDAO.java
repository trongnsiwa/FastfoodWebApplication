/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.orderdetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import trongns.order.OrderDTO;
import trongns.utilities.MyConnection;

/**
 *
 * @author TrongNS
 */
public class OrderDetailsDAO implements Serializable {

    private int amountOrderedProduct = 0;

    /**
     * @return the amountOrderedProduct
     */
    public int getAmountOrderedProduct() {
        return amountOrderedProduct;
    }

    public void getAmountOrderedProductById(String id) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT SUM(Amount) as SumAmount"
                        + "FROM OrderDetails "
                        + "WHERE ProductId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, id);

                rs = stm.executeQuery();
                if (rs.next()) {
                    this.amountOrderedProduct = rs.getInt("SumAmount");
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

    public boolean insertOrderedProductDetails(OrderDTO order) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        boolean result = false;
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO OrderDetails(OrderId, ProductId, ProductName, ImageLink, Amount, Price) "
                        + "VALUES(?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                for (OrderDetailsDTO detail : order.getOrderItems()) {
                    stm.setString(1, detail.getOrderId());
                    stm.setString(2, detail.getProductId());
                    stm.setString(3, detail.getProductName());
                    stm.setString(4, detail.getImageLink());
                    stm.setInt(5, detail.getAmount());
                    stm.setBigDecimal(6, detail.getPrice());
                    int row = stm.executeUpdate();
                    result = (row > 0);
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
        return result;
    }
    
    private ArrayList<OrderDetailsDTO> listHistoryOrderDetals = null;

    /**
     * @return the listHistoryOrderDetals
     */
    public ArrayList<OrderDetailsDTO> getListHistoryOrderDetals() {
        return listHistoryOrderDetals;
    }
    
    public void loadListOrderDetailByOrderId(String orderId) throws SQLException, NamingException {
        this.listHistoryOrderDetals = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT ProductId, ProductName, ImageLink, Amount, Price "
                        + "FROM [OrderDetails] "
                        + "WHERE OrderId = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);

                rs = stm.executeQuery();

                int amount;
                String productId;
                String productName, imageLink;
                BigDecimal price;
                
                while (rs.next()) {
                    productId = rs.getString("ProductId");
                    productName = rs.getString("ProductName");
                    imageLink = rs.getString("ImageLink");
                    amount = rs.getInt("Amount");
                    price = rs.getBigDecimal("Price");

                    if (this.listHistoryOrderDetals == null) {
                        this.listHistoryOrderDetals = new ArrayList<>();
                    }

                    OrderDetailsDTO dto = new OrderDetailsDTO(orderId, productId, productName, amount, price);
                    dto.setImageLink(imageLink);
                    this.listHistoryOrderDetals.add(dto);
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
