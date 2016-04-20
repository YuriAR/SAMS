/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sams;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class FXMLSummaryController implements Initializable {

    @FXML private TextArea sum;
    @FXML private Button submit;
    String summary;
    
    @FXML
    private void handleButtonSubmit(ActionEvent event) throws IOException{
        summary = sum.getText();
        SAMS.currentAppointment.setSummary(summary);
    }
    
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}