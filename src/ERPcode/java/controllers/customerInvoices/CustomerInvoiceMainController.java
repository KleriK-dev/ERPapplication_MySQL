package controllers.customerInvoices;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import objects.CustomerInvoice;
import objects.ItemTransaction;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CustomerInvoiceMainController {

    @FXML
    private Button addButton;

    @FXML
    private Button cancelInvoiceButton;

    @FXML
    private MenuItem cancelInvoiceMenu;

    @FXML
    private Button instructionButton;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<CustomerInvoice> table;

    @FXML
    private TableColumn<CustomerInvoice, String> col_customer;

    @FXML
    private TableColumn<CustomerInvoice, Integer> col_id;

    @FXML
    private TableColumn<CustomerInvoice, String> col_invoicetype;

    @FXML
    private TableColumn<CustomerInvoice, String> col_invoiceNumber;

    @FXML
    private TableColumn<CustomerInvoice, String> col_time;

    @FXML
    private TableColumn<CustomerInvoice, String> col_date;

    @FXML
    private TableColumn<CustomerInvoice, String> col_total;

    @FXML
    private TableColumn<CustomerInvoice, String> col_handwrited;

    @FXML
    private Label customer_label;

    @FXML
    private Label date_label;

    @FXML
    private Label handwrited_label;

    @FXML
    private Button deleteButton;

    @FXML
    private MenuItem deleteInvoiceMenu;

    @FXML
    private MenuItem editInvoiceMenu;

    @FXML
    private Label id_label;

    @FXML
    private Label invoicenum_label;

    @FXML
    private Label invoicetype_label;

    @FXML
    private MenuItem newInvoiceMenu;

    @FXML
    private Label payingmethod_label;

    @FXML
    private Label remark_label;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField registrations;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchbar;

    @FXML
    private TextField selectedID;

    @FXML
    private MenuItem showInvoiceMenu;

    @FXML
    private Label time_label;

    @FXML
    private Label total_label;

    @FXML
    private Label user_label;

    @FXML
    private Button viewButton;

    @FXML
    private TextField ProgramUserField;

    ObservableList<CustomerInvoice> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    ObservableList<ItemTransaction> itemTransactionList = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    /* create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static CustomerInvoiceMainController instance;

    public CustomerInvoiceMainController(){
        instance = this;
    }

    public static CustomerInvoiceMainController getInstance(){
        return instance;
    }

    @FXML
    public void initialize() {

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    private void CloseCustomerInvoiceWindow(ActionEvent event) {

        Stage customerInvoiceStage = (Stage) closeButton.getScene().getWindow();
        customerInvoiceStage.close();

    }

    @FXML
    private void OpenInstructions(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/CustomerInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage customerInvoiceInstructionStage = new Stage();
        Scene customerInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        customerInvoiceInstructionStage.getIcons().add(image);
        customerInvoiceInstructionStage.setX(303);
        customerInvoiceInstructionStage.setY(106);
        customerInvoiceInstructionStage.setScene(customerInvoiceInstructionScene);
        customerInvoiceInstructionStage.setTitle("Instructions");
        customerInvoiceInstructionStage.setAlwaysOnTop(true);
        customerInvoiceInstructionStage.show();

    }

    @FXML
    private void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displayCustomerInvoiceInfo(); // add customers info to the textfields
            selectTransactionsOfHandwrittenInvoice(selectedID.getText());
        }

        //on double click to a row, open the view invoice window
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    try {
                        openViewInvoiceWindow();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

    }

    @FXML
    void HandleSelectedRowByKey(KeyEvent event) throws IOException {

        if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN){

            index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

            if (index <= -1) { // check if index is -1 or smaller then do not act
                return;
            } else {
                selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
                displayCustomerInvoiceInfo(); // add customers info to the textfields
                selectTransactionsOfHandwrittenInvoice(selectedID.getText());
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
            } else {
                openViewInvoiceWindow();
            }

        }

    }


    @FXML
    private void OpenInvoiceCancellationWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")){ //check if invoice is selected
            alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
        } else if (handwrited_label.getText().equals("0")) { //check if this invoice isn't handwritten

            if(remark_label.getText().equals("CANCELED")){
                alert.show("Warning", "This invoice is already canceled!", Alert.AlertType.WARNING);
            } else {

                String invoiceType = invoicetype_label.getText();

                if(invoiceType.equals("SPECIAL CANCELLATION")){ //lastly check if this is not a special cancelled invoice
                    alert.show("Warning", "This is a Special Cancellation invoice.\nIt can not be cancelled again!", Alert.AlertType.WARNING);
                } else {

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String currentDate = LocalDateTime.now().format(dateFormatter);

                    if(date_label.getText().equals(currentDate)){ //if invoice date and current date are equal than open special cancellation

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/customerInvoices/SpecialCancellationWindow.fxml"));
                        Parent root = loader.load();

                        SpecialCancellationController specialCancellationController = loader.getController();
                        specialCancellationController.getData(table.getSelectionModel().getSelectedItem());
                        specialCancellationController.loadTableWithData(selectedID.getText());
                        specialCancellationController.displayInvoiceID(commons.getNextID("customer_invoices", "id"));

                        Stage specialCancellationStage = new Stage();
                        Scene specialCancellationScene = new Scene(root);
                        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
                        specialCancellationStage.getIcons().add(image);
                        specialCancellationStage.setX(203);
                        specialCancellationStage.setY(106);
                        specialCancellationStage.setScene(specialCancellationScene);
                        specialCancellationStage.setTitle("Customer invoice (Special Cancellation)");
                        specialCancellationStage.setAlwaysOnTop(true);
                        specialCancellationStage.setResizable(false);
                        specialCancellationStage.show();

                    } else {

                        Optional<ButtonType> result = alert.showConfirmation("Confirmation", "This invoice has different date than today.\nDo you want to cancel it?");

                        if(result.get() == ButtonType.OK) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/customerInvoices/SpecialCancellationWindow.fxml"));
                            Parent root = loader.load();

                            SpecialCancellationController specialCancellationController = loader.getController();
                            specialCancellationController.getData(table.getSelectionModel().getSelectedItem());
                            specialCancellationController.loadTableWithData(selectedID.getText());
                            specialCancellationController.displayInvoiceID(commons.getNextID("customer_invoices", "id"));

                            Stage specialCancellationStage = new Stage();
                            Scene specialCancellationScene = new Scene(root);
                            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
                            specialCancellationStage.getIcons().add(image);
                            specialCancellationStage.setX(203);
                            specialCancellationStage.setY(106);
                            specialCancellationStage.setScene(specialCancellationScene);
                            specialCancellationStage.setTitle("Customer invoice (Special Cancellation)");
                            specialCancellationStage.setAlwaysOnTop(true);
                            specialCancellationStage.setResizable(false);
                            specialCancellationStage.show();

                        }

                    }

            }

            }

        } else {
            alert.show("Warning", "This is a handwritten document. It can not be cancelled!", Alert.AlertType.WARNING);
        }

    }

    @FXML
    private void OpenNewInvoiceCustomerWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/NewCustomerInvoiceWindow.fxml"));
        Parent root = loader.load();

        //pass user to newCustomerInvoice Controller
        NewCustomerInvoiceController newCustomerInvoiceController = loader.getController();
        newCustomerInvoiceController.showProgramUser(ProgramUserField.getText());
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

        // handle when NewCustomerInvoice window is closed by X button
        newCustomerInvoiceStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                we.consume(); // consume the event so that the window does not close when cancel button is pressed to the confirmation window

                Optional<ButtonType> result = alert.showConfirmation("Cancel confirmation", "This invoice will be lost with all the items, are you sure you want to continue?");

                if(result.get() == ButtonType.OK) { // if its pressed OK

                    newCustomerInvoiceStage.close();

                }
            }
        });

    }

    @FXML
    private void OpenViewInvoiceWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")){
            alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
        } else {
            openViewInvoiceWindow();
        }

    }

    @FXML
    void DeleteCustomerInvoice(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
        } else {

            int invoice_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            CustomerInvoice invoice = new CustomerInvoice(invoice_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, null, null); // use customer contructor to pass the variable

            if(handwrited_label.getText().equals("1")){

                Optional<ButtonType> result = alert.showConfirmation("Delete Confirmation", "This invoice will be deleted permanently, do you want to continue?");

                if(result.get() == ButtonType.OK) {
                    invoice.deleteCustomerInvoice(); // use deleteCustomerInvoice method of the object CustomerInvoice

                    for (ItemTransaction items : itemTransactionList) { // loop the list that was created with the transactions of the invoice

                        items.updateItemRemaining("+"); // and update their remainings by adding them back

                    }

                    ItemTransaction transaction = new ItemTransaction();
                    transaction.setInvoiceID(invoice_id);
                    transaction.setTransaction_code(300);
                    transaction.deleteTransactions();
                }
            } else {
                alert.show("Warning", "Computerized invoices can not be deleted!\nUse special cancellation for this invoice!", Alert.AlertType.WARNING);
            }

            loadData(); // load new rows
            countRows(); // count new rows
            selectedID.clear(); // empty the selectedid textfield
            clearInfoTable(); // empty textfields from the info table as this customer does not exist anymore

        }

    }

    @FXML
    private void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    void SearchCustomerInvoice(ActionEvent event) {
        searchEngine();
    }

    @FXML
    void SearchCustomerInvoiceFromTextfield(MouseEvent event) {
        searchEngine();
    }

    public void showProgramUser(String userName){ //display the user

        ProgramUserField.setText(userName);

    }

    private void loadData(){

        /* Clear the observablelist and then run the query
         * as if user presses refresh button there will not be
         * duplicated data on the tableview */

        oblist.clear();

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, brandname AS customer, description AS invoiceType FROM customer_invoices " +
                "LEFT JOIN customer ON customer.customer_id = customer_invoices.customer_id " +
                "LEFT JOIN type_of_customer_document ON type_of_customer_document.id = customer_invoices.invoicetype_id " +
                "ORDER BY customer_invoices.id DESC";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new CustomerInvoice(rs.getInt("id"), rs.getString("date"),
                        rs.getString("time"), rs.getInt("customer_id"), rs.getString("customer"), rs.getInt("invoicetype_id"),
                        rs.getString("invoiceType"), rs.getString("invoice_number"), rs.getInt("payingway_id"), rs.getString("purpose_of_tracking"),
                        rs.getString("from_place"), rs.getString("to_place"), rs.getString("license_plate"),
                        rs.getString("vatregime_id"), rs.getString("user_id"), rs.getDouble("initial_value"), rs.getDouble("discount_percent"),
                        rs.getDouble("discount_value"),rs.getDouble("value_beforevat"),rs.getDouble("vat_value"),
                        rs.getDouble("quantity"),rs.getDouble("total"), rs.getInt("handwrited_invoice"), rs.getString("remarks")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("customerInvoiceID"));
            col_date.setCellValueFactory(new PropertyValueFactory<>("insertionDate"));
            col_time.setCellValueFactory(new PropertyValueFactory<>("insertionTime"));
            col_customer.setCellValueFactory(new PropertyValueFactory<>("customerBrandname"));
            col_invoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
            col_invoicetype.setCellValueFactory(new PropertyValueFactory<>("invoiceTypeDescription"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            col_handwrited.setCellValueFactory(new PropertyValueFactory<>("handwrited"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    private void displayCustomerInvoiceInfo(){

        index = table.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, customer.brandname AS customer, " +
                "type_of_customer_document.description AS invoiceType, " +
                "paying_way.description AS payingMethod, " +
                "users.username AS regUser " +
                "FROM customer_invoices " +
                "INNER JOIN type_of_customer_document ON type_of_customer_document.id = customer_invoices.invoicetype_id " +
                "INNER JOIN customer ON customer.customer_id = customer_invoices.customer_id " +
                "INNER JOIN paying_way ON paying_way.id = customer_invoices.payingway_id " +
                "INNER JOIN users ON users.id = customer_invoices.user_id " +
                "WHERE customer_invoices.id = ? ";
        PreparedStatement prst;
        ResultSet rs;
        Integer customerInvoiceID = col_id.getCellData(index);

        String date = null;
        String time = null;
        String customer = null;
        String invoiceType = null;
        String invoiceNumber = null;
        String payingMethod = null;
        String remarks = null;
        String user = null;
        String total = null;
        String handwrited = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, customerInvoiceID);
            rs = prst.executeQuery();
            while (rs.next()) {
                date = rs.getString("date");
                time = rs.getString("time");
                customer = rs.getString("customer");
                invoiceType = rs.getString("invoiceType");
                invoiceNumber = rs.getString("invoice_number");
                payingMethod = rs.getString("payingMethod");
                remarks = rs.getString("remarks");
                user = rs.getString("regUser");
                total = rs.getString("total");
                handwrited = rs.getString("handwrited_invoice");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

        id_label.setText(String.valueOf(customerInvoiceID));
        date_label.setText(date);
        time_label.setText(time);
        customer_label.setText(customer);
        invoicetype_label.setText(invoiceType);
        invoicenum_label.setText(invoiceNumber);
        payingmethod_label.setText(payingMethod);
        remark_label.setText(remarks);
        user_label.setText(user);
        total_label.setText(total);
        handwrited_label.setText(handwrited);

    }

    private void clearInfoTable(){

        id_label.setText("");
        date_label.setText("");
        time_label.setText("");
        customer_label.setText("");
        invoicetype_label.setText("");
        invoicenum_label.setText("");
        payingmethod_label.setText("");
        remark_label.setText("");
        user_label.setText("");
        total_label.setText("");
        handwrited_label.setText("");
        selectedID.setText("");

    }

    public void RefreshData() { //create this public method so it can be used by AddCustomerController
        loadData();
        countRows();
        clearInfoTable();
    }

    public void searchEngine(){

        //Initial filtered list
        FilteredList<CustomerInvoice> filterData = new FilteredList<>(oblist, b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(CustomerInvoice -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (CustomerInvoice.getInsertionDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on date
                } else if (CustomerInvoice.getCustomerBrandname().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on brandname
                } else if (CustomerInvoice.getInvoiceNumber().replaceAll("0", "").indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on invoice number
                } else if (CustomerInvoice.getInvoiceTypeDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on invoiceType
                } else if (String.valueOf(CustomerInvoice.getCustomerInvoiceID()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on customer invoice id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<CustomerInvoice> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table.setItems(sortData);

    }

    private void countRows(){

        registrations.setText(commons.CountRows("customer_invoices"));

    }

    //create a method that opens view invoice window, so we can call it everu time we want
    public void openViewInvoiceWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/ViewCustomerInvoiceWindow.fxml"));
        Parent root = loader.load();

        ViewCustomerInvoiceController viewCustomerInvoiceController = loader.getController();
        viewCustomerInvoiceController.getData(table.getSelectionModel().getSelectedItem());

        int transactionCode;

        // check what invoice is about to be viewed and set transaction code 301 if its special cancellation as we need only special cancellations item transaction.
        // transaction code = 301 -> Special Cancellation transactions
        // transaction code = 300 -> all selling transactions
        // transaction code = 100 -> all handwritten selling transactions
        if(invoicetype_label.getText().equals("SPECIAL CANCELLATION")){
            transactionCode = 301;
        } else if(handwrited_label.getText().equals("1")){
            transactionCode = 100;
        } else {
            transactionCode = 300;
        }

        viewCustomerInvoiceController.loadTableWithData(selectedID.getText(), transactionCode);

        Stage viewCustomerInvoiceStage = new Stage();
        Scene viewCustomerInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        viewCustomerInvoiceStage.getIcons().add(image);
        viewCustomerInvoiceStage.setX(203);
        viewCustomerInvoiceStage.setY(106);
        viewCustomerInvoiceStage.setScene(viewCustomerInvoiceScene);
        viewCustomerInvoiceStage.setTitle("Customer invoice (View invoice)");
        viewCustomerInvoiceStage.setAlwaysOnTop(true);
        viewCustomerInvoiceStage.setResizable(false);
        viewCustomerInvoiceStage.show();

    }

    public void selectTransactionsOfHandwrittenInvoice(String invoiceID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM item_transactions1 " +
                "WHERE customer_invoiceID = " + invoiceID + " AND transaction_code = 300";

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                itemTransactionList.add(new ItemTransaction(rs.getInt("id"), rs.getString("date"), rs.getString("time"), rs.getString("item_code"), rs.getString("item_description"), rs.getDouble("quantity"), rs.getDouble("unit_price"), rs.getDouble("discount"), rs.getDouble("vat"), rs.getString("etiology"), rs.getDouble("total"), rs.getInt("transaction_code"), rs.getInt("customer_invoiceID")));
            }

        } catch (SQLException e) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

    }

}

