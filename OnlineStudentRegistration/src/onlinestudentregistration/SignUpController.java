/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinestudentregistration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel Macharia
 */
public class SignUpController implements Initializable {
    @FXML
    private TextField username_input, email_input;
    
    @FXML
    private PasswordField password_input, confirm_password_input;
    
    
    @FXML
    private void loginAction(ActionEvent event)
    {
        try
        {
            Stage stage = (Stage) ((Scene)((Button)event.getSource()).getScene()).getWindow();
            Parent root = FXMLLoader.load( getClass().getResource("FXMLDocument.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene( scene );
            stage.show();
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    
    @FXML
    private void signUpAction(ActionEvent event)
    {
        try
        {
            String username, email, password, confirmPassword;
            
            username = username_input.getText();
            email = email_input.getText();
            password = password_input.getText();
            confirmPassword = confirm_password_input.getText();
            
            if( areValid( username, password, email) )
            {
                if( !password.equals(confirmPassword) )
                {
                    UtilityClass.alert("The password and the confirm password do not match!");
                    return;
                }
                
                Connection conn = UtilityClass.getDatabaseConnection();
                
                String sql = "INSERT INTO user(userName, userPassword, userEmail) VALUES(?,?,?)";
                
                PreparedStatement state = conn.prepareStatement( sql );
                state.setString(1, username);
                state.setString(2, password);
                state.setString(3, email);
                
                int rows = state.executeUpdate();
                
                if( rows == 0 )
                {
                    UtilityClass.alert("Sorry, we could not create the account!");
                }
                else
                {
                    UtilityClass.alert("Account created Successfully!");
                    clearFields();
                }
            }
            
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    private boolean areValid( String username, String password, String email)
    {
        if( !UtilityClass.isNameValid( username ) )
        {
            UtilityClass.alert("Invalid Username!"
                    + "\nA username may contain upper and lowercase characters,"
                    + "\n an apostrophe or a space.");
            return false;   
        }
            
        if( !UtilityClass.isPasswordValid( password ) )
        {
            UtilityClass.alert("Invalid Password!"
                    + "\nA password should contain atleast "
                    + "\none upper and lowercase characters,"
                    + "\none digit and one symbol.");
            return false;   
        }

        if( !UtilityClass.isEmailValid( email ) )
        {
            UtilityClass.alert("Invalid Email Address!"
                    + "\nAn email address may contain "
                    + "\nlowercase characters, digits, a hyphen and the '@' symbol");
            return false;   
        }
            
        return true;
    }
    
    private void clearFields()
    {
        username_input.setText("");
        password_input.setText("");
        confirm_password_input.setText("");
        email_input.setText("");
    }
}
