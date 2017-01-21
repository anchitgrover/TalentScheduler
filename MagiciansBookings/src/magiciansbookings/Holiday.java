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
public class Holiday  
{
    private final ArrayList<String> holidays = new ArrayList<>();
    private static final connection conn = new connection();
    private ResultSet result= null;
    private Statement statement = null;
    
    public void insertNewHoliday(String holiday) throws SQLException
    {
        conn.setPreparedStatement("INSERT INTO HOLIDAY(HOLIDAY) VALUES (?) ");
        conn.getPreparedStatement().setString(1,holiday);
        conn.getPreparedStatement().executeUpdate();
    }
    
//    public void deleteHoliday(String holiday) throws SQLException
//    {
//        conn.setPreparedStatement("DELETE FROM HOLIDAY WHERE HOLIDAY = ? ");
//        conn.getPreparedStatement().setString(1,holiday);
//        conn.getPreparedStatement().executeUpdate();
//    }
    public ArrayList<String> getHoliday() //method to get the holidays from the holiday table in the database 
    {
        try
        {
            statement = conn.getConnection().createStatement();
            result = statement.executeQuery("SELECT HOLIDAY FROM HOLIDAY");
            holidays.clear();
            while(result.next())
            {
                holidays.add(result.getString("HOLIDAY"));    
            }
        }       
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return holidays;
    }
    
    
    public String[] getHolidayName(ArrayList<String> holiday) //converts the list of holidays into string to be used in the drop down menu
    {
        
        String array[] = new String[holiday.size()];
        for(int i = 0; i<holiday.size(); i++)
        {
            array[i] = holiday.get(i);
        }
        
        return array;
    }
}