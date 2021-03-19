/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.product;

import java.io.Serializable;

/**
 *
 * @author TrongNS
 */
public class CreateNewProductError implements Serializable {
    
    private String duplicateProductId;
    private String invalidImageLink;

    public CreateNewProductError() {
    }

    public CreateNewProductError(String duplicateProductId, String invalidImageLink) {
        this.duplicateProductId = duplicateProductId;
        this.invalidImageLink = invalidImageLink;
    }

    /**
     * @return the invalidImageLink
     */
    public String getInvalidImageLink() {
        return invalidImageLink;
    }

    /**
     * @param invalidImageLink the invalidImageLink to set
     */
    public void setInvalidImageLink(String invalidImageLink) {
        this.invalidImageLink = invalidImageLink;
    }

    /**
     * @return the duplicateProductId
     */
    public String getDuplicateProductId() {
        return duplicateProductId;
    }

    /**
     * @param duplicateProductId the duplicateProductId to set
     */
    public void setDuplicateProductId(String duplicateProductId) {
        this.duplicateProductId = duplicateProductId;
    }
}
