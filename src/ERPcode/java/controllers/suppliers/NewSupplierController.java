package controllers.suppliers;

import controllers.selectionWindows.PayingWaySelectionController;
import controllers.selectionWindows.PriceListSelectionController;
import controllers.selectionWindows.VATregimeSelectionController;
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
import objects.Supplier;
import specialAlerts.SpecialAlert;

import java.io.IOException;

public class NewSupplierController {

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
    private Button VATregimeSearchButton;

    @FXML
    private Button payingMethodSearchButton;

    @FXML
    private Button pricelistSearchButton;

    @FXML
    private Button saveButton;

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
    private CheckBox checkTAXcheckbox;

    @FXML
    private CheckBox checkDOYcheckbox;

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
    private TextField id;

    SpecialAlert alert = new SpecialAlert();

    /* From line 104 to line 116
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static NewSupplierController instance;

    public NewSupplierController(){
        instance = this;
    }

    public static NewSupplierController getInstance(){
        return instance;
    }

    @FXML
    private void CloseNewSupplierWindow(ActionEvent event) {

        Stage supplierStage = (Stage) cancelButton.getScene().getWindow();
        supplierStage.close();

    }

    @FXML
    private void OpenInstructions(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/SupplierInstructions.fxml"));
        Parent root = loader.load();

        Stage newSupplierInstructionStage = new Stage();
        Scene newSupplierInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newSupplierInstructionStage.getIcons().add(image);
        newSupplierInstructionStage.setX(303);
        newSupplierInstructionStage.setY(106);
        newSupplierInstructionStage.setScene(newSupplierInstructionScene);
        newSupplierInstructionStage.setTitle("Instructions");
        newSupplierInstructionStage.setAlwaysOnTop(true);
        newSupplierInstructionStage.show();

    }

    public void showProgramUser(String userName){ //display who is the user

        programUserField.setText(userName);

    }

    @FXML
    private void SaveNewSupplier(ActionEvent event) {

        //store textfields to string and int variables
        int newSupID = Integer.parseInt(id.getText());
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
        String programUser = programUserField.getText();
        String vatregime_id = vatregime_id_textfield.getText();
        String pricelist_id = pricelist_id_textfield.getText();
        String payingway_id = payingway_id_textfield.getText();

        // CHECKS FOR TAX AND DOY
        /*
         * If only check tax is selected then with other if else
         * check the tax code field and then the brandname field,
         * as it's IMPORTANT to be there
         * and at last call addSupplier method from supplier class
         * */
        if (checkTAXcheckbox.isSelected() && !checkDOYcheckbox.isSelected()) {

            if (newtaxcode.length() < 9) {
                alert.show("Warning", "TAX code has less than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() > 9) {
                alert.show("Warning", "TAX code has more than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() == 9) {

                if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add supplier's brandname!", Alert.AlertType.WARNING);
                } else {

                    Supplier supplier = new Supplier(newSupID, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregime_id, payingway_id, pricelist_id, programUser, 1, 0); // pass them to supplier object

                    if(supplier.checkIfSupplierBrandnameExists()){
                        alert.show("Warning", "Supplier with this brandname exists!", Alert.AlertType.WARNING);
                    } else if(supplier.checkIfSupplierTaxcodeExists()){
                        alert.show("Warning", "Supplier with this taxcode exists!", Alert.AlertType.WARNING);
                    } else {

                        supplier.addSupplier();

                        Stage stage = (Stage) saveButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the SuppliersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        SupplierMainController.getInstance().RefreshData();

                    }

                }

            }

        }
        /*
         * Else if only check doy is selected then with other if else
         * check doy field and then the brandname field,
         * as it's IMPORTANT to be there
         * and at last call addSupplier method from Supplier class
         * */
        else if (!checkTAXcheckbox.isSelected() && checkDOYcheckbox.isSelected()) {

            if (newdoy.equals("")) {
                alert.show("Warning", "Empty DOY! Please add supplier's DOY!", Alert.AlertType.WARNING);
            } else {
                if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add supplier's brandname!", Alert.AlertType.WARNING);
                } else {

                    Supplier supplier = new Supplier(newSupID, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregime_id, payingway_id, pricelist_id, programUser, 0, 1); // pass them to Supplier object

                    if(supplier.checkIfSupplierBrandnameExists()){
                        alert.show("Warning", "Supplier with this brandname exists!", Alert.AlertType.WARNING);
                    } else {

                        supplier.addSupplier();

                        Stage stage = (Stage) saveButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the SuppliersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        SupplierMainController.getInstance().RefreshData();

                    }


                }

            }

        }
        /*
         * Else if both checkboxes are selected,
         * first check tax code field,
         * then check doy field,
         * then brandname field
         * and at last if everything is correct, call addSupplier method from Supplier class
         * */

