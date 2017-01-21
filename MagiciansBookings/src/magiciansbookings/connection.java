/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Anchit Grover
 */
package magiciansbookings;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class connection //class to connect to the database
{   
    final String DATABASE_URL = "jdbc:derby://localhost:1527/MagiciansDB";
    static private Connection connection = null;
    String username = "aqg5306"; //username of database
    final String password = "1234"; //password of database
    PreparedStatement ps;
    
    public connection() 
    {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL,username,password);
        }
        
        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }             
    }
    
    public Connection getConnection() //gets the connection 
    {
        return connection;
    }
    
    public void setPreparedStatement(String str) //sets prepared statements
    {
        try
        {
            ps = connection.prepareStatement(str);
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PreparedStatement getPreparedStatement() //gets the prepared statements
    {
        return ps;
    }
}
