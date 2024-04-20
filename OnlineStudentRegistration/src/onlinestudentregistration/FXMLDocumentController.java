/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinestudentregistration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Daniel Macharia
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField username_input;
    
    @FXML
    private PasswordField password_input;
    
    @FXML
    private void loginAction(ActionEvent event) {
        try
        {
            String username = username_input.getText();
            String password = password_input.getText();
            
            if( areCorrectCredentials(username, password) )
            {
                Stage stage = (Stage)((Scene) ((Button) event.getSource()).getScene() ).getWindow();

                Parent root = FXMLLoader.load( getClass().getResource("register.fxml") );

                Scene scene = new Scene(root);
                stage.setScene( scene );

                stage.show();
            }
        
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    @FXML
    private void signUpAction(ActionEvent event) {
        try
        {
            Stage stage = (Stage) ( (Scene) ((Button)(event.getSource())).getScene()).getWindow();
            Parent root = FXMLLoader.load( getClass().getResource("signUp.fxml") );
            
            Scene scene = new Scene( root );
            
            stage.setScene( scene );
            stage.show();
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private boolean areCorrectCredentials( String username, String password )
    {
        try
        {
            String sql = "SELECT userID, userPassword FROM user WHERE userName=?";
        
            Connection conn = UtilityClass.getDatabaseConnection();

            PreparedStatement state = conn.prepareStatement(sql);
            state.setString(1, username);
            
            ResultSet result = state.executeQuery();
            
            
            while( result.next() )
            {
                if( result.getString(2).equals(password) )
                {
                    RegisterController.userID = new String( result.getString(1) );
                    UtilityClass.alert("Login Successful!");
                    return true;
                }
            }
            
            UtilityClass.alert("Invalid username or password!");
            
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
        
        return false;
    }
    
}
