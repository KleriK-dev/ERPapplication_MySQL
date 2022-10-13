package controllers.selectionWindows;

import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.customers.EditCustomerController;
import controllers.customers.NewCustomerController;
import controllers.handwritedInvoices.NewHandwritedInvoiceController;
import controllers.supplierInvoices.NewSupplierInvoiceController;
import controllers.suppliers.EditSupplierController;
import controllers.suppliers.NewSupplierController;
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
import objects.PayingWay;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PayingWaySelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<PayingWay> table;

    @FXML
    private TableColumn<PayingWay, Integer> col_id;

    @FXML
    private TableColumn<PayingWay, String> col_payingway;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedPayingway;

    @FXML
    private TextField openedFromTextfield;

    ObservableList<PayingWay> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize(){

        loadData();

    }


    @FXML
    void AddPayingWay(ActionEvent event) {

        addPayingWay();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage payingwaySelectionStage = (Stage) cancelButton.getScene().getWindow();
        payingwaySelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedPayingway.setText(String.valueOf(col_payingway.getCellData(index)));
        }

        //on double click to a row, add paying way
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addPayingWay();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM paying_way";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new PayingWay(rs.getInt("id"), rs.getString("description")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("payingway_id"));
            col_payingway.setCellValueFactory(new PropertyValueFactory<>("payingway_description"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    // this paying selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void addPayingWay(){

        String payingwayID = selectedID.getText();
        String payingwayDescription = selectedPayingway.getText();

        if(openedFromTextfield.getText().equals("from new customer")){ // check from where this selection window is opened

            NewCustomerController.getInstance().getPayingWayData(payingwayID, payingwayDescription); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from edit customer")){

            EditCustomerController.getInstance().getPayingWayData(payingwayID, payingwayDescription); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from new supplier")){

            NewSupplierController.getInstance().getPayingWayData(payingwayID, payingwayDescription);

        } else if(openedFromTextfield.getText().equals("from edit supplier")){

            EditSupplierController.getInstance().getPayingWayData(payingwayID, payingwayDescription);

        } else if(openedFromTextfield.getText().equals("from new customer invoice")){

            NewCustomerInvoiceController.getInstance().getPayingWayData(payingwayID, payingwayDescription);

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice")){

            NewHandwritedInvoiceController.getInstance().getPayingWayData(payingwayID, payingwayDescription);

        } else if(openedFromTextfield.getText().equals("from new supplier invoice")){

            NewSupplierInvoiceController.getInstance().getPayingWayData(payingwayID, payingwayDescription);

        }

        Stage payingwaySelectionStage = (Stage) selectButton.getScene().getWindow();
        payingwaySelectionStage.close();

    }


}
