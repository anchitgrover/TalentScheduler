/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magiciansbookings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Anchit Grover
 */
public class Customer 
{
    private final ArrayList<String> customer = new ArrayList<>();
    private static final connection conn = new connection();
    private ResultSet result= null;
    private Statement statement = null;
    
    public void insertNewCustomer(String customer) throws SQLException
    {
        conn.setPreparedStatement("INSERT INTO CUSTOMER(NAME) VALUES (?) ");
        conn.getPreparedStatement().setString(1,customer);
        conn.getPreparedStatement().executeUpdate();
    }
    
    public void deleteCustomer(String customer) throws SQLException
    {
        conn.setPreparedStatement("DELETE FROM CUSTOMER WHERE NAME = ? ");
        conn.getPreparedStatement().setString(1,customer);
        conn.getPreparedStatement().executeUpdate();
         System.out.println("customer was deleted from customer table");
    }
    
    
    public ArrayList<String> getCustomer() //method to get the holidays from the holiday table in the database 
    {
        try
        {
            statement = conn.getConnection().createStatement();
            result = statement.executeQuery("SELECT DISTINCT CUSTOMER FROM BOOKINGS UNION SELECT DISTINCT CUSTOMER FROM WAITLIST");
            customer.clear();
            while(result.next())
            {
                customer.add(result.getString("CUSTOMER"));    
            }
        }       
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
       
        return customer;
    }
    
    public String[] getCustomerName(ArrayList<String> customer) //converts the list of customers into string to be used in the drop down menu
    {
        
        String array[] = new String[customer.size()];
        for(int i = 0; i<customer.size(); i++)
        {
            array[i] = customer.get(i);
        }
        
        return array;
    }
}
