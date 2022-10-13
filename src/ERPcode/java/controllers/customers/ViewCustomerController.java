package controllers.customers;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import objects.Customer;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class ViewCustomerController {

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

    private Customer customer;
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    @FXML
    private void CloseViewCustomerWindow(ActionEvent event) {

        Stage viewCustomerStage = (Stage) cancelButton.getScene().getWindow();
        viewCustomerStage.close();

    }

    public void getData(Customer selectedCustomer){

        customer = selectedCustomer;

        id.setText(String.valueOf(customer.getCustomerID()));
        brandname.setText(customer.getBrandname());
        profession.setText(customer.getProfession());
        taxcode.setText(String.valueOf(customer.getTaxcode()));
        doy.setText(customer.getDOY());
        surname.setText(customer.getSurname());
        name.setText(customer.getName());
        address.setText(customer.getAddress());
        zipcode.setText(String.valueOf(customer.getZipcode()));
        area.setText(customer.getArea());
        phone1.setText(String.valueOf(customer.getPhone1()));
        phone2.setText(String.valueOf(customer.getPhone2()));
        faxnumber.setText(String.valueOf(customer.getFaxnumber()));
        email.setText(customer.getEmail());
        website.setText(customer.getWebsite());
        notes.setText(customer.getNotes());
        payingway_id_textfield.setText(customer.getPayingwayID());
        payingway_textfield.setText(commons.getSelectionDescription("paying_way" ,customer.getPayingwayID())); //set the description by looking at its id
        pricelist_id_textfield.setText(customer.getPricelistID());
        pricelist_textfield.setText(commons.getSelectionDescription("pricelist" ,customer.getPricelistID())); //set the description by looking at its id
        vatregime_id_textfield.setText(customer.getVatregimeID());
        vatregime_textfield.setText(commons.getSelectionDescription("vat_regime" ,customer.getVatregimeID())); //set the description by looking at its id
        programUserField.setText(commons.insertedByUser(customer.getUserID()));

        // check which checkbox was selected on insertion and display
        String customer_id = String.valueOf(customer.getCustomerID());

        switch(checkIfChecktaxcodeCeckboxIsSelected(customer_id)){
            case 1:
                checkTAXcheckbox.setSelected(true);
                break;
            case 0:
                checkTAXcheckbox.setSelected(false);
                break;
        }

        switch(checkIfCheckdoyCeckboxIsSelected(customer_id)){
            case 1:
                checkDOYcheckbox.setSelected(true);
                break;
            case 0:
                checkDOYcheckbox.setSelected(false);
                break;
        }


    }

    public Integer checkIfChecktaxcodeCeckboxIsSelected(String customerID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT check_taxcode FROM customer " +
                "WHERE customer_id =" + customerID;

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

    public Integer checkIfCheckdoyCeckboxIsSelected(String customerID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT check_doy FROM customer " +
                "WHERE customer_id =" + customerID;

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
