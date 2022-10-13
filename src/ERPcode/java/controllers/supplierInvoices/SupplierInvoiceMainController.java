package controllers.supplierInvoices;

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
import objects.Item;
import objects.SupplierInvoice;
import objects.ItemTransaction;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class SupplierInvoiceMainController {

    @FXML
    private TextField ProgramUserField;

    @FXML
    private Button closeButton;

    @FXML
    private Button instructionButton;

    @FXML
    private TableView<SupplierInvoice> table;

    @FXML
    private TableColumn<SupplierInvoice, Integer> col_id;

    @FXML
    private TableColumn<SupplierInvoice, String> col_invoicetype;

    @FXML
    private TableColumn<SupplierInvoice, String> col_invoiceNumber;

    @FXML
    private TableColumn<SupplierInvoice, String> col_supplier;

    @FXML
    private TableColumn<SupplierInvoice, String> col_date;

    @FXML
    private TableColumn<SupplierInvoice, String> col_time;

    @FXML
    private TableColumn<SupplierInvoice, String> col_total;

    @FXML
    private Label supplier_label;

    @FXML
    private Label date_label;

    @FXML
    private Button deleteButton;

    @FXML
    private MenuItem deleteInvoiceMenu;

    @FXML
    private Label id_label;

    @FXML
    private Label invoicenum_label;

    @FXML
    private Label invoicetype_label;

    @FXML
    private Button newInvoiceButton;

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
    private Label time_label;

    @FXML
    private Label total_label;

    @FXML
    private Label user_label;

    @FXML
    private Button viewInvoiceButton;

    @FXML
    private MenuItem viewInvoiceMenu;

    ObservableList<SupplierInvoice> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    ObservableList<ItemTransaction> itemTransactionList = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    /* create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static SupplierInvoiceMainController instance;

    public SupplierInvoiceMainController() {
        instance = this;
    }

    public static SupplierInvoiceMainController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    private void CloseSupplierInvoiceWindow(ActionEvent event) {

        Stage supplierInvoiceStage = (Stage) closeButton.getScene().getWindow();
        supplierInvoiceStage.close();

    }

    @FXML
    private void OpenInstructions(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/SupplierInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage supplierInvoiceInstructionStage = new Stage();
        Scene supplierInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        supplierInvoiceInstructionStage.getIcons().add(image);
        supplierInvoiceInstructionStage.setX(303);
        supplierInvoiceInstructionStage.setY(106);
        supplierInvoiceInstructionStage.setScene(supplierInvoiceInstructionScene);
        supplierInvoiceInstructionStage.setTitle("Instructions");
        supplierInvoiceInstructionStage.setAlwaysOnTop(true);
        supplierInvoiceInstructionStage.show();

    }

    @FXML
    private void DeleteSupplierInvoice(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
        } else {

            int invoice_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            SupplierInvoice invoice = new SupplierInvoice(invoice_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, null); // use supplier contructor to pass the variable

            Optional<ButtonType> result = alert.showConfirmation("Delete Confirmation", "This invoice will be deleted permanently, do you want to continue?");

            if (result.get() == ButtonType.OK) {
                invoice.deleteSupplierInvoice(); // use deleteSupplierInvoice method of the object SupplierInvoice

                Item item = new Item();

                //DEN KANEI TA RESET ON DELETE
                for (ItemTransaction transaction : itemTransactionList) { // loop the list that was created with the transactions of the invoice
                    item.updateItemWithSupplierAndPurchasePrice(0, 0.0, transaction.getItem_code());
                    transaction.updateItemRemaining("-"); // and update their remainings by substituting them back
                }

                ItemTransaction transaction = new ItemTransaction();
                transaction.setInvoiceID(invoice_id);
                transaction.setTransaction_code(200);
                transaction.deleteTransactions();

            }
        }

        loadData(); // load new rows
        countRows(); // count new rows
        selectedID.clear(); // empty the selectedid textfield
        clearInfoTable(); // empty textfields from the info table as this supplier does not exist anymore

    }

    @FXML
    private void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displaySupplierInvoiceInfo(); // add suppliers info to the textfields
            loadTransactionList(selectedID.getText());
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
    private void HandleSelectedRowByKey(KeyEvent event) {

        if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN){

            index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

            if (index <= -1) { // check if index is -1 or smaller then do not act
                return;
            } else {
                selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
                displaySupplierInvoiceInfo(); // add supplier's invoice info to the textfields
                loadTransactionList(selectedID.getText());
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
            } else {
                try {
                    openViewInvoiceWindow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @FXML
    private void OpenNewSupplierInvoiceWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/supplierInvoices/NewSupplierInvoiceWindow.fxml"));
        Parent root = loader.load();

        //pass user to newSupplierInvoice Controller
        NewSupplierInvoiceController newSupplierInvoiceController = loader.getController();
        newSupplierInvoiceController.showProgramUser(ProgramUserField.getText());
        newSupplierInvoiceController.displayInvoiceID(commons.getNextID("supplier_invoices", "id"));

        Stage newSupplierInvoiceStage = new Stage();
        Scene newSupplierInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newSupplierInvoiceStage.getIcons().add(image);
        newSupplierInvoiceStage.setX(203);
        newSupplierInvoiceStage.setY(106);
        newSupplierInvoiceStage.setScene(newSupplierInvoiceScene);
        newSupplierInvoiceStage.setTitle("Supplier invoice (New invoice)");
        newSupplierInvoiceStage.setAlwaysOnTop(true);
        newSupplierInvoiceStage.setResizable(false);
        newSupplierInvoiceStage.show();

        // handle when NewSupplierInvoice window is closed by X button
        newSupplierInvoiceStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                we.consume(); // consume the event so that the window does not close when cancel button is pressed to the confirmation window

                Optional<ButtonType> result = alert.showConfirmation("Cancel confirmation", "This invoice will be lost with all the items, are you sure you want to continue?");

                if(result.get() == ButtonType.OK) { // if its pressed OK

                    newSupplierInvoiceStage.close();

                }
            }
        });

    }

    @FXML
    private void OpenViewSupplierInvoiceWindow(ActionEvent event) {

        if(selectedID.getText().equals("")){
            alert.show("Warning", "Select an invoice from table!", Alert.AlertType.WARNING);
        } else {
            try {
                openViewInvoiceWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    public void RefreshData() {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    private void SearchSupplierInvoice(ActionEvent event) {
        searchEngine();
    }

    @FXML
    private void SearchSupplierInvoiceFromTextfield(MouseEvent event) {
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
        String query = "SELECT *, brandname AS supplier, description AS invoiceType FROM supplier_invoices " +
                "LEFT JOIN supplier ON supplier.supplier_id = supplier_invoices.supplier_id " +
                "LEFT JOIN type_of_supplier_document ON type_of_supplier_document.id = supplier_invoices.invoicetype_id " +
                "ORDER BY supplier_invoices.id DESC";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new SupplierInvoice(rs.getInt("id"), rs.getString("date"),
                        rs.getString("time"), rs.getInt("supplier_id"), rs.getString("supplier"), rs.getInt("invoicetype_id"),
                        rs.getString("invoiceType"), rs.getString("invoice_number"), rs.getInt("payingway_id"), rs.getString("purpose_of_tracking"),
                        rs.getString("from_place"), rs.getString("to_place"), rs.getString("license_plate"),
                        rs.getString("vatregime_id"), rs.getString("user_id"), rs.getDouble("initial_value"), rs.getDouble("discount_percent"),
                        rs.getDouble("discount_value"),rs.getDouble("value_beforevat"),rs.getDouble("vat_value"),
                        rs.getDouble("quantity"),rs.getDouble("total"), rs.getString("remarks")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("supplierInvoiceID"));
            col_date.setCellValueFactory(new PropertyValueFactory<>("insertionDate"));
            col_time.setCellValueFactory(new PropertyValueFactory<>("insertionTime"));
            col_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierBrandname"));
            col_invoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
            col_invoicetype.setCellValueFactory(new PropertyValueFactory<>("invoiceTypeDescription"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("total"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

    }

    private void displaySupplierInvoiceInfo(){

        index = table.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, supplier.brandname AS supplier, " +
                "type_of_supplier_document.description AS invoiceType, " +
                "paying_way.description AS payingMethod, " +
                "users.username AS regUser " +
                "FROM supplier_invoices " +
                "INNER JOIN type_of_supplier_document ON type_of_supplier_document.id = supplier_invoices.invoicetype_id " +
                "INNER JOIN supplier ON supplier.supplier_id = supplier_invoices.supplier_id " +
                "INNER JOIN paying_way ON paying_way.id = supplier_invoices.payment_id " +
                "INNER JOIN users ON users.id = supplier_invoices.user_id " +
                "WHERE supplier_invoices.id = ? ";

        PreparedStatement prst;
        ResultSet rs;
        Integer supplierInvoiceID = col_id.getCellData(index);

        String date = null;
        String time = null;
        String supplier = null;
        String invoiceType = null;
        String invoiceNumber = null;
        String payingMethod = null;
        String remarks = null;
        String user = null;
        String total = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierInvoiceID);
            rs = prst.executeQuery();
            while (rs.next()) {
                date = rs.getString("date");
                time = rs.getString("time");
                supplier = rs.getString("supplier");
                invoiceType = rs.getString("invoiceType");
                invoiceNumber = rs.getString("invoice_number");
                payingMethod = rs.getString("payingMethod");
                remarks = rs.getString("remarks");
                user = rs.getString("regUser");
                total = rs.getString("total");
            }
        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

        id_label.setText(String.valueOf(supplierInvoiceID));
        date_label.setText(date);
        time_label.setText(time);
        supplier_label.setText(supplier);
        invoicetype_label.setText(invoiceType);
        invoicenum_label.setText(invoiceNumber);
        payingmethod_label.setText(payingMethod);
        remark_label.setText(remarks);
        user_label.setText(user);
        total_label.setText(total);

    }

    private void clearInfoTable(){

        id_label.setText("");
        date_label.setText("");
        time_label.setText("");
        supplier_label.setText("");
        invoicetype_label.setText("");
        invoicenum_label.setText("");
        payingmethod_label.setText("");
        remark_label.setText("");
        user_label.setText("");
        total_label.setText("");
        selectedID.setText("");

    }

    public void searchEngine(){

        //Initial filtered list
        FilteredList<SupplierInvoice> filterData = new FilteredList<>(oblist, b -> true);

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(SupplierInvoice -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (SupplierInvoice.getInsertionDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on date
                } else if (SupplierInvoice.getSupplierBrandname().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on brandname
                } else if (SupplierInvoice.getInvoiceNumber().replaceAll("0", "").indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on invoice number
                } else if (SupplierInvoice.getInvoiceTypeDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on invoiceType
                } else if (String.valueOf(SupplierInvoice.getSupplierInvoiceID()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on supplier invoice id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<SupplierInvoice> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table.setItems(sortData);

    }

    private void countRows(){

        registrations.setText(commons.CountRows("supplier_invoices"));

    }

    //create a method that opens view invoice window, so we can call it every time we want
    public void openViewInvoiceWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/supplierInvoices/ViewSupplierInvoiceWindow.fxml"));
        Parent root = loader.load();

        ViewSupplierInvoiceController viewSupplierInvoiceController = loader.getController();
        viewSupplierInvoiceController.getData(table.getSelectionModel().getSelectedItem());
        viewSupplierInvoiceController.loadTableWithData(selectedID.getText());

        Stage viewSupplierInvoiceStage = new Stage();
        Scene viewSupplierInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        viewSupplierInvoiceStage.getIcons().add(image);
        viewSupplierInvoiceStage.setX(203);
        viewSupplierInvoiceStage.setY(106);
        viewSupplierInvoiceStage.setScene(viewSupplierInvoiceScene);
        viewSupplierInvoiceStage.setTitle("Supplier invoice (View invoice)");
        viewSupplierInvoiceStage.setAlwaysOnTop(true);
        viewSupplierInvoiceStage.setResizable(false);
        viewSupplierInvoiceStage.show();

    }

    public void loadTransactionList(String invoiceID){

        itemTransactionList.clear();

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM item_transactions1 WHERE customer_invoiceID = " + invoiceID + " AND transaction_code = 200";

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
