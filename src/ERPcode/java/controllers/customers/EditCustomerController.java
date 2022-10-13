package controllers.customers;

import commons.CommonMethods;
import controllers.selectionWindows.PayingWaySelectionController;
import controllers.selectionWindows.PriceListSelectionController;
import controllers.selectionWindows.VATregimeSelectionController;
import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.Customer;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class EditCustomerController {

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
    private Button instructionsButton;

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
    private Button VATregimeSearchButton;

    @FXML
    private Button payingMethodSearchButton;

    @FXML
    private Button pricelistSearchButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField website;

    @FXML
    private TextField zipcode;

    @FXML
    private CheckBox checkTAXcheckbox;

    @FXML
    private CheckBox checkDOYcheckbox;

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

    private Customer customer;
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    /* From line 117 to line 123
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static EditCustomerController instance;

    public EditCustomerController(){
        instance = this;
    }

    public static EditCustomerController getInstance(){
        return instance;
    }

    @FXML
    private void CloseEditCustomerWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    private void OpenInstructionsWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/CustomerInstructions.fxml"));
        Parent root = loader.load();

        Stage editCustomerInstructionStage = new Stage();
        Scene editCustomerInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        editCustomerInstructionStage.getIcons().add(image);
        editCustomerInstructionStage.setX(303);
        editCustomerInstructionStage.setY(106);
        editCustomerInstructionStage.setScene(editCustomerInstructionScene);
        editCustomerInstructionStage.setTitle("Instructions");
        editCustomerInstructionStage.setAlwaysOnTop(true);
        editCustomerInstructionStage.show();

    }

    @FXML
    private void UpdateCustomer(ActionEvent event) {

        //store textfields to string and int variables
        int selectedid = Integer.parseInt(id.getText());
        String newbrandname = brandname.getText().trim();
        String newprofession = profession.getText().trim();
        String newtaxcode = taxcode.getText().trim();
        String newdoy = doy.getText().trim();
        String newaddress = address.getText().trim();
        String newzipcode = zipcode.getText().trim();
        String newarea = area.getText().trim();
        String newname = name.getText().trim();
        String newsurname = surname.getText().trim();
        String newphone1 = phone1.getText().trim();
        String newphone2 = phone2.getText().trim();
        String newfaxnumber = faxnumber.getText().trim();
        String newemail = email.getText().trim();
        String newwebsite = website.getText().trim();
        String newnotes = notes.getText();
        String vatregimeID = vatregime_id_textfield.getText();
        String pricelistID = pricelist_id_textfield.getText();
        String payingwayID = payingway_id_textfield.getText();
        // the user that inserted the customer has to be kept even tho is edited by other user
        // thats why we get the username of the textfield and insert it again on database
        String insertedByUser = programUserField.getText();

        // CHECKS FOR TAX AND DOY
        /*
         * If only check tax is selected then with other if else
         * check the tax code field and then the brandname field,
         * as it's IMPORTANT to be there
         * and at last call addCustomer method from customer class
         * */
        if (checkTAXcheckbox.isSelected() && !checkDOYcheckbox.isSelected()) {

            if (newtaxcode.length() < 9) {
                alert.show("Warning", "TAX code has less than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() > 9) {
                alert.show("Warning", "TAX code has more than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() == 9) {

                if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add customer's brandname!", Alert.AlertType.WARNING);
                } else {

                    Customer customer = new Customer(selectedid, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregimeID, payingwayID, pricelistID, insertedByUser, 1, 0); // pass them to customer object

                    if(customer.checkIfCustomerBrandnameExistsOnUpdate()){
                        alert.show("Warning", "Customer with this brandname exists!", Alert.AlertType.WARNING);
                    } else if(customer.checkIfCustomerTaxcodeExistsOnUpdate()){
                        alert.show("Warning", "Customer with this taxcode exists!", Alert.AlertType.WARNING);
                    } else {

                        customer.editCustomer();

                        Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the CustomersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        CustomersMainController.getInstance().RefreshData();

                    }
                }

            }

        }
        /*
         * Else if only check doy is selected then with other if else
         * check doy field and then the brandname field,
         * as it's IMPORTANT to be there
         * and at last call addCustomer method from customer class
         * */
        else if (!checkTAXcheckbox.isSelected() && checkDOYcheckbox.isSelected()) {

            if (newdoy.equals("")) {
                alert.show("Warning", "Empty DOY! Please add customer's DOY!", Alert.AlertType.WARNING);
            } else {
                if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add customer's brandname!", Alert.AlertType.WARNING);
                } else {

                    Customer customer = new Customer(selectedid, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregimeID, payingwayID, pricelistID, insertedByUser, 0, 1); // pass them to customer object

                    if(customer.checkIfCustomerBrandnameExistsOnUpdate()){
                        alert.show("Warning", "Customer with this brandname exists!", Alert.AlertType.WARNING);
                    } else {

                        customer.editCustomer();

                        Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the CustomersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        CustomersMainController.getInstance().RefreshData();

                    }
                }

            }

        }
        /*
         * Else if both checkboxes are selected,
         * first check tax code field,
         * then check doy field,
         * then brandname field
         * and at last if everything is correct, call addCustomer method from customer class
         * */
        else if (checkTAXcheckbox.isSelected() && checkDOYcheckbox.isSelected()) {

            if (newtaxcode.length() < 9) {
                alert.show("Warning", "TAX code has less than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() > 9) {
                alert.show("Warning", "TAX code has more than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() == 9) {

                if (newdoy.equals("")) {
                    alert.show("Warning", "Empty DOY! Please add customer's DOY!", Alert.AlertType.WARNING);
                } else if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add customer's brandname!", Alert.AlertType.WARNING);
                } else {

                    Customer customer = new Customer(selectedid, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregimeID, payingwayID, pricelistID, insertedByUser, 1, 1); // pass them to customer object

                    if(customer.checkIfCustomerBrandnameExistsOnUpdate()){
                        alert.show("Warning", "Customer with this brandname exists!", Alert.AlertType.WARNING);
                    } else if(customer.checkIfCustomerTaxcodeExistsOnUpdate()){
                        alert.show("Warning", "Customer with this taxcode exists!", Alert.AlertType.WARNING);
                    } else {

                        customer.editCustomer();

                        Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the CustomersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        CustomersMainController.getInstance().RefreshData();

                    }
                }

            }

        }
        /*
         * Else if none of the checkboxes is selected,
         * just check if it has a brand name
         * */
        else if (!checkTAXcheckbox.isSelected() && !checkDOYcheckbox.isSelected()) {

            if (newbrandname.equals("")) {
                alert.show("Warning", "Empty brandname! Please add customer's brandname!", Alert.AlertType.WARNING);
            } else {

                Customer customer = new Customer(selectedid, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregimeID, payingwayID, pricelistID, insertedByUser, 0, 0); // pass them to customer object

                if(customer.checkIfCustomerBrandnameExistsOnUpdate()){
                    alert.show("Warning", "Customer with this brandname exists!", Alert.AlertType.WARNING);
                } else {

                    customer.editCustomer();

                    Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
                    stage.close();

                    // use RefreshData method of the CustomersMainController class from this controller
                    // so it can load the edited row on tableview without pressing refresh button
                    CustomersMainController.getInstance().RefreshData();

                }
            }

        }

    }

    public void getData(Customer selectedCustomer) {

        customer = selectedCustomer;

        id.setText(String.valueOf(customer.getCustomerID()));
        brandname.setText(customer.getBrandname());
        profession.setText(customer.getProfession());
        taxcode.setText(customer.getTaxcode());
        doy.setText(customer.getDOY());
        surname.setText(customer.getSurname());
        name.setText(customer.getName());
        address.setText(customer.getAddress());
        zipcode.setText(customer.getZipcode());
        area.setText(customer.getArea());
        phone1.setText(customer.getPhone1());
        phone2.setText(customer.getPhone2());
        faxnumber.setText(customer.getFaxnumber());
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


    @FXML
    private void OpenPayingSelectionWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/PayingWaySelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        PayingWaySelectionController payingWaySelectionController = loader.getController();
        payingWaySelectionController.openedFrom("from edit customer");

        Stage vatregimeStage = new Stage();
        Scene vatregimeScene = new Scene(root);
        vatregimeStage.setX(220);
        vatregimeStage.setY(310);
        vatregimeStage.setScene(vatregimeScene);
        vatregimeStage.initStyle(StageStyle.UNDECORATED);
        vatregimeStage.initModality(Modality.APPLICATION_MODAL);
        vatregimeStage.setAlwaysOnTop(true);
        vatregimeStage.setResizable(false);
        vatregimeStage.show();

    }

    @FXML
    private void OpenVATregimeSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/VATregimeSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        VATregimeSelectionController vaTregimeSelectionController = loader.getController();
        vaTregimeSelectionController.openedFrom("from edit customer");

        Stage vatregimeStage = new Stage();
        Scene vatregimeScene = new Scene(root);
        vatregimeStage.setX(220);
        vatregimeStage.setY(230);
        vatregimeStage.setScene(vatregimeScene);
        vatregimeStage.initStyle(StageStyle.UNDECORATED);
        vatregimeStage.initModality(Modality.APPLICATION_MODAL);
        vatregimeStage.setAlwaysOnTop(true);
        vatregimeStage.setResizable(false);
        vatregimeStage.show();

    }

    @FXML
    private void OpenPricelistSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/PriceListSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        PriceListSelectionController priceListSelectionController = loader.getController();
        priceListSelectionController.openedFrom("from edit customer");

        Stage vatregimeStage = new Stage();
        Scene vatregimeScene = new Scene(root);
        vatregimeStage.setX(220);
        vatregimeStage.setY(260);
        vatregimeStage.setScene(vatregimeScene);
        vatregimeStage.initStyle(StageStyle.UNDECORATED);
        vatregimeStage.initModality(Modality.APPLICATION_MODAL);
        vatregimeStage.setAlwaysOnTop(true);
        vatregimeStage.setResizable(false);
        vatregimeStage.show();

    }

    public void getVatRegimeData(String vatregime_id, String vatregime){

        vatregime_id_textfield.setText(vatregime_id);
        vatregime_textfield.setText(vatregime);

    }

    public void getPayingWayData(String payingway_id, String payingway){

        payingway_id_textfield.setText(payingway_id);
        payingway_textfield.setText(payingway);

    }

    public void getPricelistData(String payingway_id, String payingway){

        pricelist_id_textfield.setText(payingway_id);
        pricelist_textfield.setText(payingway);

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
