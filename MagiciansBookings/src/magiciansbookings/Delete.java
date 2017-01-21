/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magiciansbookings;

/**
 *
 * @author Anchit Grover
 */
public class Delete
{
    private String customerName;
    private String magicianName;
    private String holiday;
    
    public void setCustomerName(String name)
    {
        customerName = name;
    }
    
    public String getCustomerName()
    {
      return customerName;
    }
    
    public void setMagicianName(String name)
    {
        magicianName = name;
    }
    
    public String getMagicianName()
    {
        return magicianName;
    }
    
    public void setHoliday(String name)
    {
        holiday = name;
    }
    
    public String getHoliday()
    {
        return holiday;
    }
}
