package controllers.items;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.Item;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class ViewItemController {

    @FXML
    private TextField barcode;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField code;

    @FXML
    private TextField costPrice;

    @FXML
    private TextField description;

    @FXML
    private TextField discount;

    @FXML
    private TextField id;

    @FXML
    private TextField measurement_id_textfield;

    @FXML
    private TextField measurement_textfield;

    @FXML
    private TextField retailPrice;

    @FXML
    private TextField supplier;

    @FXML
    private TextField supplierID;

    @FXML
    private TextField vatcategorie_id_textfield;

    @FXML
    private TextField vatcategorie_textfield;

    @FXML
    private TextField wholesalePrice;

    @FXML
    private TextField remaining;

    @FXML
    private CheckBox retailContainsVat;

    @FXML
    private CheckBox wholesaleContainsVat;

    Item item = new Item();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    @FXML
    void CloseNewItemWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    public void getData(Item selectedItem) {

        item = selectedItem;

        id.setText(String.valueOf(item.getItemID()));
        code.setText(item.getCode());
        description.setText(item.getDescription());
        supplierID.setText(String.valueOf(item.getSupplierID()));
        supplier.setText(item.getSupplierBrandname());
        barcode.setText(item.getBarcode());
        costPrice.setText(String.valueOf(item.getPurchase_price()));
        wholesalePrice.setText(String.valueOf(item.getWholesale_price()));
        retailPrice.setText(String.valueOf(item.getRetail_price()));
        discount.setText(String.valueOf(item.getDiscount()));
        vatcategorie_id_textfield.setText(String.valueOf(item.getVatCategorieID()));
        vatcategorie_textfield.setText(commons.getSelectionDescription("vat_categorie", String.valueOf(item.getVatCategorieID())));
        measurement_id_textfield.setText(String.valueOf(item.getMeasurementUnitID()));
        measurement_textfield.setText(commons.getSelectionDescription("measurement_unit", String.valueOf(item.getMeasurementUnitID())));
        remaining.setText(String.valueOf(item.getRemaining()));

        String item_id = String.valueOf(item.getItemID());

        switch(checkIfRetailVatIsSelected(item_id)){
            case 1:
                retailContainsVat.setSelected(true);
                break;
            case 0:
                retailContainsVat.setSelected(false);
                break;
        }

        switch(checkIfWholesaleVatIsSelected(item_id)){
            case 1:
                wholesaleContainsVat.setSelected(true);
                break;
            case 0:
                wholesaleContainsVat.setSelected(false);
                break;
        }

    }

    // create a method that selects the vat categorie description by its ID
//    public String getVatCategorieDescription(String vatcategorieID) {
//
//        Connection conn = MySQLConnection.connectToDB();
//        String query = "SELECT description FROM vat_categorie " +
//                "WHERE id =" + vatcategorieID;
//
//        String description = null;
//
//        PreparedStatement prst;
//        Statement st;
//        ResultSet rs;
//
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            while (rs.next()) {
//                description = rs.getString("description");
//            }
//        } catch (SQLException e) {
//            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
//        }
//        return description;
//    }
//
//    // create a method that selects the measurement unit description by its ID
//    public String getMeasurementDescription(String measurementID) {
//
//        Connection conn = MySQLConnection.connectToDB();
//        String query = "SELECT description FROM measurement_unit " +
//                "WHERE id =" + measurementID;
//
//        String description = null;
//
//        PreparedStatement prst;
//        Statement st;
//        ResultSet rs;
//
//        try {
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            while (rs.next()) {
//                description = rs.getString("description");
//            }
//        } catch (SQLException e) {
//            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
//        }
//        return description;
//    }

    public Integer checkIfRetailVatIsSelected(String itemID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT retail_contains_vat FROM items " +
                "WHERE id =" + itemID;

        Integer retailVat = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retailVat = rs.getInt("retail_contains_vat");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return retailVat;

    }

    public Integer checkIfWholesaleVatIsSelected(String itemID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT wholesale_contains_vat FROM items " +
                "WHERE id =" + itemID;

        Integer wholesaleVat = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                wholesaleVat = rs.getInt("wholesale_contains_vat");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return wholesaleVat;
    }

}
