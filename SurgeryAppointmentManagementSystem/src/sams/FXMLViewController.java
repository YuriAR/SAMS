/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sams;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sams.DataModels.Appointment;
import sams.DataModels.Condition;
import sams.DataModels.Patient;
import sams.DataModels.Summary;

/**
 * FXML Controller class
 *
 * @author Eóin
 */
public class FXMLViewController implements Initializable {
    
    Collection<TableColumn> columns = new ArrayList<>();
    Patient selectedPatient;
    Appointment selectedAppointment;
    Condition selectedCondition;
    
    @FXML private Text actiontarget;
    @FXML private TableView tableViewChoose;
    @FXML private ComboBox tableCombo;
    @FXML Button chooseBtn;
    @FXML Button backBtn;
    @FXML Button deleteBtn;
    @FXML Button addBtn;
    
    @FXML
    private void handleButtonChoose(ActionEvent event) throws IOException{
    
        String selectedTable;
        
        selectedTable = (String) tableCombo.getSelectionModel().getSelectedItem();
        
        tableViewChoose.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown() && event.isShiftDown()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Delete");
                    alert.setHeaderText("Are you sure?");
                    alert.setContentText("You are about to delete a record. Are you sure you want to go ahead with this?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        try{
                            switch(selectedTable){
                                case "Patients":
                                    selectedPatient = (Patient) tableViewChoose.getSelectionModel().getSelectedItem();
                                    DatabaseHelper.delete(selectedTable, "pID", selectedPatient.getPID());
                                    break;
                                case "Appointments":
                                    Appointment selectedAppointment = (Appointment) tableViewChoose.getSelectionModel().getSelectedItem();
                                    DatabaseHelper.delete(selectedTable, "apID", selectedAppointment.getApID());
                                    break;
                                case "Condition":
                                    Condition selectedCondition = (Condition) tableViewChoose.getSelectionModel().getSelectedItem();
                                    DatabaseHelper.delete(selectedTable, "cID", selectedCondition.getCID());
                                    break;
                            }
                            //DatabaseHelper.delete(selectedTable, id, selectedPatient.getPID());
                            int selectedIndex = tableViewChoose.getSelectionModel().getSelectedIndex();
                            tableViewChoose.getItems().remove(selectedIndex);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                }else if(event.isSecondaryButtonDown() && ! event.isShiftDown()){
                    Stage stage = (Stage)tableViewChoose.getScene().getWindow();
                    try{
                        SAMS.currentTable = selectedTable;
                        if(tableViewChoose.getSelectionModel().getSelectedItem() instanceof Patient){
                         selectedPatient = (Patient) tableViewChoose.getSelectionModel().getSelectedItem();   
                         SAMS.currentPatient = selectedPatient;
                        }else if(tableViewChoose.getSelectionModel().getSelectedItem() instanceof Appointment){
                            selectedAppointment = (Appointment) tableViewChoose.getSelectionModel().getSelectedItem();
                            SAMS.currentAppointment = selectedAppointment;
                        }else if(tableViewChoose.getSelectionModel().getSelectedItem() instanceof Condition){
                            selectedCondition = (Condition) tableViewChoose.getSelectionModel().getSelectedItem();
                            SAMS.currentCondition = selectedCondition;
                        }
                        Parent root = FXMLLoader.load(getClass().getResource("FXMLViewSearchResultDetails.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }catch(IOException e){};
                }
             }
        });
        
        if(tableViewChoose.isPressed()){
            Stage stage = (Stage)tableViewChoose.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLViewSearchResultDetails.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        if(event.getSource() == backBtn){
            Stage stage = (Stage)backBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        if (event.getSource() == addBtn){
            Stage stage = (Stage)addBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("AddScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
        if(event.getSource() == chooseBtn){
            actiontarget.setText("Searching...");
            //selectedTable = (String) tableCombo.getSelectionModel().getSelectedItem();
            tableViewChoose.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            if(!tableViewChoose.getColumns().isEmpty()){
                tableViewChoose.getColumns().removeAll(columns);
                columns.removeAll(columns);
            }
            switch(selectedTable){
                case "Patients":
                    TableColumn name = new TableColumn("Name");
                    name.setCellFactory(TextFieldTableCell.forTableColumn());
                    name.setOnEditCommit(
                        new EventHandler<CellEditEvent<Patient, String>>() {
                            @Override
                            public void handle(CellEditEvent<Patient, String> t) {
                                ((Patient) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPName(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "pName", t.getNewValue(), ((Patient) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn surname = new TableColumn("Surname");
                    surname.setCellFactory(TextFieldTableCell.forTableColumn());
                    surname.setOnEditCommit(
                        new EventHandler<CellEditEvent<Patient, String>>() {
                            @Override
                            public void handle(CellEditEvent<Patient, String> t) {
                                ((Patient) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPSurname(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "pSurname", t.getNewValue(), ((Patient) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn mobile = new TableColumn("Mobile Phone");
                    mobile.setCellFactory(TextFieldTableCell.forTableColumn());
                    mobile.setOnEditCommit(
                        new EventHandler<CellEditEvent<Patient, String>>() {
                            @Override
                            public void handle(CellEditEvent<Patient, String> t) {
                                ((Patient) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPMobPhone(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "pMobPhone", t.getNewValue(), ((Patient) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn email = new TableColumn("Email");
                    email.setCellFactory(TextFieldTableCell.forTableColumn());
                    email.setOnEditCommit(
                        new EventHandler<CellEditEvent<Patient, String>>() {
                            @Override
                            public void handle(CellEditEvent<Patient, String> t) {
                                ((Patient) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPEmail(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "pEmail", t.getNewValue(), ((Patient) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn addr = new TableColumn("Address");
                    addr.setCellFactory(TextFieldTableCell.forTableColumn());
                    addr.setOnEditCommit(
                        new EventHandler<CellEditEvent<Patient, String>>() {
                            @Override
                            public void handle(CellEditEvent<Patient, String> t) {
                                ((Patient) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setPAddress(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "pAddress", t.getNewValue(), ((Patient) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    columns.add(name);
                    columns.add(surname);
                    columns.add(mobile);
                    columns.add(email);
                    columns.add(addr);
                    
                    name.setCellValueFactory(new PropertyValueFactory<Patient, String>("pName"));
                    surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("pSurname"));
                    mobile.setCellValueFactory(new PropertyValueFactory<Patient, String>("pMobPhone"));
                    email.setCellValueFactory(new PropertyValueFactory<Patient, String>("pEmail"));
                    addr.setCellValueFactory(new PropertyValueFactory<Patient, String>("pAddress"));
                    
                    tableViewChoose.getColumns().addAll(columns);
 
                    final Button addButton = new Button("Add");
                    addButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            //Direct to Joonas' page
                        }
                    });
                    break;
                case "Conditions":
                    TableColumn cName = new TableColumn("Condition Name");
                    cName.setCellFactory(TextFieldTableCell.forTableColumn());
                    cName.setOnEditCommit(
                        new EventHandler<CellEditEvent<Condition, String>>() {
                            @Override
                            public void handle(CellEditEvent<Condition, String> t) {
                                ((Condition) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setCName(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "cName", t.getNewValue(), ((Condition) t.getTableView().getItems().get(t.getTablePosition().getRow())).getCID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn cDesc = new TableColumn("Description");
                    cDesc.setCellFactory(TextFieldTableCell.forTableColumn());
                    cDesc.setOnEditCommit(
                        new EventHandler<CellEditEvent<Condition, String>>() {
                            @Override
                            public void handle(CellEditEvent<Condition, String> t) {
                                ((Condition) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setCDesc(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "cDesc", t.getNewValue(), ((Condition) t.getTableView().getItems().get(t.getTablePosition().getRow())).getCID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn tLevel = new TableColumn("Threat level");
                    tLevel.setCellFactory(TextFieldTableCell.forTableColumn());
                    tLevel.setOnEditCommit(
                        new EventHandler<CellEditEvent<Condition, String>>() {
                            @Override
                            public void handle(CellEditEvent<Condition, String> t) {
                                ((Condition) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setTLevel(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "tLevel", t.getNewValue(), ((Condition) t.getTableView().getItems().get(t.getTablePosition().getRow())).getCID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    columns.add(cName);
                    columns.add(cDesc);
                    columns.add(tLevel);
                    
                    cName.setCellValueFactory(new PropertyValueFactory<Condition, String>("cName"));
                    cDesc.setCellValueFactory(new PropertyValueFactory<Condition, String>("cDesc"));
                    tLevel.setCellValueFactory(new PropertyValueFactory<Condition, String>("tLevel"));
                    
                    tableViewChoose.getColumns().addAll(columns);
                    break;
                case "Appointment":
                    TableColumn apName = new TableColumn("Patient ID");
                    apName.setCellFactory(TextFieldTableCell.forTableColumn());
                    apName.setOnEditCommit(
                        new EventHandler<CellEditEvent<Appointment, String>>() {
                            @Override
                            public void handle(CellEditEvent<Appointment, String> t) {
                                ((Appointment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setApName(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "apName", t.getNewValue(), ((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).getApID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn aDates = new TableColumn("Datetime");
                    aDates.setCellFactory(TextFieldTableCell.forTableColumn());
                    aDates.setOnEditCommit(
                        new EventHandler<CellEditEvent<Appointment, String>>() {
                            @Override
                            public void handle(CellEditEvent<Appointment, String> t) {
                                ((Appointment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setADates(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "aDates", t.getNewValue(), ((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).getApID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    
                    TableColumn aType = new TableColumn("Type");
                    aType.setCellFactory(TextFieldTableCell.forTableColumn());
                    aType.setOnEditCommit(
                        new EventHandler<CellEditEvent<Appointment, String>>() {
                            @Override
                            public void handle(CellEditEvent<Appointment, String> t) {
                                ((Appointment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setAType(t.getNewValue());
                                try{
                                    DatabaseHelper.update(selectedTable, "aType", t.getNewValue(), ((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).getApID());
                                }catch(SQLException e){}
                            }
                        }
                    );
                    columns.add(apName);
                    columns.add(aDates);
                    columns.add(aType);
                    
                    apName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apName"));
                    aDates.setCellValueFactory(new PropertyValueFactory<Appointment, String>("aDates"));
                    aType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("aType"));
                    
                    tableViewChoose.getColumns().addAll(columns);
                    break;
            }
            
            Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List results = new ArrayList<>();
                try{
                    results = DatabaseHelper.view2(selectedTable);
                    tableViewChoose.getItems().setAll(results);
                }
                catch(SQLException e){
                    System.out.println(e);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                actiontarget.setText("Done");
            }
        };
        new Thread(task).start();
            
            
            
            
            
            //actiontarget.setVisible(false);
            //stage = (Stage)goBtn.getScene().getWindow();
            //root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            //stage.show();
        }
        
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
}
