package controllers.items;

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
import objects.Item;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class ItemMainController {

    @FXML
    private Button addButton;

    @FXML
    private Label barcode_label;

    @FXML
    private Button closeButton;

    @FXML
    private Label code_label;

    @FXML
    private TableView<Item> table1;

    @FXML
    private TableColumn<Item, String> col1_barcode;

    @FXML
    private TableColumn<Item, String> col1_code;

    @FXML
    private TableColumn<Item, String> col1_description;

    @FXML
    private TableColumn<Item, Integer> col1_id;

    @FXML
    private TableColumn<Item, Double> col1_remaining;

    @FXML
    private TableColumn<Item, String> col1_supplier;

    @FXML
    private Button deleteButton;

    @FXML
    private Label description_label;

    @FXML
    private Label discount_label;

    @FXML
    private Button editButton;

    @FXML
    private Label id_label;

    @FXML
    private Label remaining_label;

    @FXML
    private MenuItem menuAddButton;

    @FXML
    private MenuItem menuDeleteButton;

    @FXML
    private MenuItem menuEditButton;

    @FXML
    private MenuItem menuViewButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField registrationNumber;

    @FXML
    private Label retail_label;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField selectedID;

    @FXML
    private Label supplier_label;

    @FXML
    private Label vat_label;

    @FXML
    private Button viewButton;

    @FXML
    private Label wholesale_label;

    ObservableList<Item> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    private static ItemMainController instance;

    public ItemMainController(){
        instance = this;
    }

    public static ItemMainController getInstance(){
        return instance;
    }
    
    @FXML
    void CloseItemWindow(ActionEvent event) {

        Stage itemStage = (Stage) closeButton.getScene().getWindow();
        itemStage.close();

    }

    @FXML
    public void initialize() {

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    void DeleteItem(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select an item from table!", Alert.AlertType.WARNING);
        } else {

            int item_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            Item item = new Item(item_id, null, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null, null, null);// use item contructor to pass the variable
            item.deleteItem(); // use deleteItem method of the object Item
            loadData(); // load new rows
            countRows(); // count new rows
            selectedID.clear(); // empty the selectedid textfield
            clearInfoTable(); // empty textfields from the info table as this item does not exist anymore
        }

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table1.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col1_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displayItemInfo(); // add items info to the textfields
        }

        //on double click to a row, open the edit item window
        table1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    try {
                         openEditItemWindow();
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

            index = table1.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

            if (index <= -1) { // check if index is -1 or smaller then do not act
                return;
            } else {
                selectedID.setText(String.valueOf(col1_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
                displayItemInfo(); // add item info to the textfields
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select an item from table!", Alert.AlertType.WARNING);
            } else {
                openEditItemWindow();
            }

        }

    }

    @FXML
    void OpenAddItemWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/items/NewItemWindow.fxml"));
        Parent root = loader.load();

        NewItemController newItemController = loader.getController();
        newItemController.displayItemID(commons.getNextID("items", "id"));

        Stage addItemStage = new Stage();
        Scene addItemScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        addItemStage.getIcons().add(image);
        addItemStage.setX(203);
        addItemStage.setY(106);
        addItemStage.setScene(addItemScene);
        addItemStage.setTitle("Items (New item)");
        addItemStage.setAlwaysOnTop(true);
        addItemStage.setResizable(false);
        addItemStage.show();

    }

    @FXML
    void OpenEditItemWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")) {
            alert.show("Warning", "Select an item from table!", Alert.AlertType.WARNING);
        } else {
            openEditItemWindow();
        }
    }

    @FXML
    void OpenViewItemWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")) {
            alert.show("Warning", "Select an item from table!", Alert.AlertType.WARNING);
        } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/items/ViewItemWindow.fxml"));
                Parent root = loader.load();

                //pass selected data from this controller to the ViewItemController
                ViewItemController viewItemController = loader.getController();
                viewItemController.getData(table1.getSelectionModel().getSelectedItem());

                Stage addItemStage = new Stage();
                Scene addItemScene = new Scene(root);
                Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
                addItemStage.getIcons().add(image);
                addItemStage.setX(203);
                addItemStage.setY(106);
                addItemStage.setScene(addItemScene);
                addItemStage.setTitle("Items (View item)");
                addItemStage.setAlwaysOnTop(true);
                addItemStage.setResizable(false);
                addItemStage.show();
        }

    }

    @FXML
    void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    void SearchItem(ActionEvent event) {
        searchEngine();
    }

    @FXML
    void SearchItemFromTextfield(MouseEvent event) {
        searchEngine();
    }


    private void displayItemInfo(){

        index = table1.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, vat_categorie.description AS vatcategorie_description, " +
                        "measurement_unit.description AS measurement_unit_description, " +
                        "supplier.brandname AS supplier_brandname " +
                        "FROM `items` " +
                        "INNER JOIN vat_categorie ON vat_categorie.id = items.vat_categorieID " +
                        "INNER JOIN measurement_unit ON measurement_unit.id = items.measurement_unitID " +
                        "INNER JOIN supplier ON supplier.supplier_id = items.supplierID " +
                        "WHERE items.id = ? ";
        PreparedStatement prst;
        ResultSet rs;
        Integer itemID = col1_id.getCellData(index);

        String code = null;
        String description = null;
        String barcode = null;
        String supplier = null;
        double wholesale_price = 0.0;
        double retail_price = 0.0;
        double discount = 0.0;
        double remaining = 0.0;
        String vatcategorie = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, itemID);
            rs = prst.executeQuery();
            while (rs.next()) {
                code = rs.getString("code");
                description = rs.getString("description");
                barcode = rs.getString("barcode");
                supplier = rs.getString("supplier_brandname");
                wholesale_price = rs.getDouble("wholesale_price");
                retail_price = rs.getDouble("retail_price");
                discount = rs.getDouble("discount");
                vatcategorie = rs.getString("vatcategorie_description");
                remaining = rs.getDouble("remaining");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        id_label.setText(String.valueOf(itemID));
        code_label.setText(code);
        description_label.setText(description);
        barcode_label.setText(barcode);
        supplier_label.setText(supplier);
        wholesale_label.setText(String.valueOf(wholesale_price));
        retail_label.setText(String.valueOf(retail_price));
        discount_label.setText(String.valueOf(discount));
        vat_label.setText(vatcategorie);
        remaining_label.setText(String.valueOf(remaining));

    }

    private void clearInfoTable(){

        id_label.setText("");
        code_label.setText("");
        description_label.setText("");
        barcode_label.setText("");
        supplier_label.setText("");
        wholesale_label.setText("");
        retail_label.setText("");
        discount_label.setText("");
        vat_label.setText("");
        remaining_label.setText("");
        selectedID.setText("");

    }

    private void loadData(){

        /* Clear the observablelist and then run the query
         * as if user presses refresh button there will not be
         * duplicated data on the tableview */

        oblist.clear();

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, supplier.brandname AS supplier_brandname " +
                "FROM `items` " +
                "INNER JOIN vat_categorie ON vat_categorie.id = items.vat_categorieID " +
                "INNER JOIN measurement_unit ON measurement_unit.id = items.measurement_unitID " +
                "INNER JOIN supplier ON supplier.supplier_id = items.supplierID ";
        Statement st;
        ResultSet rs;

        String supplierBrandname = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Item(rs.getInt("id"), rs.getString("code"), rs.getString("description"), rs.getString("barcode"), rs.getDouble("purchase_price"), rs.getDouble("wholesale_price"), rs.getDouble("retail_price"), rs.getDouble("discount"), rs.getDouble("remaining"), rs.getInt("supplierID"), rs.getString("supplier_brandname"), rs.getInt("vat_categorieID"), rs.getInt("measurement_unitID"), rs.getInt("retail_contains_vat"), rs.getInt("wholesale_contains_vat")));
            }

            col1_id.setCellValueFactory(new PropertyValueFactory<>("itemID"));
            col1_code.setCellValueFactory(new PropertyValueFactory<>("code"));
            col1_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col1_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            col1_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierBrandname"));
            col1_remaining.setCellValueFactory(new PropertyValueFactory<>("remaining"));

            table1.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    public void searchEngine(){

        //Initial filtered list
        FilteredList<Item> filterData = new FilteredList<>(oblist, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Item -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (Item.getDescription().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on description
                } else if (Item.getCode().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on code
                } else if (Item.getBarcode().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on barcode
                } else if (Item.getSupplierBrandname().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on supplier brandname
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<Item> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table1.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table1.setItems(sortData);

    }

    public void RefreshData() { //create this public method so it can be used by AddItemHandController
        loadData();
        countRows();
        clearInfoTable();
    }

    private void countRows() {

        registrationNumber.setText(commons.CountRows("items"));

    }

    //method that opens edit item window, we need this so we can use it multiple times
    public void openEditItemWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/items/EditItemWindow.fxml"));
        Parent root = loader.load();

        //pass selected data from this controller to the EditItemHandController
        EditItemController editItemController = loader.getController();
        editItemController.getData(table1.getSelectionModel().getSelectedItem());

        Stage addItemStage = new Stage();
        Scene addItemScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        addItemStage.getIcons().add(image);
        addItemStage.setX(203);
        addItemStage.setY(106);
        addItemStage.setScene(addItemScene);
        addItemStage.setTitle("Items (Edit item)");
        addItemStage.setAlwaysOnTop(true);
        addItemStage.setResizable(false);
        addItemStage.show();

    }

}
