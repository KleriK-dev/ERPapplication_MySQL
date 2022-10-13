package controllers.supplierInvoices;

import commons.CommonMethods;
import controllers.selectionWindows.*;
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
import objects.Item;
import objects.ItemTransaction;
import objects.SupplierInvoice;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class NewSupplierInvoiceController {

    @FXML
    private Button SelectVatRegimeButton;

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
    private DatePicker dateField;

    @FXML
    private Button deleteItemButton;

    @FXML
    private TextField discountInvoice;

    @FXML
    private TextField discountvalue;

    @FXML
    private Button editItemButton;

    @FXML
    private TextField from;

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
    private TextField remarks;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchSupplierButton;

    @FXML
    private Button selectInvoiceTypeButton;

    @FXML
    private Button selectPayingwayButton;

    @FXML
    private TextField supplier_brandname;

    @FXML
    private TextField supplier_id;

    @FXML
    private TextField timeTextfield;

    @FXML
    private TextField to;

    @FXML
    private TextField total;

    @FXML
    private TextField valueBeforeVAT;

    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    SupplierInvoice supplierInvoice = new SupplierInvoice();
    ObservableList<ItemTransaction> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    int index = -1; // create an integer so we can use it to handle clicked rows on table
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static NewSupplierInvoiceController instance;

    public NewSupplierInvoiceController(){
        instance = this;
    }

    public static NewSupplierInvoiceController getInstance(){
        return instance;
    }

    @FXML
    public void initialize(){

        //load list to the table
        loadDataFromList();

    }

    @FXML
    void CloseSupplierInvoiceWindow(ActionEvent event) {

        Optional<ButtonType> result = alert.showConfirmation("Cancel confirmation", "This invoice will be lost, are you sure you want to continue?");

        if(result.get() == ButtonType.OK) {

            Stage NewSupplierInvoiceStage = (Stage) cancelButton.getScene().getWindow();
            NewSupplierInvoiceStage.close();

        }

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

    }

    @FXML
    void OpenAddItemWindow(ActionEvent event) throws IOException {

        if(supplier_id.getText().equals("") || dateField.getValue() == null){
            alert.show("Warning", "Add supplier and date before adding items!", Alert.AlertType.WARNING);
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/supplierInvoices/AddSuppItemWindow.fxml"));
            Parent root = loader.load();

            AddSuppItemController addSuppItemController = loader.getController();
            addSuppItemController.loadDataForTransaction(supplier_id.getText(), id.getText(), dateField.getValue().format(formatter), timeTextfield.getText()); //PREPEI NA DW TO DATE FIELD

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
    void OpenEditItemWindow(ActionEvent event) throws IOException {

        if(index <= -1){
            alert.show("Warning", "Select an item!", Alert.AlertType.WARNING);
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/supplierInvoices/EditSuppItemWindow.fxml"));
            Parent root = loader.load();

            EditSuppItemController editSuppItemController = loader.getController();
            editSuppItemController.getData(table.getSelectionModel().getSelectedItem());
            editSuppItemController.loadDataForTransaction(supplier_id.getText(), id.getText(), dateField.getValue().format(formatter), timeTextfield.getText());

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
        loader.setLocation(getClass().getResource("/fxml/instructions/NewSupplierInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage newSupplierInvoiceInstructionStage = new Stage();
        Scene newSupplierInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newSupplierInvoiceInstructionStage.getIcons().add(image);
        newSupplierInvoiceInstructionStage.setX(303);
        newSupplierInvoiceInstructionStage.setY(106);
        newSupplierInvoiceInstructionStage.setScene(newSupplierInvoiceInstructionScene);
        newSupplierInvoiceInstructionStage.setTitle("Instructions");
        newSupplierInvoiceInstructionStage.setAlwaysOnTop(true);
        newSupplierInvoiceInstructionStage.show();

    }

    @FXML
    void QalculateInvoiceDiscount(KeyEvent event) {

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

            initialValue = supplierInvoice.sumOfInitialValueWholesale(oblist);
            totalValue = supplierInvoice.sumTotalForWholesale(oblist);
            oldVatValue = totalValue - initialValue;

            VAT.setText(df.format(oldVatValue));
            total.setText(df.format(totalValue));
            valueBeforeVAT.setText(df.format(initialValue));


        } else {

            Discount = Double.parseDouble(discountInvoice.getText());
            double initialValue;
            double totalValue;
            double oldVatValue;

            if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE){

                initialValue = supplierInvoice.sumOfInitialValueWholesale(oblist);
                totalValue = supplierInvoice.sumTotalForWholesale(oblist);
                oldVatValue = totalValue - initialValue;

                VAT.setText(df.format(oldVatValue));
                total.setText(df.format(totalValue));
                valueBeforeVAT.setText(df.format(initialValue));


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

    @FXML
    void SaveInvoice(ActionEvent event) {

        Integer invoiceID = Integer.valueOf(id.getText());
        String invoiceDate = null;
        if(dateField.getValue() == null){ // check in case user forgets to add date
            alert.show("Warning", "There is no date, insert the date!", Alert.AlertType.WARNING);
        } else{
            invoiceDate = dateField.getValue().format(formatter); // else add it to the invoiceDate variable
        }
        String invoiceTime = timeTextfield.getText();
        Integer supplierID = null;
        if(supplier_id.getText().equals("")){
            alert.show("Warning", "Supplier is not selected!", Alert.AlertType.WARNING); //show alert if there is no supplier id
        } else {
            supplierID = Integer.valueOf(supplier_id.getText());
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
        String remark = remarks.getText().trim();

        if(invoiceDate == null || supplierID == null || invoiceType == null || invoiceNumber == null || invoicePayingWayID == null){ // check for null pointers, if there is no null then go for other checks
            alert.show("Information", "Fill in all missing fields!", Alert.AlertType.INFORMATION);
        } else {

            // create supplier invoice object with the inserted data
            SupplierInvoice invoice = new SupplierInvoice(invoiceID, invoiceDate, invoiceTime, supplierID, null, invoiceType, null, invoiceNumber, invoicePayingWayID, invoiceTrackingPurpose, fromPlace, toPlace, invoiceLicensePLate, invoiceVATregime, programUser, initialValue, discountPercentage, discountValue, valueNoVAT, vatvalue, totalQuantity, invoiceTotal, remark);

            // Before checking the document, first we check if there are some items to the table then we go for other checks
            if (oblist.isEmpty()) {
                alert.show("Warning", "There are no transactions, add items to the table!", Alert.AlertType.WARNING);
            } else {

                if(invoice.checkIfSelectedSupplierHasTaxCode(supplierID)){

                    Item item = new Item();

                    //with a for loop add all the rows of the tableview to the database
                    for (ItemTransaction transaction : oblist) {

                        item.updateItemWithSupplierAndPurchasePrice(supplierID, transaction.getUnit_price(), transaction.getItem_code());

                        transaction.makeTransaction();
                        transaction.updateItemRemaining("+"); // update each item remaining by adding their current remaining value because we supply those items

                    }

                    invoice.addSupplierInvoice(); // add that invoice

                    Stage supplierStage = (Stage) saveButton.getScene().getWindow();
                    supplierStage.close();

                    SupplierInvoiceMainController.getInstance().RefreshData();

                } else {
                    alert.show("Warning", "Supplier has no tax code!", Alert.AlertType.WARNING);
                }

            }
        }

    }

    @FXML
    void SearchSupplier(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/SupplierSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        SupplierSelectionController supplierSelectionController = loader.getController();
        supplierSelectionController.openedFrom("from new supplier invoice");
        supplierSelectionController.loadData(supplier_brandname.getText().trim()); //load data of the inputed brandname

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
    void SelectAllText(MouseEvent event) {

        discountInvoice.selectAll();

    }

    @FXML
    void SelectInvoiceType(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/SupplierInvoiceTypeSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        SupplierInvoiceTypeSelectionController supplierInvoiceTypeSelectionController = loader.getController();
        supplierInvoiceTypeSelectionController.openedFrom("from new supplier invoice");

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
    void SelectPayingWay(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/PayingWaySelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        PayingWaySelectionController payingWaySelectionController = loader.getController();
        payingWaySelectionController.openedFrom("from new supplier invoice");

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
    void SelectVatRegime(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/VATregimeSelectionWindow.fxml"));
        Parent root = loader.load();

        VATregimeSelectionController vaTregimeSelectionController = loader.getController();
        vaTregimeSelectionController.openedFrom("from new supplier invoice");

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

    public void displayInvoiceID(String ID){

        id.setText(ID);

    }

    // by selecting a supplier
    // we also need to take vat_regime id and paying way id
    // and display their description in the textfields
    // as we defined that this supplier will have that spesific payingway id
    // and that spesific vat regime id
    public void getSupplierData(String supplierID, String supplierBrandname, String vatregimeID, String payingWayID){

        supplier_id.setText(supplierID);
        supplier_brandname.setText(supplierBrandname);
        payingway_id.setText(payingWayID);
        payingway_description.setText(commons.getSelectionDescription("paying_way", payingWayID));
        VATregime.setText(commons.getSelectionDescription("vat_regime", vatregimeID));

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

    public void showProgramUser(String userName){ //display who is the user

        registrationUser.setText(userName);

    }

    //Add, Update and Delete methods for items on tableview
    public void addItemToList(ItemTransaction item){

        oblist.add(item);

        setCalculationResultsToTextFields();

    }

    public void updateItemToList(ItemTransaction item){

        oblist.set(index, item);
        setCalculationResultsToTextFields();
        index = -1;

    }

    @FXML
    void DeleteItem(ActionEvent event) {

        if(index <= -1){ // check if a row is selected
            alert.show("Warning", "Select an item to delete!", Alert.AlertType.WARNING);
        } else {
            oblist.remove(index); // remove from the list the item with the index equal to the selected row
            setCalculationResultsToTextFields();
            index = -1; //inizialize index to -1 as after the deletion there is no row selected
        }

    }

    public void setCalculationResultsToTextFields(){

        DecimalFormat df = new DecimalFormat("0.00");
        double sumQuantity = supplierInvoice.sumOfQuantities(oblist);

        double initialValue = supplierInvoice.sumOfInitialValueWholesale(oblist);
        double totalValue = supplierInvoice.sumTotalForWholesale(oblist);
        double vatValue = totalValue - initialValue;

        initialvalue.setText(String.valueOf(initialValue));

        valueBeforeVAT.setText(initialvalue.getText()); //this value is the same with initial thats why we just get the text from initial value textfield

        VAT.setText(df.format(vatValue));

        quantity.setText(String.valueOf(sumQuantity));

        total.setText(df.format(totalValue));

    }

    public void getInvoiceTypeData(String invoiceType_id, String invoiceType){

        invoicetype_id.setText(invoiceType_id);
        invoicetype_description.setText(invoiceType);

        if(invoiceType_id.equals("1")){

            purposeOfTracking.setText("Purchase");
            from.setText("Our headquarters");
            to.setText("Your headquarters");

        } else {

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
    
}
