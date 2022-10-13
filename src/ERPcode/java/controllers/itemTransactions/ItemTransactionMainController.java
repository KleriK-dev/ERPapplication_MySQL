package controllers.itemTransactions;

import controllers.customerInvoices.ViewCustomerInvoiceController;
import controllers.supplierInvoices.ViewSupplierInvoiceController;
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
import objects.ItemTransaction;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class ItemTransactionMainController {

    @FXML
    private TableView<ItemTransaction> table;

    @FXML
    private TableColumn<ItemTransaction, String> col_date;

    @FXML
    private TableColumn<ItemTransaction, Double> col_discount;

    @FXML
    private TableColumn<ItemTransaction, String> col_etiology;

    @FXML
    private TableColumn<ItemTransaction, Integer> col_id;

    @FXML
    private TableColumn<ItemTransaction, Integer> col_invoiceID;

    @FXML
    private TableColumn<ItemTransaction, String> col_itemDescription;

    @FXML
    private TableColumn<ItemTransaction, String> col_itemcode;

    @FXML
    private TableColumn<ItemTransaction, Double> col_price;

    @FXML
    private TableColumn<ItemTransaction, Double> col_quantity;

    @FXML
    private TableColumn<ItemTransaction, String> col_time;

    @FXML
    private TableColumn<ItemTransaction, Double> col_total;

    @FXML
    private TableColumn<ItemTransaction, Integer> col_transactionCode;

    @FXML
    private TableColumn<ItemTransaction, Double> col_vat;

    @FXML
    private Button instructionButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField selectedInvoiceID; //hidden

    @FXML
    private TextField selectedTransactionCode; //hidden

    SpecialAlert alert = new SpecialAlert();
    ObservableList<ItemTransaction> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    @FXML
    private void initialize(){

        loadData();
        addColorsToRows();

    }

    @FXML
    private void OpenInstructionWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/ItemTransactionInstructions.fxml"));
        Parent root = loader.load();

        Stage editItemInstructionStage = new Stage();
        Scene editItemInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        editItemInstructionStage.getIcons().add(image);
        editItemInstructionStage.setX(303);
        editItemInstructionStage.setY(106);
        editItemInstructionStage.setScene(editItemInstructionScene);
        editItemInstructionStage.setTitle("Instructions");
        editItemInstructionStage.setAlwaysOnTop(true);
        editItemInstructionStage.show();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedInvoiceID.setText(String.valueOf(col_invoiceID.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedTransactionCode.setText(String.valueOf(col_transactionCode.getCellData(index)));
        }

        //on double click to a row, open the view invoice window
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    if(selectedTransactionCode.getText().equals("200")){

                        try {
                            openViewSupplierInvoice();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    } else if(selectedTransactionCode.getText().equals("100") || selectedTransactionCode.getText().equals("300") || selectedTransactionCode.getText().equals("301")){

                        try {
                            openViewCustomerInvoice();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                }
            }
        });

    }

    @FXML
    void HandleSelectedRowByKey(KeyEvent event) {

        if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN){

            index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

            if (index <= -1) { // check if index is -1 or smaller then do not act
                return;
            } else {
                selectedInvoiceID.setText(String.valueOf(col_invoiceID.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
                selectedTransactionCode.setText(String.valueOf(col_transactionCode.getCellData(index)));
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedTransactionCode.getText().equals("200")){

                try {
                    openViewSupplierInvoice();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else if(selectedTransactionCode.getText().equals("100") || selectedTransactionCode.getText().equals("300") || selectedTransactionCode.getText().equals("301")){

                try {
                    openViewCustomerInvoice();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        }

    }

    @FXML
    private void SearchTransaction(MouseEvent event) {

        searchEngine();

    }

    public void searchEngine(){

        //Initial filtered list
        FilteredList<ItemTransaction> filterData = new FilteredList<>(oblist, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(ItemTransactionSell -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (ItemTransactionSell.getItem_code().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on item code
                } else if (ItemTransactionSell.getItem_description().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on item description
                } else if (String.valueOf(ItemTransactionSell.getTransaction_code()).indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on transaction code
                } else if (ItemTransactionSell.getDate().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on date
                } else if (String.valueOf(ItemTransactionSell.getInvoiceID()).indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on customer invoice id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<ItemTransaction> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table.setItems(sortData);

    }

    public void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM item_transactions1 ORDER BY id DESC";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new ItemTransaction(rs.getInt("id"), rs.getString("date"), rs.getString("time"), rs.getString("item_code"), rs.getString("item_description"), rs.getDouble("quantity"), rs.getDouble("unit_price"), rs.getDouble("vat"), rs.getDouble("discount"), rs.getString("etiology"), rs.getDouble("total"), rs.getInt("transaction_code"), rs.getInt("customer_invoiceID")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
            col_itemcode.setCellValueFactory(new PropertyValueFactory<>("item_code"));
            col_itemDescription.setCellValueFactory(new PropertyValueFactory<>("item_description"));
            col_transactionCode.setCellValueFactory(new PropertyValueFactory<>("transaction_code"));
            col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            col_price.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
            col_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            col_vat.setCellValueFactory(new PropertyValueFactory<>("vat"));
            col_etiology.setCellValueFactory(new PropertyValueFactory<>("etiology"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            col_invoiceID.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));

            table.setItems(oblist);

        } catch (SQLException e) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

    }

    public void addColorsToRows(){

        table.setRowFactory(tv -> new TableRow<ItemTransaction>() {
            @Override
            public void updateItem(ItemTransaction item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getTransaction_code() == 301) {
                    setStyle("-fx-background-color: #eca7a7");
                } else if (item.getTransaction_code() == 200){
                    setStyle("-fx-background-color:#aceeac");
                } else if (item.getTransaction_code() == 300){
                    setStyle("-fx-background-color:#eeedac");
                } else if(item.getTransaction_code() == 100){
                    setStyle("-fx-background-color:#fcae82");
                }
            }
        });

    }

    public void openViewCustomerInvoice() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/ViewCustomerInvoiceWindow.fxml"));
        Parent root = loader.load();

        ViewCustomerInvoiceController viewCustomerInvoiceController = loader.getController();
        viewCustomerInvoiceController.getData(selectedInvoiceID.getText());
        viewCustomerInvoiceController.loadTableWithData(selectedInvoiceID.getText(), Integer.valueOf(selectedTransactionCode.getText()));

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

    public void openViewSupplierInvoice() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/supplierInvoices/ViewSupplierInvoiceWindow.fxml"));
        Parent root = loader.load();

        ViewSupplierInvoiceController viewSupplierInvoiceController = loader.getController();
        viewSupplierInvoiceController.getData(selectedInvoiceID.getText());
        viewSupplierInvoiceController.loadTableWithData(selectedInvoiceID.getText());

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

}
