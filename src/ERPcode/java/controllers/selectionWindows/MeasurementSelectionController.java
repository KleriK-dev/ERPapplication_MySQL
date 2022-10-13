package controllers.selectionWindows;

import controllers.items.EditItemController;
import controllers.items.NewItemController;
import database.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.MeasurementUnit;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeasurementSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<MeasurementUnit> table;

    @FXML
    private TableColumn<MeasurementUnit, String> col_code;

    @FXML
    private TableColumn<MeasurementUnit, Integer> col_id;

    @FXML
    private TableColumn<MeasurementUnit, String> col_measurement;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedMeasurement;

    ObservableList<MeasurementUnit> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize(){

        loadData();

    }

    @FXML
    void AddMeasurement(ActionEvent event) {

        addMeasurement();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage measurementSelectionStage = (Stage) cancelButton.getScene().getWindow();
        measurementSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedMeasurement.setText(String.valueOf(col_measurement.getCellData(index)));
        }

        //on double click to a row, add measurement
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addMeasurement();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM measurement_unit";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new MeasurementUnit(rs.getInt("id"), rs.getString("code"), rs.getString("description")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("measurement_id"));
            col_code.setCellValueFactory(new PropertyValueFactory<>("measurement_code"));
            col_measurement.setCellValueFactory(new PropertyValueFactory<>("measurement_description"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    // this measurement selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void addMeasurement(){

        String measurementID = selectedID.getText();
        String measurementDescription = selectedMeasurement.getText();

        if(openedFromTextfield.getText().equals("from new item")){ // check from where this selection window is opened

            NewItemController.getInstance().getMeasurementData(measurementID, measurementDescription); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from edit item")){

            EditItemController.getInstance().getMeasurementData(measurementID, measurementDescription);

        }

        Stage measurementSelectionStage = (Stage) cancelButton.getScene().getWindow();
        measurementSelectionStage.close();

    }

}
