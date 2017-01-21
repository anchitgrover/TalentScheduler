/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magiciansbookings;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Anchit Grover
 */
public class MagiciansBookings
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Magicians frame = new Magicians(); //used to display the gui panels
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setVisible(true);
        
    }   
} 

