package controllers.suppliers;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import objects.Supplier;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class ViewSupplierController {

    @FXML
    private TextField address;

    @FXML
    private TextField area;

    @FXML
    private TextField brandname;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField doy;

    @FXML
    private TextField email;

    @FXML
    private TextField faxnumber;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextArea notes;

    @FXML
    private TextField phone1;

    @FXML
    private TextField phone2;

    @FXML
    private TextField profession;

    @FXML
    private TextField surname;

    @FXML
    private TextField taxcode;

    @FXML
    private TextField website;

    @FXML
    private TextField zipcode;

    @FXML
    private TextField programUserField;

    @FXML
    private TextField vatregime_id_textfield;

    @FXML
    private TextField vatregime_textfield;

    @FXML
    private TextField payingway_id_textfield;

    @FXML
    private TextField payingway_textfield;

    @FXML
    private TextField pricelist_id_textfield;

    @FXML
    private TextField pricelist_textfield;

    @FXML
    private CheckBox checkDOYcheckbox;

    @FXML
    private CheckBox checkTAXcheckbox;

    private Supplier supplier;
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    @FXML
    private void CloseViewSupplierWindow(ActionEvent event) {

        Stage viewSupplierStage = (Stage) cancelButton.getScene().getWindow();
        viewSupplierStage.close();

    }

    public void getData(Supplier selectedSupplier){

        supplier = selectedSupplier;

        id.setText(String.valueOf(supplier.getSupplierID()));
        brandname.setText(supplier.getBrandname());
        profession.setText(supplier.getProfession());
        taxcode.setText(String.valueOf(supplier.getTaxcode()));
        doy.setText(supplier.getDOY());
        surname.setText(supplier.getSurname());
        name.setText(supplier.getName());
        address.setText(supplier.getAddress());
        zipcode.setText(String.valueOf(supplier.getZipcode()));
        area.setText(supplier.getArea());
        phone1.setText(String.valueOf(supplier.getPhone1()));
        phone2.setText(String.valueOf(supplier.getPhone2()));
        faxnumber.setText(String.valueOf(supplier.getFaxnumber()));
        email.setText(supplier.getEmail());
        website.setText(supplier.getWebsite());
        notes.setText(supplier.getNotes());
        payingway_id_textfield.setText(supplier.getPayingwayID());
        payingway_textfield.setText(commons.getSelectionDescription("paying_way", supplier.getPayingwayID())); //set the description by looking at its id
        pricelist_id_textfield.setText(supplier.getPricelistID());
        pricelist_textfield.setText(commons.getSelectionDescription("pricelist", supplier.getPricelistID())); //set the description by looking at its id
        vatregime_id_textfield.setText(supplier.getVatregimeID());
        vatregime_textfield.setText(commons.getSelectionDescription("vat_regime", supplier.getVatregimeID())); //set the description by looking at its id
        programUserField.setText(commons.insertedByUser(supplier.getUserID()));

        // check which checkbox was selected on insertion and display
        String supplier_id = String.valueOf(supplier.getSupplierID());

        switch(checkIfChecktaxcodeCeckboxIsSelected(supplier_id)){
            case 1:
                checkTAXcheckbox.setSelected(true);
                break;
            case 0:
                checkTAXcheckbox.setSelected(false);
                break;
        }

        switch(checkIfCheckdoyCeckboxIsSelected(supplier_id)){
            case 1:
                checkDOYcheckbox.setSelected(true);
                break;
            case 0:
                checkDOYcheckbox.setSelected(false);
                break;
        }

    }

    // create a method that selects the user's username by his ID
    public String insertedByUser(String user_id) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT username AS inserted_by FROM users " +
                "WHERE id =" + user_id;

        String username = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                username = rs.getString("inserted_by");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return username;
    }

    // create a method that selects the payingway description by its ID
    public String getPayingWayDescription(String payingwayID) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM paying_way " +
                "WHERE id =" + payingwayID;

        String description = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return description;
    }

    // create a method that selects the pricelist description by its ID
    public String getPricelistDescription(String pricelistID) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM pricelist " +
                "WHERE id =" + pricelistID;

        String description = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return description;
    }

    // create a method that selects the vat regime description by its ID
    public String getVatRegimeDescription(String vatregimeID) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM vat_regime " +
                "WHERE id =" + vatregimeID;

        String description = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return description;
    }

    public Integer checkIfChecktaxcodeCeckboxIsSelected(String supplierID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT check_taxcode FROM supplier " +
                "WHERE supplier_id =" + supplierID;

        Integer taxcodeCheck = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                taxcodeCheck = rs.getInt("check_taxcode");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return taxcodeCheck;

    }

    public Integer checkIfCheckdoyCeckboxIsSelected(String supplierID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT check_doy FROM supplier " +
                "WHERE supplier_id =" + supplierID;

        Integer doyCheck = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                doyCheck = rs.getInt("check_doy");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return doyCheck;
    }

}
