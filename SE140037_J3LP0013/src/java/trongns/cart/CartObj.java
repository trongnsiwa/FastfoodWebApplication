/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.cart;

import java.io.Serializable;
import java.util.HashMap;
import trongns.product.ProductDTO;

/**
 *
 * @author TrongNS
 */
public class CartObj implements Serializable{
    private HashMap<ProductDTO, Integer> items;

    /**
     * @return the items
     */
    public HashMap<ProductDTO, Integer> getItems() {
        return items;
    }
    
    public void addProductToCart(ProductDTO product) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        
        int quantity = 1;
        if (this.items.containsKey(product)) {
            quantity += this.items.get(product);
        }
        
        this.items.put(product, quantity);
    }

    public void removeProductFromCart(ProductDTO product) {
        if (this.items == null) {
            return;
        }
        
        if (this.items.containsKey(product)) {
            this.items.remove(product);
            if (this.items.isEmpty()) {
                this.items = null;
            }
        }
    }
    
    public void updateProductInCart(ProductDTO dto, int amount) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        
        if (this.items.containsKey(dto)) {
            this.items.put(dto, amount);
        }
    }
    
    public ProductDTO getProductInCartById(String id) {
        ProductDTO product = null;
        for (ProductDTO dto : items.keySet()) {
            if (dto.getId().equals(id)) {
                product = dto;
                break;
            }
        }
        return product;
    }
 }
