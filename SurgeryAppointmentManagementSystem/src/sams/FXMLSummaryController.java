/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sams;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sams.DataModels.Appointment;

public class FXMLSummaryController implements Initializable {

    @FXML private TextArea sum;
    @FXML private Button submit;
    String summary;
    
    @FXML
    private void handleButtonSubmit(ActionEvent event) throws IOException, SQLException{
        summary = sum.getText();
        Appointment a = SAMS.currentAppointment;
        System.out.println(a.getApID());
        a.setSummary(summary);
        System.out.print(a.getSummary());
        int i = Integer.parseInt(a.getApID());
        DatabaseHelper.insertAppointmentSum(i, a.getADates(), a.getAType(), a.getSummary());
        try{
            Stage stage = (Stage)submit.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewScreen.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}