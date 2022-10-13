package controllers.handwritedInvoices;

import commons.CommonMethods;
import controllers.customerInvoices.CustomerInvoiceMainController;
import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.selectionWindows.CustomerSelectionController;
import controllers.selectionWindows.InvoiceTypeSelectionController;
import controllers.selectionWindows.PayingWaySelectionController;
import controllers.selectionWindows.VATregimeSelectionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.CustomerInvoice;
import objects.ItemTransaction;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class NewHandwritedInvoiceController {

    @FXML
    private TextField VAT;

    @FXML
    private TextField VATregime;

    @FXML
    private Button addItemButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<ItemTransaction> table;

    @FXML
    private TableColumn<ItemTransaction, Double> col_VAT;

    @FXML
    private TableColumn<ItemTransaction, String> col_code;

    @FXML
    private TableColumn<ItemTransaction, String> col_description;

    @FXML
    private TableColumn<ItemTransaction, Double> col_discount;

    @FXML
    private TableColumn<ItemTransaction, String> col_etiology;

    @FXML
    private TableColumn<ItemTransaction, Double> col_price;

    @FXML
    private TableColumn<ItemTransaction, Double> col_quantity;

    @FXML
    private TableColumn<ItemTransaction, Double> col_total;

    @FXML
    private TextField customer_brandname;

    @FXML
    private TextField customer_id;

    @FXML
    private Button deleteItemButton;

    @FXML
    private TextField discountvalue;

    @FXML
    private Button editItemButton;

    @FXML
    private TextField from;

    @FXML
    private Button handwriteButton;

    @FXML
    private TextField id;

    @FXML
    private TextField initialvalue;

    @FXML
    private Button instructionsButton;

    @FXML
    private TextField invoicenumber;

    @FXML
    private TextField invoicetype_description;

    @FXML
    private TextField invoicetype_id;

    @FXML
    private TextField licenseplate;

    @FXML
    private TextField payingway_description;

    @FXML
    private TextField payingway_id;

    @FXML
    private TextField purposeOfTracking;

    @FXML
    private TextField quantity;

    @FXML
    private TextField registrationUser;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchCustomerButton;

    @FXML
    private Button selectInvoiceTypeButton;

    @FXML
    private Button selectPayingwayButton;

    @FXML
    private Button SelectVatRegimeButton;

    @FXML
    private TextField to;

    @FXML
    private TextField total;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField timeTextfield;

    @FXML
    private TextField valueBeforeVAT;

    @FXML
    private TextField discountInvoice;

    @FXML
    private TextField remarks;

    @FXML
    private CheckBox handwritedCheckbox;

    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    CustomerInvoice customerInvoice = new CustomerInvoice();
    ObservableList<ItemTransaction> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    int index = -1; // create an integer so we can use it to handle clicked rows on table
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static NewHandwritedInvoiceController instance;

    public NewHandwritedInvoiceController(){
        instance = this;
    }

    public static NewHandwritedInvoiceController getInstance(){
        return instance;
    }

    @FXML
    public void initialize(){

        //load list to the table
        loadDataFromList();

    }

    @FXML
    private void ChangeToNormalInvoice(ActionEvent event) throws IOException {

        handwriteButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/NewCustomerInvoiceWindow.fxml"));
        Parent root = loader.load();

        //pass user to newCustomerInvoice Controller
        NewCustomerInvoiceController newCustomerInvoiceController = loader.getController();
        newCustomerInvoiceController.showProgramUser(registrationUser.getText());
        newCustomerInvoiceController.displayInvoiceID(commons.getNextID("customer_invoices", "id"));

        Stage newCustomerInvoiceStage = new Stage();
        Scene newCustomerInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newCustomerInvoiceStage.getIcons().add(image);
        newCustomerInvoiceStage.setX(203);
        newCustomerInvoiceStage.setY(106);
        newCustomerInvoiceStage.setScene(newCustomerInvoiceScene);
        newCustomerInvoiceStage.setTitle("Customer invoice (New invoice)");
        newCustomerInvoiceStage.setAlwaysOnTop(true);
        newCustomerInvoiceStage.setResizable(false);
        newCustomerInvoiceStage.show();

    }

    @FXML
    private void CloseCustomerInvoiceWindow(ActionEvent event) {

        Optional<ButtonType> result = alert.showConfirmation("Cancel confirmation", "This invoice will be lost, are you sure you want to continue?");

        if(result.get() == ButtonType.OK) {

            Stage customerStage = (Stage) cancelButton.getScene().getWindow();
            customerStage.close();

        }

    }

    @FXML
    private void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

    }

    @FXML
    public void OpenAddItemWindow(ActionEvent event) throws IOException {

        if(customer_id.getText().equals("") || dateField.getValue() == null){
            alert.show("Warning", "Add customer and date before adding items!", Alert.AlertType.WARNING);
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/handwritedInvoices/AddItemHandWindow.fxml"));
            Parent root = loader.load();

            AddItemHandController addItemHandController = loader.getController();
            addItemHandController.loadDataForTransaction(customer_id.getText(), id.getText(), dateField.getValue().format(formatter), timeTextfield.getText());

            Stage addItemStage = new Stage();
            Scene addItemScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            addItemStage.getIcons().add(image);
            addItemStage.setX(203);
            addItemStage.setY(106);
            addItemStage.setScene(addItemScene);
            addItemStage.setTitle("Items (Add item)");
            addItemStage.setAlwaysOnTop(true);
            addItemStage.initModality(Modality.APPLICATION_MODAL);
            addItemStage.setResizable(false);
            addItemStage.show();
        }

    }

    @FXML
    private void OpenEditItemWindow(ActionEvent event) throws IOException {

        if(index <= -1){
            alert.show("Warning", "Select an item!", Alert.AlertType.WARNING);
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/handwritedInvoices/EditItemHandWindow.fxml"));
            Parent root = loader.load();

            EditItemHandController editItemHandController = loader.getController();
            editItemHandController.getData(table.getSelectionModel().getSelectedItem());
            editItemHandController.loadDataForTransaction(customer_id.getText(), id.getText(), dateField.getValue().format(formatter), timeTextfield.getText());

            Stage editItemStage = new Stage();
            Scene editItemScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            editItemStage.getIcons().add(image);
            editItemStage.setX(203);
            editItemStage.setY(106);
            editItemStage.setScene(editItemScene);
            editItemStage.setTitle("Items (Edit item)");
            editItemStage.initModality(Modality.APPLICATION_MODAL);
            editItemStage.setAlwaysOnTop(true);
            editItemStage.setResizable(false);
            editItemStage.show();

        }

    }

    @FXML
    private void OpenInstructionsWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/NewCustomerInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage newCustomerInvoiceInstructionStage = new Stage();
        Scene newCustomerInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newCustomerInvoiceInstructionStage.getIcons().add(image);
        newCustomerInvoiceInstructionStage.setX(303);
        newCustomerInvoiceInstructionStage.setY(106);
        newCustomerInvoiceInstructionStage.setScene(newCustomerInvoiceInstructionScene);
        newCustomerInvoiceInstructionStage.setTitle("Instructions");
        newCustomerInvoiceInstructionStage.setAlwaysOnTop(true);
        newCustomerInvoiceInstructionStage.show();

    }

    @FXML
    private void SaveInvoice(ActionEvent event) {

        Integer invoiceID = Integer.valueOf(id.getText());
        String invoiceDate = null;
        if(dateField.getValue() == null){ // check in case user forgets to add date
            alert.show("Warning", "There is no date, insert the date!", Alert.AlertType.WARNING);
        } else{
            invoiceDate = dateField.getValue().format(formatter); // else add it to the invoiceDate variable
        }
        String invoiceTime = timeTextfield.getText();
        Integer invoiceCustomerID = null;
        if(customer_id.getText().equals("")){
            alert.show("Warning", "Customer is not selected!", Alert.AlertType.WARNING); //show alert if there is no customer id
        } else {
            invoiceCustomerID = Integer.valueOf(customer_id.getText());
        }
        Integer invoiceType = null;
        if(invoicetype_id.getText().equals("")){ // check in case user forgets to add invoice type
            alert.show("Warning", "Invoice Type is not selected!", Alert.AlertType.WARNING); //show alert if there is no invoice type id
        } else{
            invoiceType = Integer.valueOf(invoicetype_id.getText()); // else add it to the invoiceType variable
        }
        String invoiceTypeDescription = invoicetype_description.getText();
        String invoiceNumber = null;
        if(invoicenumber.getText().equals("")){ // check in case user forgets to add invoice number
            alert.show("Warning", "There is no invoice number, insert the number!", Alert.AlertType.WARNING);
        } else{
            invoiceNumber = invoicenumber.getText(); // else add it to the invoiceNumber variable
        }
        Integer invoicePayingWayID = null;
        if(payingway_id.getText().equals("")){
            alert.show("Warning", "There is no paying way!", Alert.AlertType.WARNING);
        } else{
            invoicePayingWayID = Integer.valueOf(payingway_id.getText());
        }

        String invoiceTrackingPurpose = purposeOfTracking.getText();
        String fromPlace = from.getText();
        String toPlace = to.getText();
        String invoiceLicensePLate = licenseplate.getText();
        String invoiceVATregime = VATregime.getText();
        String programUser = registrationUser.getText();
        double initialValue = Double.parseDouble(initialvalue.getText().replace(",", "."));
        double discountPercentage = Double.parseDouble(discountInvoice.getText());
        double discountValue = Double.parseDouble(discountvalue.getText().replace(",", "."));
        double valueNoVAT = Double.parseDouble(valueBeforeVAT.getText().replace(",", "."));
        double vatvalue = Double.parseDouble(VAT.getText().replace(",", "."));
        double totalQuantity = Double.parseDouble(quantity.getText());
        double invoiceTotal = Double.parseDouble(total.getText().replace(",", "."));
        Integer handwrited = 1; // set 1 because checkbox handwrited invoice is checked
        String remark = remarks.getText().trim();

        if(invoiceDate == null || invoiceCustomerID == null || invoiceType == null || invoiceNumber == null || invoicePayingWayID == null){ // check for null pointers, if there is no null then go for other checks
            alert.show("Information", "Fill in all missing fields!", Alert.AlertType.INFORMATION);
        } else {

            // create customer invoice object with the inserted data
            CustomerInvoice invoice = new CustomerInvoice(invoiceID, invoiceDate, invoiceTime, invoiceCustomerID, null, invoiceType, null, invoiceNumber, invoicePayingWayID, invoiceTrackingPurpose, fromPlace, toPlace, invoiceLicensePLate, invoiceVATregime, programUser, initialValue, discountPercentage, discountValue, valueNoVAT, vatvalue, totalQuantity, invoiceTotal, handwrited, remark);

            // Before checking the document, first we check if there are some items to the table then we go for other checks
            if (oblist.isEmpty()) {
                alert.show("Warning", "There are no transactions, add items to the table!", Alert.AlertType.WARNING);
            } else {

                //SOME CHECKS BEFORE THE CUSTOMER DOCUMENT IS INSERTED TO DATABASE
                //if checkBeforeInsertion method from CustomerInvoice class returns true that means everything is ok and its good to go and add that invoice
                if (invoice.checkBeforeInsertion(invoiceTypeDescription, invoiceType, invoiceLicensePLate, invoiceCustomerID)) {

                    //with a for loop add all the rows of the tableview to the database
                    for (ItemTransaction items : oblist) {
                        items.makeTransaction();
                        items.updateItemRemaining("-"); // update each item remaining by subtracting their current remaining value
                    }

                    invoice.addCustomerInvoice(); // add that invoice

                    Stage customerStage = (Stage) saveButton.getScene().getWindow();
                    customerStage.close();

                    CustomerInvoiceMainController.getInstance().RefreshData();

                }

            }
        }

    }

    @FXML
    private void SearchCustomer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/CustomerSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        CustomerSelectionController customerSelectionController = loader.getController();
        customerSelectionController.openedFrom("from new handwrited invoice");
        customerSelectionController.loadData(customer_brandname.getText().trim()); //load data of the inputed brandname

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(270);
        measurementStage.setY(110);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    private void SelectInvoiceType(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/InvoiceTypeSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        InvoiceTypeSelectionController invoiceTypeSelectionController = loader.getController();
        invoiceTypeSelectionController.openedFrom("from new handwrited invoice");

        Stage invoiceTypeStage = new Stage();
        Scene invoiceTypeScene = new Scene(root);
        invoiceTypeStage.setX(260);
        invoiceTypeStage.setY(70);
        invoiceTypeStage.setScene(invoiceTypeScene);
        invoiceTypeStage.initStyle(StageStyle.UNDECORATED);
        invoiceTypeStage.initModality(Modality.APPLICATION_MODAL);
        invoiceTypeStage.setAlwaysOnTop(true);
        invoiceTypeStage.setResizable(false);
        invoiceTypeStage.show();

    }

    @FXML
    private void SelectPayingWay(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/PayingWaySelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        PayingWaySelectionController payingWaySelectionController = loader.getController();
        payingWaySelectionController.openedFrom("from new handwrited invoice");

        Stage payingwayStage = new Stage();
        Scene payingwayScene = new Scene(root);
        payingwayStage.setX(545);
        payingwayStage.setY(70);
        payingwayStage.setScene(payingwayScene);
        payingwayStage.initStyle(StageStyle.UNDECORATED);
        payingwayStage.initModality(Modality.APPLICATION_MODAL);
        payingwayStage.setAlwaysOnTop(true);
        payingwayStage.setResizable(false);
        payingwayStage.show();

    }

    @FXML
    private void SelectVatRegime(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/VATregimeSelectionWindow.fxml"));
        Parent root = loader.load();

        VATregimeSelectionController vaTregimeSelectionController = loader.getController();
        vaTregimeSelectionController.openedFrom("from new handwrited invoice");

        Stage vatregimeStage = new Stage();
        Scene vatregimeScene = new Scene(root);
        vatregimeStage.setX(545);
        vatregimeStage.setY(140);
        vatregimeStage.setScene(vatregimeScene);
        vatregimeStage.initStyle(StageStyle.UNDECORATED);
        vatregimeStage.initModality(Modality.APPLICATION_MODAL);
        vatregimeStage.setAlwaysOnTop(true);
        vatregimeStage.setResizable(false);
        vatregimeStage.show();

    }

    @FXML
    private void SelectAllText(MouseEvent event){

        discountInvoice.selectAll();

    }

    @FXML
    private void QalqulateInvoiceDiscount(KeyEvent event) {

        DecimalFormat df = new DecimalFormat("0.00");
        double Discount;
        double DiscountValue;
        double newVatValue;
        double newTotalWithVAT;
        double newValueBeforeVAT;

        if (discountInvoice.getText().equals("")) {

            double initialValue;
            double totalValue;
            double oldVatValue;
            Discount = 0.0;
            DiscountValue = 0.0;
            discountInvoice.setText(String.valueOf(Discount));
            discountvalue.setText(String.valueOf(DiscountValue));

            if(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("wholesale_price")){

                initialValue = customerInvoice.sumOfInitialValueWholesale(oblist);
                totalValue = customerInvoice.sumTotalForWholesale(oblist);
                oldVatValue = totalValue - initialValue;

                VAT.setText(df.format(oldVatValue));
                total.setText(df.format(totalValue));
                valueBeforeVAT.setText(df.format(initialValue));

            } else if(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("retail_price")){

                initialValue = customerInvoice.sumOfInitialValueRetail(oblist);
                totalValue = customerInvoice.sumTotalForRetail(oblist);
                oldVatValue = totalValue - initialValue;

                VAT.setText(df.format(oldVatValue));
                total.setText(df.format(totalValue));
                valueBeforeVAT.setText(df.format(initialValue));

            }

        } else {
            Discount = Double.parseDouble(discountInvoice.getText());
            double initialValue;
            double totalValue;
            double oldVatValue;

            if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE){

                if(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("wholesale_price")){

                    initialValue = customerInvoice.sumOfInitialValueWholesale(oblist);
                    totalValue = customerInvoice.sumTotalForWholesale(oblist);
                    oldVatValue = totalValue - initialValue;

                    VAT.setText(df.format(oldVatValue));
                    total.setText(df.format(totalValue));
                    valueBeforeVAT.setText(df.format(initialValue));

                } else if(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("retail_price")){

                    initialValue = customerInvoice.sumOfInitialValueRetail(oblist);
                    totalValue = customerInvoice.sumTotalForRetail(oblist);
                    oldVatValue = totalValue - initialValue;

                    VAT.setText(df.format(oldVatValue));
                    total.setText(df.format(totalValue));
                    valueBeforeVAT.setText(df.format(initialValue));

                }

            }

            if(event.getCode() == KeyCode.ENTER) {

                double InitialValue = Double.parseDouble(initialvalue.getText().replace(",", "."));
                double vat = Double.parseDouble(VAT.getText().replace(",", "."));

                double s = 100 - Discount;

                newValueBeforeVAT = (s * InitialValue) / 100;
                valueBeforeVAT.setText(df.format(newValueBeforeVAT));

                newVatValue = (s * vat) / 100;
                VAT.setText(df.format(newVatValue));

                newTotalWithVAT = newValueBeforeVAT + newVatValue;
                total.setText(df.format(newTotalWithVAT));

                DiscountValue = InitialValue - newValueBeforeVAT;
                discountvalue.setText(df.format(DiscountValue));

            }

        }

    }

    public void showProgramUser(String userName){ //display who is the user

        registrationUser.setText(userName);

    }

    public void getInvoiceTypeData(String invoiceType_id, String invoiceType, String deliveryCheck){

        invoicetype_id.setText(invoiceType_id);
        invoicetype_description.setText(invoiceType);

        //check invoice type deliveryCheck and add data to invoice elements textfields
        if(deliveryCheck.equals("1")){ // if its 1 that means that this type of invoice has delivery

            purposeOfTracking.setText("Sales");
            from.setText("Our headquarters");
            to.setText("Your headquarters");

        } else { // else empty textFields

            purposeOfTracking.setText("");
            from.setText("");
            to.setText("");

        }

    }

    public void getPayingWayData(String payingwayID, String payingway){

        payingway_id.setText(payingwayID);
        payingway_description.setText(payingway);

    }

    public void getVatRegimeData(String vatregime){

        VATregime.setText(vatregime);

        if(vatregime.equals("Zero VAT regime")){

            index = 0;
            for (ItemTransaction items : oblist) {

                items.setVat(0.0);
                oblist.set(index, items);
                index++;

            }

            setCalculationResultsToTextFields();

        } else if(vatregime.equals("Reduced VAT (Border)")){

            index = 0;
            for (ItemTransaction items : oblist) {

                double oldVat = commons.getItemVAT(items.getItem_code());
                double s = 70;
                double newVat = (s * oldVat) / 100;

                items.setVat(Double.valueOf(Math.round(newVat)));

                oblist.set(index, items);
                index++;

            }

            setCalculationResultsToTextFields();

        } else if(vatregime.equals("Normal VAT regime")){

            index = 0;
            for (ItemTransaction items : oblist) {

                double defaultVat = commons.getItemVAT(items.getItem_code());

                items.setVat(defaultVat);

                oblist.set(index, items);
                index++;

            }

            setCalculationResultsToTextFields();

        }

    }

    public void displayInvoiceID(String ID){

        id.setText(ID);

    }

    // by selecting a customer
    // we also need to take vat_regime id and paying way id
    // and display their description in the textfields
    // as we defined that this customer will have that spesific payingway id
    // and that spesific vat regime id
    public void getCustomerData(String customerID, String customerBrandname, String vatregimeID, String payingWayID){

        customer_id.setText(customerID);
        customer_brandname.setText(customerBrandname);
        payingway_id.setText(payingWayID);
        payingway_description.setText(commons.getSelectionDescription("paying_way", payingWayID));
        VATregime.setText(commons.getSelectionDescription("vat_regime", vatregimeID));

    }

    @FXML
    private void DeleteItem(ActionEvent event) {

        if(index <= -1){ // check if a row is selected
            alert.show("Warning", "Select an item to delete!", Alert.AlertType.WARNING);
        } else {
            oblist.remove(index); // remove from the list the item with the index equal to the selected row
            setCalculationResultsToTextFields();
            index = -1; //inizialize index to -1 as after the deletion there is no row selected
        }

    }

    public void addItemToList(ItemTransaction item){

        oblist.add(item);
        double quantity = 0.0;

        for (ItemTransaction items : oblist) { // we for loop the table with the inputed items

            if(items.getItem_code().equals(item.getItem_code())) { //check items that has the same code as the new inserted item

                quantity += items.getQuantity(); //get their quantities

            }

        }

        //then check if user has passed the remaining of the items
        item.checkItemRemaining(item.getItem_code(), quantity);

        setCalculationResultsToTextFields();

    }

    public void updateItemToList(ItemTransaction item){

        oblist.set(index, item);
        setCalculationResultsToTextFields();
        index = -1;

    }

    public void loadDataFromList(){

        col_code.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("item_description"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        col_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        col_VAT.setCellValueFactory(new PropertyValueFactory<>("vat"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_etiology.setCellValueFactory(new PropertyValueFactory<>("etiology"));

        table.setItems(oblist);

    }

    public void setCalculationResultsToTextFields(){

        DecimalFormat df = new DecimalFormat("0.00");
        double sumQuantity = customerInvoice.sumOfQuantities(oblist);

        if(commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("wholesale_price")){ //we check if the selected customer has wholesale pricelist

            double initialValue = customerInvoice.sumOfInitialValueWholesale(oblist);
            double totalValue = customerInvoice.sumTotalForWholesale(oblist);
            double vatValue = totalValue - initialValue;

            initialvalue.setText(String.valueOf(initialValue));

            valueBeforeVAT.setText(initialvalue.getText()); //this value is the same with initial thats why we just get the text from initial value textfield

            VAT.setText(df.format(vatValue));

            quantity.setText(String.valueOf(sumQuantity));

            total.setText(df.format(totalValue));
        }

        else if (commons.getPriceColumnBasedOnCustomerOrSupplierPriceList("customer", "customer_id", customer_id.getText()).equals("retail_price")){//we check if the selected customer has retail pricelist

            double initialValue = customerInvoice.sumOfInitialValueRetail(oblist);
            double totalValue = customerInvoice.sumTotalForRetail(oblist);
            double vatValue = totalValue - initialValue;

            initialvalue.setText(df.format(initialValue));

            valueBeforeVAT.setText(initialvalue.getText()); //this value is the same with initial thats why we just get the text from initial value textfield

            VAT.setText(df.format(vatValue));

            quantity.setText(String.valueOf(sumQuantity));

            total.setText(df.format(totalValue));
        }

    }

}
