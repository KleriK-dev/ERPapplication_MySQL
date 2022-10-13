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
import objects.CustomerInvoiceType;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InvoiceTypeSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<CustomerInvoiceType> table;

    @FXML
    private TableColumn<CustomerInvoiceType, String> col_abbreviation;

    @FXML
    private TableColumn<CustomerInvoiceType, String> col_description;

    @FXML
    private TableColumn<CustomerInvoiceType, Integer> col_id;

    @FXML
    private TableColumn<CustomerInvoiceType, Integer> col_deliveryCheck;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedInvoiceType;

    @FXML
    private TextField delivery;

    ObservableList<CustomerInvoiceType> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize(){

        loadData();

    }

    @FXML
    void AddInvoiceType(ActionEvent event) {

        addInvoiceType();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage invoiceTypeSelectionStage = (Stage) cancelButton.getScene().getWindow();
        invoiceTypeSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedInvoiceType.setText(col_description.getCellData(index));
            delivery.setText(String.valueOf(col_deliveryCheck.getCellData(index)));
        }

        //on double click to a row, add invoice type
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addInvoiceType();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM type_of_customer_document WHERE NOT id = 5"; // we dont want to show Special Cancellation as an option
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new CustomerInvoiceType(rs.getInt("id"), rs.getString("abbreviation"), rs.getString("description"), rs.getInt("delivery")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("invoiceTypeID"));
            col_abbreviation.setCellValueFactory(new PropertyValueFactory<>("abbreviationOfInvoiceType"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_deliveryCheck.setCellValueFactory(new PropertyValueFactory<>("delivery_check"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    // this invoice type selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void addInvoiceType(){

        String invoiceTypeID = selectedID.getText();
        String invoiceTypeDescription = selectedInvoiceType.getText();
        String deliveryCheck = delivery.getText();

        if(openedFromTextfield.getText().equals("from new customer invoice")){ // check from where this selection window is opened

            NewCustomerInvoiceController.getInstance().getInvoiceTypeData(invoiceTypeID, invoiceTypeDescription, deliveryCheck); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice")){

            NewHandwritedInvoiceController.getInstance().getInvoiceTypeData(invoiceTypeID, invoiceTypeDescription, deliveryCheck); // pass the data to that controller

        }

        Stage invoiceTypeSelectionStage = (Stage) cancelButton.getScene().getWindow();
        invoiceTypeSelectionStage.close();

    }

}
