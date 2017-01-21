/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magiciansbookings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Anchit Grover
 */
public class Bookings 
{
    private static final connection conn = new connection(); 
   
//    public void cancelBookings(String customer, String holiday) throws SQLException
//    {
//        conn.setPreparedStatement("DELETE FROM BOOKINGS WHERE CUSTOMER = ? AND HOLIDAY = ?");
//        conn.getPreparedStatement().setString(1,customer);
//        conn.getPreparedStatement().setString(2,holiday);
//        conn.getPreparedStatement().executeUpdate();      
//    }
   
    public void moveCustFromWaitList(String holiday) //method used when you drop a magician
{
    ResultSet getCust=null;
    
    try
    {
        conn.setPreparedStatement("SELECT * FROM WAITLIST WHERE HOLIDAY=? ORDER BY DATE");
        conn.getPreparedStatement().setString(1, holiday);
        getCust = conn.getPreparedStatement().executeQuery();
        
        while(getCust.next());
        {
            insertBookings(getCust.getString("HOLIDAY"), getCust.getString("CUSTOMER"));
            removeCusWaitList(getCust.getString("CUSTOMER"));
        }
    }

    catch(SQLException sqlException)
    {
       sqlException.printStackTrace();

    }
}

    
    public void CancelCustomer(String customer,String holiday)
{
    try
    {
        conn.setPreparedStatement("DELETE FROM BOOKINGS WHERE HOLIDAY = ? and CUSTOMER=?");
        conn.getPreparedStatement().setString(1, holiday);
        conn.getPreparedStatement().setString(2, customer);
        conn.getPreparedStatement().executeUpdate();
        System.out.println("Customer was cancelled from booking");
    }

    catch(SQLException sqlException)
    {
       sqlException.printStackTrace();

    }
}
    public void removeCusWaitList(String customer)
{
    try
    {
        conn.setPreparedStatement("DELETE FROM WAITLIST WHERE CUSTOMER=?");
        conn.getPreparedStatement().setString(1, customer);
        conn.getPreparedStatement().executeUpdate();
        System.out.println("Customer was removed from wait list");

    }

    catch(SQLException sqlException)
    {
       sqlException.printStackTrace();

    }
    
}    
    
    public void deleteFromWaitingList(String customer, String holiday) throws SQLException
    {
        conn.setPreparedStatement("DELETE FROM WAITLIST WHERE CUSTOMER = ? AND HOLIDAY = ?");
        conn.getPreparedStatement().setString(1,customer);
        conn.getPreparedStatement().setString(2,holiday);
        conn.getPreparedStatement().executeUpdate();        
    }
    
