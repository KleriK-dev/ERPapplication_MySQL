package controllers.selectionWindows;

import commons.CommonMethods;
import controllers.customerInvoices.AddItemController;
import controllers.customerInvoices.EditItemController;
import controllers.handwritedInvoices.AddItemHandController;
import controllers.handwritedInvoices.EditItemHandController;
import controllers.supplierInvoices.AddSuppItemController;
import controllers.supplierInvoices.EditSuppItemController;
import database.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Customer;
import objects.Item;
import objects.PriceList;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class ItemSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item, String> col_barcode;

    @FXML
    private TableColumn<Item, String> col_code;

    @FXML
    private TableColumn<Item, String> col_description;

    @FXML
    private TableColumn<Item, String> col_supplier;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedCode;

    @FXML
    private TextField customerORsupplier_id;

    ObservableList<Item> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1;

    @FXML
    void SelectItem(ActionEvent event) {

        selectItem();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage itemSelectionStage = (Stage) cancelButton.getScene().getWindow();
        itemSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedCode.setText(String.valueOf(col_code.getCellData(index))); // else set on the selected code textfield the code that exists on this spesific index of tableview
        }

        //on double click to a row, select item
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                        selectItem();

                }
            }
        });

    }

    public void loadDataByCode(String inputedItemCode){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, supplier.brandname AS supplierName FROM items " +
                "INNER JOIN supplier ON supplier.supplier_id = items.supplierID" +
                " WHERE code LIKE ? ";

        ResultSet rs = null;
        PreparedStatement prst = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, "%" + inputedItemCode + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                oblist.add(new Item(null, rs.getString("code"), rs.getString("description"), rs.getString("barcode"), 0, 0, 0, 0, 0, rs.getInt("supplierID"), rs.getString("supplierName"), null, null, null, null));
            }

            col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            col_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierBrandname"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);

        }

    }

    public void loadDataByBarcode(String inputedBarcode){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, supplier.brandname AS supplierName FROM items " +
                "INNER JOIN supplier ON supplier.supplier_id = items.supplierID" +
                " WHERE barcode LIKE ? ";

        ResultSet rs = null;
        PreparedStatement prst = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, "%" + inputedBarcode + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                oblist.add(new Item(null, rs.getString("code"), rs.getString("description"), rs.getString("barcode"), 0, 0, 0, 0, 0, rs.getInt("supplierID"), rs.getString("supplierName"), null, null, null, null));
            }

            col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            col_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierBrandname"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);

        }

    }

    public void loadDataByDescription(String inputedDescription){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, supplier.brandname AS supplierName FROM items " +
                "INNER JOIN supplier ON supplier.supplier_id = items.supplierID" +
                " WHERE description LIKE ? ";

        ResultSet rs = null;
        PreparedStatement prst = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, "%" + inputedDescription + "%");
            rs = prst.executeQuery();
            while (rs.next()) {
                oblist.add(new Item(null, rs.getString("code"), rs.getString("description"), rs.getString("barcode"), 0, 0, 0, 0, 0, rs.getInt("supplierID"), rs.getString("supplierName"), null, null, null, null));
            }

            col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            col_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierBrandname"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);

        }

    }

    // this customer selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void getCustomerOrSupplierID(String id){

        customerORsupplier_id.setText(id);

    }


    public void selectItem(){

        String selectedItemCode = selectedCode.getText(); // save as variable the selected code, so we can use it in the selection query
        String ID = customerORsupplier_id.getText();
        String tableName = null;
        String columnName = null;

        if(openedFromTextfield.getText().contains("customer") || openedFromTextfield.getText().contains("handwrited")){

             tableName = "customer";
             columnName = "customer_id";

        } else {

             tableName = "supplier";
             columnName = "supplier_id";

        }


        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, vat_categorie.vat_percentage AS vatpercentage FROM items " +
                "INNER JOIN vat_categorie ON vat_categorie.id = items.vat_categorieID " +
                "WHERE code = '" + selectedItemCode + "'";
        Statement st;
        ResultSet rs;

        String itemDescription = "";
        String itemBarcode = "";
        Double itemDiscount = 0.0;
        Double itemVAT = 0.0;
        Double itemPrice = 0.0;
        Double itemQuantity = 1.0;
        String itemEtiology = "";
        Double totalPrice = 0.0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                itemDescription = rs.getString("description");
                itemBarcode = rs.getString("barcode");
                itemDiscount = rs.getDouble("discount");

                if (commons.checkCustomerOrSupplierVATregime(tableName, columnName, ID) == 2) { // if customer has zero vat regime set 0.0 to vat percentage
                    itemVAT = 0.0;
                } else if (commons.checkCustomerOrSupplierVATregime(tableName, columnName, ID) == 3) { // else if customer has reduced vat set the 30% of the item vat percentage to vat percent

                    double currentItemVat = rs.getDouble("vatpercentage");

                    double s = 70;

                    itemVAT = Double.valueOf(Math.round((s * currentItemVat) / 100));

                } else if (commons.checkCustomerOrSupplierVATregime(tableName, columnName, ID) == 1) { // else if customer has normal vat regime, set the vat percentage of item
                    itemVAT = rs.getDouble("vatpercentage");
                }

                //depends on customer or supplier pricelist, we add the column that the itemPrice will get
                itemPrice = rs.getDouble(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList(tableName, columnName, ID)); // thats why we use the method from common methods that checks what pricelist has the selected customer or supplier

                double s = 100 - itemDiscount;

                totalPrice = (s * itemPrice) / 100;

            }

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);

        }

        if(openedFromTextfield.getText().equals("from new customer invoice add item")){

            AddItemController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        } else if(openedFromTextfield.getText().equals("from new customer invoice edit item")){

            EditItemController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice add item")){

            AddItemHandController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice edit item")){

            EditItemHandController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        } else if(openedFromTextfield.getText().equals("from new supplier invoice add item")){

            AddSuppItemController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        } else if(openedFromTextfield.getText().equals("from new supplier invoice edit item")){

            EditSuppItemController.getInstance().getItemData(selectedItemCode, itemDescription, itemQuantity, itemPrice, itemBarcode, itemDiscount, itemVAT, itemEtiology, totalPrice);

        }

        Stage itemSelectionStage = (Stage) cancelButton.getScene().getWindow();
        itemSelectionStage.close();

    }

}
