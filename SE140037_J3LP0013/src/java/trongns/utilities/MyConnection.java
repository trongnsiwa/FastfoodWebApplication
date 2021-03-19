/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.utilities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author TrongNS
 */
public class MyConnection implements Serializable{
    
    public static Connection makeConnection() throws NamingException, SQLException {
        Connection con;
        Context initialContext = new InitialContext();
        Context envContext = (Context) initialContext.lookup("java:comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("HanaShop");
        con = dataSource.getConnection();
        return con;
    }
}
