/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magiciansbookings;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Anchit Grover
 */
public class MagicianName 
{
    private ArrayList<String> magicianNames = new ArrayList<String>(); //array for the list of magicians in the datatbase
    private ResultSet result = null;
    private Statement statement = null;
    private PreparedStatement ps= null;
    private connection conn = new connection();
    private Bookings book = new Bookings();
    private Entry entry = new Entry();
    
    
//   
    public void removeMagician(String magician)
 { 
    try
    {
        
        conn.setPreparedStatement("DELETE FROM MAGICIAN WHERE NAME=?");
        conn.getPreparedStatement().setString(1, magician);
        
        conn.getPreparedStatement().executeUpdate();
    }
    catch(SQLException sqlException)
    {
       sqlException.printStackTrace();
    }
}
    
    public void moveFromWaitList()
    {
        ResultSet resultSet=null;
        ResultSet resultSetCustomer=null;
        Statement statement = null;
        Connection connection= conn.getConnection();


        try
        {
            statement=connection.createStatement();
            resultSet=statement.executeQuery("SELECT HOLIDAY,MIN(DATE) AS DATE FROM WAITLIST GROUP BY HOLIDAY,DATE");
            //resultSet=statement.executeQuery("SELECT HOLIDAY FROM WAITLIST ORDER BY DATE");
            while(resultSet.next())
            {
                conn.setPreparedStatement("SELECT CUSTOMER FROM WAITLIST WHERE HOLIDAY=? AND DATE=?");

                conn.getPreparedStatement().setString(1, resultSet.getString("HOLIDAY"));

                conn.getPreparedStatement().setTimestamp(2,resultSet.getTimestamp("DATE"));

                resultSetCustomer=conn.getPreparedStatement().executeQuery();

                resultSetCustomer.next();

                String custName= resultSetCustomer.getString("CUSTOMER");
                if(this.getFreeMagician(resultSet.getString("HOLIDAY")).size()!=0)
                {
                    book.bookFromWaitingList(resultSet.getString("HOLIDAY"),custName, resultSet.getTimestamp("DATE").toString());
                    book.deleteFromWaitingList(custName,resultSet.getString("HOLIDAY"));
                }
                System.out.println("Customer was rebooked");
            }

        }

        catch(SQLException sqlException)
        {
           sqlException.printStackTrace();
        }

    }
    
    public void moveToWaitList(String magician) throws SQLException
    {
        conn.setPreparedStatement("SELECT * FROM BOOKINGS WHERE MAGICIAN = ?");
        conn.getPreparedStatement().setString(1, magician);
        conn.getPreparedStatement().executeQuery();        
    }
    
    
    
    public ArrayList<String> getFreeMagician(String holiday) throws SQLException
    {
        ArrayList<String> magicians = new ArrayList<>();
        ResultSet result = null;
        conn.setPreparedStatement("SELECT NAME FROM MAGICIAN WHERE NAME NOT IN (SELECT MAGICIAN FROM BOOKINGS WHERE HOLIDAY = ?)");
        conn.getPreparedStatement().setString(1,holiday);
        result = conn.getPreparedStatement().executeQuery();
        
        ResultSetMetaData metaData = result.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        while(result.next())
        {
            for(int i = 1; i <= numberOfColumns; i++)
            {     
                magicians.add(result.getObject(i).toString());
            }       
        }
        return magicians;
    }
    
    
    public void insertNewMagician(String magician) throws SQLException
    {
        conn.setPreparedStatement("INSERT INTO MAGICIAN(NAME) VALUES (?) ");
        conn.getPreparedStatement().setString(1,magician);
        conn.getPreparedStatement().executeUpdate();
    }
    
    public void deleteMagician(String magician) throws SQLException
    {
        conn.setPreparedStatement("DELETE FROM MAGICIAN WHERE NAME = ? ");
        conn.getPreparedStatement().setString(1,magician);
        conn.getPreparedStatement().executeUpdate();
    }
       
    
     
    public ArrayList<String> getMagicianName() //method to get the list of magicians from the magician table in the database
    {
        try
        {
            statement = conn.getConnection().createStatement();
            result = statement.executeQuery("SELECT NAME FROM MAGICIAN");
            magicianNames.clear();
            while(result.next())
            {
                magicianNames.add(result.getString("NAME"));
            }
        }
        
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return magicianNames;
    } 
    
    
    public void sendCustomerIntoWaitListAfterDelete(String magician) throws SQLException
    {
        ArrayList<String>magicians = new ArrayList<String>();
        ResultSet result = null;
       // ResultSet holiday = null;
        conn.setPreparedStatement("SELECT * FROM BOOKINGS WHERE MAGICIAN = ?");
        conn.getPreparedStatement().setString(1,magician);
        result = conn.getPreparedStatement().executeQuery();
        
        while(result.next())
        {
           if(this.getFreeMagician(result.getString("HOLIDAY")).size()==0)
           {
               conn.setPreparedStatement("INSERT INTO WAITLIST (CUSTOMER, HOLIDAY,DATE) VALUES (?,?,?)");
               conn.getPreparedStatement().setString(1, result.getString("CUSTOMER"));
               conn.getPreparedStatement().setString(2, result.getString("HOLIDAY"));
               conn.getPreparedStatement().setString(3, result.getTimestamp("DATE").toString());
               conn.getPreparedStatement().executeUpdate();
               System.out.println("dfa");
           }
           else
           {
              conn.setPreparedStatement("INSERT INTO BOOKINGS (CUSTOMER, HOLIDAY, MAGICIAN,DATE) VALUES (?,?,?,CURRENT_TIMESTAMP)");
              conn.getPreparedStatement().setString(1, result.getString("CUSTOMER"));
              conn.getPreparedStatement().setString(2, result.getString("HOLIDAY"));
              conn.getPreparedStatement().setString(3,this.getFreeMagician(result.getString("HOLIDAY")).get(0).toString());
              conn.getPreparedStatement().setString(4, result.getString("DATE"));
              conn.getPreparedStatement().executeUpdate();
           }
           
            conn.setPreparedStatement("DELETE FROM BOOKINGS WHERE CUSTOMER = ? AND HOLIDAY = ? AND MAGICIAN = ? AND DATE = ?");
            conn.getPreparedStatement().setString(1, result.getString("CUSTOMER"));
            conn.getPreparedStatement().setString(2, result.getString("HOLIDAY"));
            conn.getPreparedStatement().setString(3,magician);
            conn.getPreparedStatement().setString(4, result.getString("DATE"));
            conn.getPreparedStatement().executeUpdate();
        }       
        
        
    }
    
    
    public String[] getNameList(ArrayList<String> magicianNames) //method to convert the array list of magician names into string for the drop down menu
    {
        String array[] = new String[magicianNames.size()];
        for(int i = 0; i<magicianNames.size(); i++)
        {
            array[i] = magicianNames.get(i);
        }
        return array;
    }
}