        else if (checkTAXcheckbox.isSelected() && checkDOYcheckbox.isSelected()){

            if (newtaxcode.length() < 9) {
                alert.show("Warning", "TAX code has less than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() > 9) {
                alert.show("Warning", "TAX code has more than 9 numbers!", Alert.AlertType.WARNING);
            } else if (newtaxcode.length() == 9) {

                if (newdoy.equals("")) {
                    alert.show("Warning", "Empty DOY! Please add supplier's DOY!", Alert.AlertType.WARNING);
                } else if (newbrandname.equals("")) {
                    alert.show("Warning", "Empty brandname! Please add supplier's brandname!", Alert.AlertType.WARNING);
                } else {

                    Supplier supplier = new Supplier(newSupID, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregime_id, payingway_id, pricelist_id, programUser, 1, 1); // pass them to Supplier object

                    if(supplier.checkIfSupplierBrandnameExists()){
                        alert.show("Warning", "Supplier with this brandname exists!", Alert.AlertType.WARNING);
                    } else if(supplier.checkIfSupplierTaxcodeExists()){
                        alert.show("Warning", "Supplier with this taxcode exists!", Alert.AlertType.WARNING);
                    } else {

                        supplier.addSupplier();

                        Stage stage = (Stage) saveButton.getScene().getWindow(); // close this stage
                        stage.close();

                        // use RefreshData method of the SuppliersMainController class from this controller
                        // so it can load the edited row on tableview without pressing refresh button
                        SupplierMainController.getInstance().RefreshData();

                    }


                }

            }

        }
        /*
         * Else if none of the checkboxes is selected,
         * just check if it has a brand name
         * */
        else if (!checkTAXcheckbox.isSelected() && !checkDOYcheckbox.isSelected()){

            if (newbrandname.equals("")) {
                alert.show("Warning", "Empty brandname! Please add supplier's brandname!", Alert.AlertType.WARNING);
            } else {

                Supplier supplier = new Supplier(newSupID, newbrandname, newprofession, newtaxcode, newaddress, newarea, newzipcode, newdoy, newsurname, newname, newphone1, newphone2, newfaxnumber, newemail, newwebsite, newnotes, vatregime_id, payingway_id, pricelist_id, programUser, 0, 0); // pass them to Supplier object

                if(supplier.checkIfSupplierBrandnameExists()){
                    alert.show("Warning", "Supplier with this brandname exists!", Alert.AlertType.WARNING);
                } else {

                    supplier.addSupplier();

                    Stage stage = (Stage) saveButton.getScene().getWindow(); // close this stage
                    stage.close();

                    // use RefreshData method of the SuppliersMainController class from this controller
                    // so it can load the edited row on tableview without pressing refresh button
                    SupplierMainController.getInstance().RefreshData();

                }


            }

        }

    }

    @FXML
    private void OpenPayingSelectionWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/PayingWaySelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        PayingWaySelectionController payingWaySelectionController = loader.getController();
        payingWaySelectionController.openedFrom("from new supplier");

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

        VATregimeSelectionController vaTregimeSelectionController = loader.getController();
        vaTregimeSelectionController.openedFrom("from new supplier");

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

        PriceListSelectionController priceListSelectionController = loader.getController();
        priceListSelectionController.openedFrom("from new supplier");

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

    public void displaySupplierID(String ID){

        id.setText(ID);

    }


}