    public void bookFromWaitingList(String holiday, String customer, String timestamp)
    {
        String freeMagicianName = assignFreeMagician(holiday); //method used to get a free magician
        
        try
        {
            conn.setPreparedStatement("INSERT INTO BOOKINGS (HOLIDAY,CUSTOMER,MAGICIAN,DATE) VALUES (?,?,?,?)"); //prepared statement used to insert the holiday, customer name and magician into the bookings class            
            if("N/A".equals(freeMagicianName)) //checks to see if the method returns a actual magician or "name" which represents no free magician
            {
                bookIntoWaitList(holiday,customer, timestamp); //inserts into waitlist using method
            }  
            else
            {
                conn.getPreparedStatement().setString(1, holiday);
                conn.getPreparedStatement().setString(2, customer);
                conn.getPreparedStatement().setString(3, freeMagicianName);
                conn.getPreparedStatement().setString(4, timestamp);
                conn.getPreparedStatement().executeUpdate();
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.out.println("exception"+sqlException.getMessage());
        }    
    }
    
    public void bookIntoWaitList(String holiday, String customer, String timestamp)
    {
        try
        {
            conn.setPreparedStatement("INSERT INTO WAITLIST (HOLIDAY,CUSTOMER, DATE)VALUES (?,?,?)");
            conn.getPreparedStatement().setString(1, holiday);
            conn.getPreparedStatement().setString(2, customer);
            conn.getPreparedStatement().setString(3, timestamp);
            conn.getPreparedStatement().executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
     
    public void insertBookings(String holiday, String customer) throws SQLException  //method to insert the magician, customer name and holiday into bookkings table
    {
        String freeMagicianName = assignFreeMagician(holiday); //method used to get a free magician
        
        try
        {
            conn.setPreparedStatement("INSERT INTO BOOKINGS (HOLIDAY,CUSTOMER,MAGICIAN,DATE) VALUES (?,?,?,CURRENT_TIMESTAMP)"); //prepared statement used to insert the holiday, customer name and magician into the bookings class            
            if("N/A".equals(freeMagicianName)) //checks to see if the method returns a actual magician or "name" which represents no free magician
            {
                insertWaitList(holiday,customer); //inserts into waitlist using method
            }  
            else
            {
                conn.getPreparedStatement().setString(1, holiday);
                conn.getPreparedStatement().setString(2, customer);
                conn.getPreparedStatement().setString(3, freeMagicianName);
                conn.getPreparedStatement().executeUpdate();
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.out.println("exception"+sqlException.getMessage());
        }    
    }
     
     public void insertWaitList(String holiday, String customer) //method to insert into wait list
    {
        try
        {
            conn.setPreparedStatement("INSERT INTO WAITLIST (HOLIDAY,CUSTOMER, DATE)VALUES (?,?, CURRENT_TIMESTAMP)");
            conn.getPreparedStatement().setString(1, holiday);
            conn.getPreparedStatement().setString(2, customer);
            conn.getPreparedStatement().executeUpdate();
            System.out.println("Customer inserted into waitlist from insert bookings method");
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
     
    public String assignFreeMagician(String holiday) //method to assign a free magician for booking
    {
        ResultSet resultSet = null;
        String nameOfMagician = "";
        try
        {
           //prepared statement to select a name of the magician from the database where that name is not assigned to any customer
           conn.setPreparedStatement("SELECT NAME FROM MAGICIAN WHERE NAME NOT IN (SELECT MAGICIAN FROM BOOKINGS WHERE HOLIDAY = ?)");
           conn.getPreparedStatement().setString(1, holiday);
           
           resultSet = conn.getPreparedStatement().executeQuery();
           
           if(resultSet.next()) //asssigns the name of the magician from the database
           {
               nameOfMagician = resultSet.getString("NAME");
           }
           
           else //assigns the name to N/A to represent that there are no free magicians 
           {
               nameOfMagician = "N/A";
           }
           
        }
        catch(SQLException sqlException) 
        {
            sqlException.printStackTrace();
        }
            
        return nameOfMagician;   
    }
    
//        public void deleteBooking(String customer,String holiday)
//    {
//        try
//        {
//            conn.setPreparedStatement("DELETE FROM BOOKINGS WHERE HOLIDAY = ? and CUSTOMER = ?");
//            conn.getPreparedStatement().setString(1, holiday);
//            conn.getPreparedStatement().setString(2, customer);
//            conn.getPreparedStatement().executeUpdate();
//        }
//
//        catch(SQLException sqlException)
//        {
//           sqlException.printStackTrace();
//        }
//    }

    public void deleteMagicianFromBookings(String magician)
     { 
        try
        {
            conn.setPreparedStatement("DELETE FROM BOOKINGS WHERE MAGICIAN = ?");
            conn.getPreparedStatement().setString(1, magician);
            conn.getPreparedStatement().executeUpdate();
            System.out.println("delete magician from bookings");
        }
        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }
    }

    public void deleteCustomerFromWaitingList(String customer)
    {
        try
        {
            conn.setPreparedStatement("DELETE FROM WAITLIST WHERE CUSTOMER=?");
            conn.getPreparedStatement().setString(1, customer);
            conn.getPreparedStatement().executeUpdate();
            System.out.println("Customer was removed from wait list");
        }

        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }
    }


    public void moveIntoWaitList (String magician)
    {
        try
        {
            conn.setPreparedStatement("INSERT INTO WAITLIST(HOLIDAY,CUSTOMER,DATE) SELECT HOLIDAY,CUSTOMER,DATE FROM BOOKING WHERE MAGICIAN=?");
            conn.getPreparedStatement().setString(1, magician);
            conn.getPreparedStatement().executeUpdate();
        }

        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }   
    }

    public void moveOutOfWaitList()//used when add magician is clicked
    {
        ResultSet waitlist=null;
        ResultSet customer=null;
        PreparedStatement state;
        Statement statement = null;
        Connection connection = conn.getConnection();
        try
        {
            statement = connection.createStatement();
            waitlist = statement.executeQuery("SELECT HOLIDAY,MIN(DATE) AS DATE FROM WAITLIST GROUP BY HOLIDAY");
            while(waitlist.next())
            {
                conn.setPreparedStatement("SELECT CUSTOMER FROM WAITLIST WHERE HOLIDAY=? AND DATE=?");
                conn.getPreparedStatement().setString(1, waitlist.getString("HOLIDAY"));
                conn.getPreparedStatement().setTimestamp(2,waitlist.getTimestamp("DATE"));
                customer = conn.getPreparedStatement().executeQuery();
                customer.next();
                String custName = customer.getString("CUSTOMER");
                insertBookings(waitlist.getString("HOLIDAY"),custName);
                //deleteCustomerFromWaitingList(custName);
            }
        }
        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }
    }

    public void moveCustomerFromWaitingList2(String holiday) //method used when you drop a magician
    {
        ResultSet getCust = null;
        try
        {
            conn.setPreparedStatement("SELECT * FROM WAITLIST WHERE HOLIDAY = ? ORDER BY DATE");
            conn.getPreparedStatement().setString(1, holiday);
            getCust = conn.getPreparedStatement().executeQuery();
            while(getCust.next())
            {
                insertBookings(getCust.getString("HOLIDAY"), getCust.getString("CUSTOMER"));
                deleteFromWaitingList(getCust.getString("CUSTOMER").toString(),holiday);
            }
        }

        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }
        
         System.out.println("move customer from waiting list2 has been called");
    }
}
