package controllers.selectionWindows;

import controllers.items.EditItemController;
import controllers.items.NewItemController;
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
import objects.Supplier;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class SupplierSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Supplier> table;

    @FXML
    private TableColumn<Supplier, String> col_brandname;

    @FXML
    private TableColumn<Supplier, Integer> col_id;

    @FXML
    private TableColumn<Supplier, String> col_profession;

    @FXML
    private TableColumn<Supplier, String> col_taxcode;

    @FXML
    private TableColumn<Supplier, String> col_payingwayID;

    @FXML
    private TableColumn<Supplier, String> col_vatregimeID;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedSupplier;

    @FXML
    private TextField supplierPayingWayID;

    @FXML
    private TextField supplierVatregimeID;

    ObservableList<Supplier> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    void AddSupplier(ActionEvent event) {

        addSupplier();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage supplierSelectionStage = (Stage) cancelButton.getScene().getWindow();
        supplierSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedSupplier.setText(col_brandname.getCellData(index));
            supplierVatregimeID.setText(col_vatregimeID.getCellData(index));
            supplierPayingWayID.setText(col_payingwayID.getCellData(index));
        }

        //on double click to a row, add supplier
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addSupplier();

                }
            }
        });

    }

    public void loadData(String inputedBrandname){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM supplier WHERE brandname LIKE ? AND NOT supplier_id = 0 ";
        ResultSet rs = null;
        PreparedStatement prst = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, "%" + inputedBrandname + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                oblist.add(new Supplier(rs.getInt("supplier_id"), rs.getString("brandname"), rs.getString("profession"), rs.getString("taxcode"), null, null, null, null, null, null, null, null, null, null, null, null, rs.getString("vatregime_id"), rs.getString("payingway_id"), null, null, null, null));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
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

    // this supplier selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void addSupplier(){

        String supplierID = selectedID.getText();
        String supplierBrandname = selectedSupplier.getText();
        String supplierVatregime = supplierVatregimeID.getText();
        String supplierPayment = supplierPayingWayID.getText();

        if(openedFromTextfield.getText().equals("from new item")){

            NewItemController.getInstance().getSupplierData(supplierID, supplierBrandname); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from edit item")){

            EditItemController.getInstance().getSupplierData(supplierID, supplierBrandname);

        } else if(openedFromTextfield.getText().equals("from new supplier invoice")){

            NewSupplierInvoiceController.getInstance().getSupplierData(supplierID, supplierBrandname, supplierVatregime, supplierPayment);

        } else if(openedFromTextfield.getText().equals("from edit supplier invoice")){

            //CopySupplierInvoiceController.getInstance().getSupplierData(supplierID, supplierBrandname, supplierVatregime, supplierPayment);

        }

        Stage supplierSelectionStage = (Stage) cancelButton.getScene().getWindow();
        supplierSelectionStage.close();

    }

}
