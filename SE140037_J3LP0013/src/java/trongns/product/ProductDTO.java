/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author TrongNS
 */
public class ProductDTO implements Serializable {
    private String id;
    private String name;
    private String imageLink;
    private int quantity;
    private String description;
    private BigDecimal price;
    private String category;
    private Date createdDate;
    private String createdUserId;
    private Date updatedDate;
    private String updatedUserId;
    private boolean active;

    public ProductDTO() {
    }
    
    public ProductDTO(String id, String name, String imageLink, int quantity, String description, BigDecimal price, String category) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the createdUserId
     */
    public String getCreatedUserId() {
        return createdUserId;
    }

    /**
     * @param createdUserId the createdUserId to set
     */
    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    /**
     * @return the updatedUserId
     */
    public String getUpdatedUserId() {
        return updatedUserId;
    }

    /**
     * @param updatedUserId the updatedUserId to set
     */
    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
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
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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
    
    @Override
    public int hashCode() {
        int hash = 3;
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
        final ProductDTO other = (ProductDTO) obj;
        if (!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }
}
