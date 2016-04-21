package sams;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sams.DataModels.Appointment;
import sams.DataModels.Condition;
import sams.DataModels.Patient;
import sams.DataModels.Summary;


public class ViewSearchDetailsController implements Initializable {
    
    private Patient patient;
    private Appointment apointment;
    private Summary summary;
    private Condition condition;
    
    @FXML private Label info;
    @FXML private Button backBtn;
    
    public void handleViewInfo(ActionEvent event) throws IOException{
    if(event.getSource() == backBtn){
            Stage stage = (Stage)backBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("viewScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setApointment(Appointment apointment) {
        this.apointment = apointment;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print(SAMS.currentTable);
        switch(SAMS.currentTable){
            case "Patients":
                String name, sname, phone, email, add;
                name = SAMS.currentPatient.getPName();
                sname = SAMS.currentPatient.getPSurname();
                phone = SAMS.currentPatient.getPMobPhone();
                email = SAMS.currentPatient.getPEmail();
                add = SAMS.currentPatient.getPAddress();
                setInfo("Name: "+name+" "+sname+"\n\nPhone: "+phone+"\n\nEmail: "+email+"\n\nAddress: "+add);
                break;
            case "Appointment":
                String summary, id, type;
                Date date;
                System.out.print(SAMS.currentAppointment);
                id = SAMS.currentAppointment.getApID();
                type = SAMS.currentAppointment.getAType();
                name = SAMS.currentAppointment.getApName();
                summary = SAMS.currentAppointment.getSummary();
                if("secretary".equals(SAMS.loggedPrivileges)){
                setInfo("Patient Number: "+name+"\n\nType: "+type);}
                else
                    setInfo("Patient Number: "+name+"\n\nType: "+type+"\n\nSummary: "+summary);
                break;
            case "Condition":
                String description, threat;
                id = SAMS.currentCondition.getCID();
                name = SAMS.currentCondition.getCName();
                description = SAMS.currentCondition.getCDesc();
                threat = SAMS.currentCondition.getTLevel();
                setInfo("Condition ID: "+id+"\n\nName: "+name+"\n\nDescription: "+description+"\n\nThreat Level: "+threat);
                break;
            default:
                break;
        }
    }
    
}