/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.user;

import java.io.Serializable;

/**
 *
 * @author TrongNS
 */
public class UserDTO implements Serializable {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String role;
    private String googleId;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String firstname, String lastname, String role) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public UserDTO(String username, String password, String firstname, String lastname, String role, String googleId) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.googleId = googleId;
    }
    

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the googleId
     */
    public String getGoogleId() {
        return googleId;
    }

    /**
     * @param googleId the googleId to set
     */
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
    
    
}
