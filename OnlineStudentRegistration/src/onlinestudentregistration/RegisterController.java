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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel Macharia
 */
public class RegisterController implements Initializable {
    
    private ArrayList<String> collegeList = new ArrayList<>();
    private ArrayList<String> collegeIDList = new ArrayList<>();
    private ArrayList<String> courseList = new ArrayList<>();
    private ArrayList<String> courseIDList = new ArrayList<>();
    
    private String collegeID = "";
    private String courseID = "";
    public static String userID = "";
    
    private ArrayList<GridPane> applicationsList = new ArrayList<>();
    
    @FXML
    private ListView list;
    
    @FXML
    private TableView applications_table;
    
    @FXML
    private TableColumn applications_kcseIndexNumber, applications_university, applications_course, applications_status;
    
    @FXML
    private RadioButton enroll_male, enroll_female, enroll_other;
    
    @FXML
    private TextField enroll_name, enroll_phone, enroll_kcse_number, enroll_kcse_year;
    
    @FXML 
    private DatePicker enroll_date_of_birth;
    
    @FXML
    private ComboBox enroll_university, enroll_course;
    
    

    @FXML
    private void enrollEnrollAction(ActionEvent event)
    {
        try
        {
            
            String name, dob, phone, kcseNumber, kcseYear, university, course;
            String gender = "";
            
            if( enroll_male.isSelected() )
                gender = "male";
            else if( enroll_female.isSelected() )
                gender = "female";
            else if( enroll_other.isSelected())
                gender = "other";
            
            name = enroll_name.getText();
            phone = enroll_phone.getText();
            kcseNumber = enroll_kcse_number.getText();
            kcseYear = enroll_kcse_year.getText();
            
            if( !dataIsValid(name, gender, phone, kcseNumber, kcseYear) )
                return;
            
            university = enroll_university.getValue().toString();
            collegeID = getUniversityID(university);
            course = enroll_course.getValue().toString();
            courseID = getCourseID(course);
            
            dob = enroll_date_of_birth.getValue().toString();
            
            makeApplication(name, dob, gender, phone, kcseNumber, kcseYear, university, course);
            
        }catch(Exception e)
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    @FXML
    private void enrollQuitAction(ActionEvent event)
    {
        try
        {
            Stage stage = (Stage) ( (Scene) ((Button)(event.getSource())).getScene()).getWindow();
            Parent root = FXMLLoader.load( getClass().getResource("FXMLDocument.fxml") );
            
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
        
        ToggleGroup genderGroup = new ToggleGroup();
        enroll_male.setToggleGroup( genderGroup );
        enroll_female.setToggleGroup( genderGroup );
        enroll_other.setToggleGroup( genderGroup );
        
        initUniversityAndCourseLists();
        
        ObservableList<String> collegeItems = FXCollections.observableArrayList(collegeList);
        ObservableList<String> courseItems = FXCollections.observableArrayList(courseList);
        
        enroll_university.getItems().addAll(collegeItems);
        enroll_course.getItems().addAll(courseItems);
        
        initApplicationsList();
        
        updateList();
        
    }    
    
    private void updateList()
    {
        ObservableList<GridPane> boxItems = FXCollections.observableArrayList( applicationsList );
        list.setItems(boxItems);
    }
    
    private GridPane getGridPane(String s, String u, String c, String st)
    {
        GridPane pane = new GridPane();
        
        Label sLabel = new Label(s);
        sLabel.setMinWidth(120.0);
        sLabel.setMaxWidth(120);
        
        Label uLabel = new Label(u);
        uLabel.setMinWidth(180.0);
        uLabel.setMaxWidth(180.0);
        
        Label cLabel = new Label(c);
        cLabel.setMinWidth(180.0);
        cLabel.setMaxWidth(180.0);
        
        Label stLabel = new Label(st);
        stLabel.setMinWidth(120.0);
        stLabel.setMinWidth(120.0);
        
        pane.add( sLabel, 0,0);
        pane.add( uLabel, 1, 0);
        pane.add( cLabel, 2, 0);
        pane.add( stLabel, 3, 0);
        
        return pane;
    }
    
    
    private void initUniversityAndCourseLists()
    {
        try
        {
            String collegeSql = "SELECT collegeID, collegeName FROM college";
            String courseSql = "SELECT courseID, courseName FROM course";
            
            Connection conn = UtilityClass.getDatabaseConnection();
            
            PreparedStatement collegeStatement = conn.prepareStatement( collegeSql );
            PreparedStatement courseStatement = conn.prepareStatement( courseSql );
            
            ResultSet collegeResult = collegeStatement.executeQuery();
            ResultSet courseResult = courseStatement.executeQuery();
            
            while( collegeResult.next() )
            {
                collegeList.add( new String( collegeResult.getString( 2 ) ) );
                collegeIDList.add( new String( collegeResult.getString(1) ) );
            }
            
            while( courseResult.next() )
            {
                courseList.add( new String( courseResult.getString( 2 ) ) );
                courseIDList.add( new String( courseResult.getString( 1 ) ) );
            }
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    private boolean dataIsValid( String name, String gender, String phone, String kcseNumber, String kcseYear)
    {
        //name may not be empty
        if( enroll_name.getText().isEmpty() )
        {
            UtilityClass.alert("Name cannot be empty!");
            return false;
        }
        //name may only contain characters
        if( !UtilityClass.isNameValid( name ) )
        {
            UtilityClass.alert("Invalid Name!");
            return false;
        }
        
        //ensure a date is selected
        try
        {
            enroll_date_of_birth.getValue().toString().isEmpty();
        }catch(NullPointerException e)
        {
            UtilityClass.alert("Date of birth cannot be empty!");
            return false;
        }
        //ensure a gender is specified
        if( gender.isEmpty() )
        {
            UtilityClass.alert("Please specify gender!");
            return false;
        }
        //ensure phone is not blank
        if( enroll_phone.getText().isEmpty() )
        {
            UtilityClass.alert("Phone cannot be empty!");
            return false;
        }
        //ensure a valid phone is entered
        if( !UtilityClass.isPhoneValid( phone ) )
        {
            UtilityClass.alert("Invalid Phone!");
            return false;
        }
        //ensure kcse number is not empty
        if( enroll_kcse_number.getText().isEmpty() )
        {
            UtilityClass.alert("KCSE Number cannot be empty!");
            return false;
        }
        //ensure kcse number is valid
        if( !UtilityClass.isIndexNumberValid(kcseNumber) )
        {
            UtilityClass.alert("Invalid KCSE number!");
            return false;
        }
        //ensure year is not empty
        if( enroll_kcse_year.getText().isEmpty() )
        {
            UtilityClass.alert("KCSE year cannot be empty!");
            return false;
        }
        //ensure a valid year is entered
        if( !UtilityClass.isYearValid( Integer.parseInt(kcseYear) ) )
        {
            UtilityClass.alert("Invalid Year!");
            return false;
        }
        //ensure a college is selected
        try
        {
            enroll_university.getValue().toString().isEmpty();
        }catch(NullPointerException e)
        {
            UtilityClass.alert("Please select a university!");
            return false;
        }
        //ensure a course is selected
        try
        {
            enroll_course.getValue().toString().isEmpty();
        }catch(NullPointerException e)
        {
            UtilityClass.alert("Please select a course!");
            return false;
        }
        
        return true;
    }
    
    private void makeApplication(String name, String dob, String gender, String phone, String kcseNumber, String kcseYear, String university, String course)
    {
        try
        {
            String id = "" + userID;
            String c = "" + courseID;
            String u = "" + collegeID;
            
            String sql = "INSERT INTO application(userID, applicantName, applicantDOB, applicantGender, applicantPhone,"
                    + "kcseIndexNumber, kcseYear, collegeID, courseID, applicationStatus)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?)";
            
            Connection conn = UtilityClass.getDatabaseConnection();
            PreparedStatement state = conn.prepareStatement( sql );
            
            state.setString(1, id);
            state.setString(2, name);
            state.setString(3, dob);
            state.setString(4, gender);
            state.setString(5, phone);
            state.setString(6, kcseNumber);
            state.setString(7, kcseYear);
            state.setString(8, u);
            state.setString(9, c);
            state.setString(10, "Pending");
            
            int rows = state.executeUpdate();
            
            if( rows == 0 )
            {
                UtilityClass.alert("Could not make application!");
            }
            else
            {
                UtilityClass.alert("Application made successfully!");
                initApplicationsList();
                updateList();
            }
            
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
    
    
    private String getUniversityID( String universityName )
    {
        for( int i = 0; i < collegeList.size(); i++ )
        {
            if( collegeList.get(i).equals( universityName ) )
                return new String( collegeIDList.get(i).toString() );
        }
            
        return "";
    }
    
    private String getCourseID( String courseName )
    {
        for( int i = 0; i < courseList.size(); i++ )
        {
            if( courseList.get(i).equals( courseName ) )
                return new String( courseIDList.get(i).toString() );
        }
            
        return "";
    }
    
    private void initApplicationsList()
    {
        try
        {
            applicationsList = null;
            applicationsList = new ArrayList<GridPane>();
            
            GridPane pane = getGridPane("KCSE Index Number", "University", "Course", "Status");
            applicationsList.add( pane );
            
            String sql = "SELECT kcseIndexNumber, collegeName, courseName, applicationStatus "
                    + "FROM application JOIN college ON application.collegeID = college.collegeID "
                    + "JOIN course ON application.courseID = course.courseID "
                    + "WHERE userID = ?";
            
            Connection conn = UtilityClass.getDatabaseConnection();
            
            PreparedStatement state = conn.prepareStatement( sql );
            
            state.setString(1, userID);
            
            ResultSet result = state.executeQuery();
            
            while( result.next() )
            {
                applicationsList.add( getGridPane( result.getString(1), result.getString(2), result.getString(3), result.getString(4)) );
            }
        }catch( Exception e )
        {
            UtilityClass.alert("Error: " + e);
        }
    }
    
}
