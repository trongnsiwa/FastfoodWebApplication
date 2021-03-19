/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.naming.NamingException;
import trongns.utilities.MyConnection;

/**
 *
 * @author TrongNS
 */
public class OrderDAO implements Serializable {

    public boolean insertOrderedProducts(OrderDTO order) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO [Order](Id, UserId, CustomerName, Address, Phone, CreatedDate, PaymentMethod, DeliveryFee, TotalPrice, [Content]) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, order.getId());
                stm.setString(2, order.getUserId());
                stm.setString(3, order.getCustomerName());
                stm.setString(4, order.getAddress());
                stm.setString(5, order.getPhone());
                stm.setTimestamp(6, new Timestamp(order.getCreatedDate().getTime()));
                stm.setString(7, order.getPaymentMethod());
                stm.setBigDecimal(8, order.getDeliveryFee());
                stm.setBigDecimal(9, order.getTotalPrice());
                stm.setString(10, order.getContent());

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

    private ArrayList<OrderDTO> listSearchedOrders = null;

    /**
     * @return the listSearchedOrders
     */
    public ArrayList<OrderDTO> getListSearchedOrders() {
        return listSearchedOrders;
    }

    public void searchOrdersWithConditions(String userId, int pageSize, String keyword, int pageNo, int day, int month, int year) throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        int offset = pageSize * (pageNo - 1);

        try {
            boolean haveKeyword = false;
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT DISTINCT o.Id, o.CustomerName, o.Address, o.Phone, o.CreatedDate, o.PaymentMethod, o.DeliveryFee, o.TotalPrice, o.[Content] "
                        + "FROM [Order] AS o ";
                if (keyword != null && !keyword.trim().isEmpty()) {
                    sql += "FULL OUTER JOIN [OrderDetails] AS od "
                            + "ON o.Id = od.OrderId ";
                    haveKeyword = true;
                }
                sql += "WHERE o.UserId = ? ";
                if (day != 0) {
                    sql += "AND DAY(o.CreatedDate) = " + day + " ";
                }
                if (month != 0) {
                    sql += "AND MONTH(o.CreatedDate) = " + month + " ";
                }
                if (year != 0) {
                    sql += "AND YEAR(o.CreatedDate) = " + year + " ";
                }
                if (haveKeyword) {
                    sql += "AND od.ProductName LIKE ? ";
                }
                sql += "ORDER BY o.CreatedDate DESC "
                        + "OFFSET " + offset + " ROWS "
                        + "FETCH NEXT " + pageSize + " ROWS ONLY";

                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                if (haveKeyword) {
                    stm.setString(2, "%" + keyword + "%");
                }

                rs = stm.executeQuery();

                String id, customerName, address, phone, paymentMethod, content;
                Date createdDate;
                BigDecimal deliveryFee, totalPrice;

                while (rs.next()) {
                    id = rs.getString("Id");
                    customerName = rs.getString("CustomerName");
                    address = rs.getString("Address");
                    phone = rs.getString("Phone");
                    createdDate = rs.getTimestamp("CreatedDate");
                    paymentMethod = rs.getString("PaymentMethod");
                    deliveryFee = rs.getBigDecimal("DeliveryFee");
                    totalPrice = rs.getBigDecimal("TotalPrice");
                    content = rs.getString("Content");

                    if (this.listSearchedOrders == null) {
                        this.listSearchedOrders = new ArrayList<>();
                    }

                    OrderDTO order = new OrderDTO(id, userId, customerName, address, phone, createdDate, paymentMethod, deliveryFee, totalPrice, content);
                    if (!this.listSearchedOrders.contains(order)) {
                        this.listSearchedOrders.add(order);
                    }
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

    public boolean checkDuplicatedOrderId(String orderId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT UserId "
                        + "FROM [Order] "
                        + "WHERE Id = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderId);

                rs = stm.executeQuery();

                if (rs.next()) {
                    return true;
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
        return false;
    }

    private int totalOrders = 0;

    /**
     * @return the totalOrders
     */
    public int getTotalOrders() {
        return totalOrders;
    }

    public void countAllOrdersByUserId(String userId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(Id) AS total "
                        + "FROM [Order] "
                        + "WHERE UserId = ? ";
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalOrders = rs.getInt("total");
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

    private int totalSearchedOrders = 0;

    /**
     * @return the totalSearchedOrders
     */
    public int getTotalSearchedOrders() {
        return totalSearchedOrders;
    }

    public void countOrdersWithConditions(String userId, String keyword, int day, int month, int year) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            boolean haveKeyword = false;
            con = MyConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT COUNT(DISTINCT o.Id) AS total "
                        + "FROM [Order] AS o ";
                if (keyword != null && !keyword.trim().isEmpty()) {
                    sql += "FULL OUTER JOIN [OrderDetails] AS od "
                            + "ON o.Id = od.OrderId ";
                    haveKeyword = true;
                }
                sql += "WHERE o.UserId = ? ";
                if (day != 0) {
                    sql += "AND DAY(o.CreatedDate) = " + day + " ";
                }
                if (month != 0) {
                    sql += "AND MONTH(o.CreatedDate) = " + month + " ";
                }
                if (year != 0) {
                    sql += "AND YEAR(o.CreatedDate) = " + year + " ";
                }
                if (haveKeyword) {
                    sql += "AND od.ProductName = ?";
                }
                stm = con.prepareStatement(sql);
                stm.setString(1, userId);
                if(haveKeyword) {
                    stm.setString(2, "%" + keyword + "%");
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    this.totalSearchedOrders = rs.getInt("total");
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
