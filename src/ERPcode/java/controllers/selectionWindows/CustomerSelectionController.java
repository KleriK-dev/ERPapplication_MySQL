package controllers.selectionWindows;

import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.handwritedInvoices.NewHandwritedInvoiceController;
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
import objects.Customer;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Customer> table;

    @FXML
    private TableColumn<Customer, String> col_brandname;

    @FXML
    private TableColumn<Customer, Integer> col_id;

    @FXML
    private TableColumn<Customer, String> col_profession;

    @FXML
    private TableColumn<Customer, String> col_taxcode;

    @FXML
    private TableColumn<Customer, String> col_vatregimeID;

    @FXML
    private TableColumn<Customer, String> col_payingwayID;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private TextField customerVatregimeID;

    @FXML
    private TextField customerPayingWayID;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedCustomer;

    @FXML
    private TextField selectedID;

    ObservableList<Customer> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    void AddCustomer(ActionEvent event) {

        addCustomer();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage customerSelectionStage = (Stage) cancelButton.getScene().getWindow();
        customerSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedCustomer.setText(col_brandname.getCellData(index));
            customerVatregimeID.setText(col_vatregimeID.getCellData(index));
            customerPayingWayID.setText(col_payingwayID.getCellData(index));
        }

        //on double click to a row, add customer
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addCustomer();

                }
            }
        });

    }

    public void loadData(String inputedBrandname){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM customer WHERE brandname LIKE ? ";

        ResultSet rs = null;
        PreparedStatement prst = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, "%" + inputedBrandname + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                oblist.add(new Customer(rs.getInt("customer_id"), rs.getString("brandname"), rs.getString("profession"), rs.getString("taxcode"), null, null, null, null, null, null, null, null, null, null, null, null, rs.getString("vatregime_id"), rs.getString("payingway_id"), null, null, null, null));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            col_brandname.setCellValueFactory(new PropertyValueFactory<>("brandname"));
            col_taxcode.setCellValueFactory(new PropertyValueFactory<>("taxcode"));
            col_profession.setCellValueFactory(new PropertyValueFactory<>("profession"));
            col_vatregimeID.setCellValueFactory(new PropertyValueFactory<>("vatregimeID"));
            col_payingwayID.setCellValueFactory(new PropertyValueFactory<>("payingwayID"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    // this customer selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    //create a method that adds customer, so it can be used in other methods
    public void addCustomer(){

        String customerID = selectedID.getText();
        String customerBrandname = selectedCustomer.getText();
        String vatregimeID = customerVatregimeID.getText();
        String payingWayID =customerPayingWayID.getText();

        if(openedFromTextfield.getText().equals("from new customer invoice")){

            NewCustomerInvoiceController.getInstance().getCustomerData(customerID, customerBrandname, vatregimeID, payingWayID); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice")){

            NewHandwritedInvoiceController.getInstance().getCustomerData(customerID, customerBrandname, vatregimeID, payingWayID); // pass the data to that controller

        }

        Stage customerSelectionStage = (Stage) cancelButton.getScene().getWindow();
        customerSelectionStage.close();

    }

}
