package sams;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddScreenController implements Initializable {
    
    @FXML
    Button backBtn, viewBtn, summaryBtn, addBtn1, addBtn3;
    
    @FXML
    Label messageLabel1, messageLabel3;
    
    @FXML //add patient textfields
    TextField fName, fSurname, fHome, fMobile, fEmail, fAddress, fSex, fDOB;
    
    @FXML //add appointment details
    TextField fPatientID, fPatientName, fDateCreated, fDate, fReasonForAppointment;

    public void handleButtonAction(ActionEvent event) throws IOException, SQLException{
    
        Stage stage;
        Parent root;
        
        if(event.getSource() == backBtn) {
            stage = (Stage)backBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }     
        
        if(event.getSource() == viewBtn) {
            stage = (Stage)viewBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("viewScreen.fxml")); //change into view.fxml
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        if(event.getSource() == addBtn1) {
            String pIDD = fPatientID.getText();
            Integer pID = Integer.parseInt(pIDD);
            String pDate = fDate.getText();
            String pReason = fReasonForAppointment.getText();
            
            DatabaseHelper.insertAppointment(pID, pDate, pReason);
            
            messageLabel1.setText("Done");
        }
        
        if(event.getSource() == summaryBtn) {
            stage = (Stage)summaryBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("viewScreen.fxml")); //change into view.fxml
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
    //Add patient details               
        if(event.getSource() == addBtn3) {
            
            String pName = fName.getText();
            String pSurname = fSurname.getText();
            String pHomenum = fHome.getText();
            String pMobile = fMobile.getText();
            String pAddress = fAddress.getText();
            String pSex = fSex.getText();
            String pEmail = fEmail.getText();
            String pDOB = fDOB.getText();
            
            DatabaseHelper.insertPatient(pName, pSurname, pHomenum, pMobile, pEmail, pAddress, pSex, pDOB);
            messageLabel3.setText("Done");            
        }  
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      Date dNow= new Date();
      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      
      fDate.setText(ft.format(dNow));
    }
    
}