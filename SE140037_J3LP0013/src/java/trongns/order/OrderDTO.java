/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import trongns.orderdetails.OrderDetailsDTO;

/**
 *
 * @author TrongNS
 */
public class OrderDTO implements Serializable {

    private String id;
    private String userId;
    private String customerName;
    private String address;
    private String phone;
    private Date createdDate;
    private String paymentMethod;
    private BigDecimal deliveryFee;
    private ArrayList<OrderDetailsDTO> orderItems;
    private BigDecimal totalPrice;
    private String content;

    public OrderDTO() {
    }

    public OrderDTO(String id, String userId, String customerName, String address, String phone, Date createdDate, String paymentMethod, BigDecimal deliveryFee, ArrayList<OrderDetailsDTO> orderItems, BigDecimal totalPrice, String content) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.createdDate = createdDate;
        this.paymentMethod = paymentMethod;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.content = content;
    }

    public OrderDTO(String id, String userId, String customerName, String address, String phone, Date createdDate, String paymentMethod, BigDecimal deliveryFee, BigDecimal totalPrice, String content) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.createdDate = createdDate;
        this.paymentMethod = paymentMethod;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.content = content;
    }

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the deliveryFee
     */
    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * @param deliveryFee the deliveryFee to set
     */
    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    /**
     * @return the orderItems
     */
    public ArrayList<OrderDetailsDTO> getOrderItems() {
        return orderItems;
    }

    /**
     * @param orderItems the orderItems to set
     */
    public void setOrderItems(ArrayList<OrderDetailsDTO> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderDTO other = (OrderDTO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
