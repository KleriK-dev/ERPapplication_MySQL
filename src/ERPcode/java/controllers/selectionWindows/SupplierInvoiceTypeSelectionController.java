package controllers.selectionWindows;

import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.handwritedInvoices.NewHandwritedInvoiceController;
import controllers.supplierInvoices.NewSupplierInvoiceController;
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
import objects.SupplierInvoiceType;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierInvoiceTypeSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<SupplierInvoiceType> table;

    @FXML
    private TableColumn<SupplierInvoiceType, String> col_abbreviation;

    @FXML
    private TableColumn<SupplierInvoiceType, String> col_description;

    @FXML
    private TableColumn<SupplierInvoiceType, Integer> col_id;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedInvoiceType;

    ObservableList<SupplierInvoiceType> oblist = FXCollections.observableArrayList();
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

    // this invoice type selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM type_of_supplier_document";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new SupplierInvoiceType(rs.getInt("id"), rs.getString("abbreviation"), rs.getString("description")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("invoiceTypeID"));
            col_abbreviation.setCellValueFactory(new PropertyValueFactory<>("abbreviationOfInvoiceType"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

    }

    public void addInvoiceType(){

        String invoiceTypeID = selectedID.getText();
        String invoiceTypeDescription = selectedInvoiceType.getText();

        if(openedFromTextfield.getText().equals("from new supplier invoice")){ // check from where this selection window is opened

            NewSupplierInvoiceController.getInstance().getInvoiceTypeData(invoiceTypeID, invoiceTypeDescription); // pass the data to that controller

        }

        Stage invoiceTypeSelectionStage = (Stage) cancelButton.getScene().getWindow();
        invoiceTypeSelectionStage.close();

    }

}
