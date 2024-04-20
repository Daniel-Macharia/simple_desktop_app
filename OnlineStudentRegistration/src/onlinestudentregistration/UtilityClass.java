
package onlinestudentregistration;

import java.sql.Connection;
import java.sql.DriverManager;

public class UtilityClass {
    
    public static void alert( String message )
    {
        System.out.println(message);
    }
    
    
    public static Connection getDatabaseConnection()
    {
        Connection conn = null;
        
        try
        {
            String username = "root";
            String password = "Adonai";

            String url = "jdbc:mysql://localhost:3306/online_course_registration";

            conn = DriverManager.getConnection(url, username, password);
        }catch(Exception e)
        {
            alert("Error: " + e);
        }
        
        return conn;
    }
    
    public static boolean isNameValid(String name)
    {
        String nameRegex = "^[a-zA-Z\\s`]{2,50}$";
        
        return name.matches( nameRegex );
    }
    
    public static boolean isPasswordValid(String password)
    {
        String lowerAlphaRegex = "^[\\w\\W]{0,60}[a-z]{1,20}[\\w\\W]{0,60}$";
        String upperAlphaRegex = "^[\\w\\W]{0,60}[A-Z]{1,20}[\\w\\W]{0,60}$";
        String integerRegex = "^[\\w\\W]{0,60}[0-9]{1,20}[\\w\\W]{0,60}$";
        String specialCharacterRegex = "^[\\w\\W]{0,60}[!@#$%^&*()]{1,20}[\\w\\W]{0,60}$";
        
        if( password.matches( lowerAlphaRegex ) && password.matches( upperAlphaRegex ) 
            &&  password.matches( integerRegex ) && password.matches( specialCharacterRegex ) )
        {
            return true;
        }
        
        return false;
    }
    
    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-z0-9-]{1,20}@[a-z]{2,20}.[a-z]{2,20}[.]{0,1}[a-z]{0,6}[.]{0,1}[a-z]{0,6}$";
        
        return email.matches( emailRegex );
    }
    
    public static boolean isPhoneValid(String phoneNumber)
    {
        String phoneRegex = "^0[17][0-9]{8}$";
        
        return phoneNumber.matches( phoneRegex );
    }
    
    public static boolean isYearValid( int year )
    {
        if( year >= 2000 && year < 2024)
            return true;
        
        return false;
    }
    
    public static boolean isIndexNumberValid( String indexNumber )
    {
        String indexNumberRegex = "^[0-9]{8,14}$";
        
        return indexNumber.matches( indexNumberRegex );
    }
}
