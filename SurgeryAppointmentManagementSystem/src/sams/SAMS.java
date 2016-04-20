
package sams;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sams.DataModels.Appointment;
import sams.DataModels.Patient;


public class SAMS extends Application {
    public static String loggedUser= null;
    public static String loggedPrivileges= null;
    public static String currentTable=null;
    public static Patient currentPatient=null;
    public static Appointment currentAppointment=null;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(SAMS.class.getResource("OverallStyle.css").toExternalForm());
        DatabaseHelper.creates();
        
        stage.setScene(scene);
        stage.show();
    }

static void main(String[] args) {
        launch(args);
    }
    
}
