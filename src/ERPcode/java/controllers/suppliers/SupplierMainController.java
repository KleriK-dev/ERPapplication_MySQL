package controllers.suppliers;

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
import objects.Supplier;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class SupplierMainController {

    @FXML
    private Button addSupplierButton;

    @FXML
    private Label address_label;

    @FXML
    private Label area_label;

    @FXML
    private Label brandname_label;

    @FXML
    private Button closeWindowButton;

    @FXML
    private TableView<Supplier> table1;

    @FXML
    private TableColumn<Supplier, String> col1_address;

    @FXML
    private TableColumn<Supplier, String> col1_brandname;

    @FXML
    private TableColumn<Supplier, String> col1_email;

    @FXML
    private TableColumn<Supplier, Integer> col1_id;

    @FXML
    private TableColumn<Supplier, String> col1_taxcode;

    @FXML
    private Button deleteButton;

    @FXML
    private Label doy_label;

    @FXML
    private Button editButton;

    @FXML
    private Label email_label;

    @FXML
    private Label id_label;

    @FXML
    private MenuItem menuDeleteButton;

    @FXML
    private MenuItem menuEditButton;

    @FXML
    private MenuItem menuViewButton;

    @FXML
    private Label name_label;

    @FXML
    private MenuItem newSupplireMenuButton;

    @FXML
    private Label phone1_label;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField registrationNumber;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField selectedID;

    @FXML
    private Label surname_label;

    @FXML
    private Label taxcode_label;

    @FXML
    private Label insertedBy_label;

    @FXML
    private Button viewButton;

    @FXML
    private TextField ProgramUserField;

    ObservableList<Supplier> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    /* From line 131 to line 137
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */
    private static SupplierMainController instance;

    public SupplierMainController(){
        instance = this;
    }

    public static SupplierMainController getInstance(){
        return instance;
    }

    @FXML
    public void initialize() {

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    private void CloseSupplierWindow(ActionEvent event) {

        Stage supplierStage = (Stage) closeWindowButton.getScene().getWindow();
        supplierStage.close();

    }

    @FXML
    private void OpenAddNewSupplierWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/suppliers/NewSupplierWindow.fxml"));
        Parent root = loader.load();


        //pass user to newSupplier Controller
        NewSupplierController newSupplierController = loader.getController();
        newSupplierController.showProgramUser(ProgramUserField.getText());
        newSupplierController.displaySupplierID(commons.getNextID("supplier", "supplier_id"));

        Stage addSupplierStage = new Stage();
        Scene addSupplierScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        addSupplierStage.getIcons().add(image);
        addSupplierStage.setX(203);
        addSupplierStage.setY(106);
        addSupplierStage.setScene(addSupplierScene);
        addSupplierStage.setTitle("Suppliers (New supplier)");
        addSupplierStage.setAlwaysOnTop(true);
        addSupplierStage.setResizable(false);
        addSupplierStage.show();

    }

    @FXML
    private void DeleteSupplier(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select a supplier from table!", Alert.AlertType.WARNING);
        } else {

            int supplier_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            Supplier supplier = new Supplier(supplier_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null); // use supplier contructor to pass the variable
            supplier.deleteSupplier(); // use deleteSupplier method of the object Supplier
            loadData(); // load new rows
            countRows(); // count new rows
            selectedID.clear(); // empty the selectedid textfield
            clearInfoTable(); // empty textfields from the info table as this Supplier does not exist anymore
        }

    }

    @FXML
    private void HandleSelectedRow(MouseEvent event) {

        index = table1.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col1_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displaySupplierInfo(); // add suppliers info to the textfields
        }

        //on double click to a row, open the edit supplier window
        table1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    try {
                        editSupplierWindow();
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
                displaySupplierInfo(); // add supplier info to the textfields
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select a supplier from table!", Alert.AlertType.WARNING);
            } else {
                editSupplierWindow();
            }

        }

    }

    @FXML
    private void OpenEditSupplierWindow(ActionEvent event) throws IOException {

        editSupplierWindow();

    }

    @FXML
    private void OpenViewSupplierWindow(ActionEvent event) throws IOException {

        try{
            FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getResource("/fxml/suppliers/ViewSupplierWindow.fxml"));
            Parent root = loader.load();

            //pass selected data from this controller to the ViewSupplierController
            ViewSupplierController viewSupplierController = loader.getController();
            viewSupplierController.getData(table1.getSelectionModel().getSelectedItem());

            Stage viewSupplierStage = new Stage();
            Scene viewSupplierScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            viewSupplierStage.getIcons().add(image);
            viewSupplierStage.setX(203);
            viewSupplierStage.setY(106);
            viewSupplierStage.setScene(viewSupplierScene);
            viewSupplierStage.setTitle("Suppliers (View supplier)");
            viewSupplierStage.setAlwaysOnTop(true);
            viewSupplierStage.setResizable(false);
            viewSupplierStage.show();
        }
        catch (Exception ex){
            alert.show("Warning", "Select a supplier from table!", Alert.AlertType.WARNING);
        }


    }

    @FXML
    private void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    private void SearchSupplier(ActionEvent event) {

        searchEngine();

    }

    @FXML
    void SearchSupplierFromTextfield(MouseEvent event) {
        searchEngine();
    }

    private void displaySupplierInfo(){

        index = table1.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, username AS inserted_by FROM supplier " +
                "INNER JOIN users ON users.id = supplier.user_id " +
                "WHERE users.id = (SELECT user_id FROM supplier WHERE supplier_id = ?) " +
                "AND supplier.supplier_id = ?";
        PreparedStatement prst;
        ResultSet rs;

        Integer supplierID = col1_id.getCellData(index);
        String brandname = null;
        String taxcode = null;
        String DOY = null;
        String address = null;
        String phone1 = null;
        String area = null;
        String name = null;
        String surname = null;
        String email = null;
        String insertedBy = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierID);
            prst.setInt(2, supplierID);
            rs = prst.executeQuery();
            while (rs.next()) {
                brandname = rs.getString("brandname");
                taxcode = rs.getString("taxcode");
                DOY = rs.getString("DOY");
                address = rs.getString("address");
                phone1 = rs.getString("phone1");
                area = rs.getString("area");
                name = rs.getString("name");
                surname = rs.getString("surname");
                email = rs.getString("email");
                insertedBy = rs.getString("inserted_by");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        id_label.setText(String.valueOf(supplierID));
        brandname_label.setText(brandname);
        taxcode_label.setText(taxcode);
        doy_label.setText(DOY);
        address_label.setText(address);
        phone1_label.setText(phone1);
        area_label.setText(area);
        name_label.setText(name);
        surname_label.setText(surname);
        email_label.setText(email);
        insertedBy_label.setText(insertedBy);

    }

    private void clearInfoTable(){

        id_label.setText("");
        brandname_label.setText("");
        taxcode_label.setText("");
        doy_label.setText("");
        address_label.setText("");
        phone1_label.setText("");
        area_label.setText("");
        name_label.setText("");
        surname_label.setText("");
        email_label.setText("");
        insertedBy_label.setText("");
        selectedID.setText("");

    }

    private void loadData(){

        /* Clear the observablelist and then run the query
         * as if user presses refresh button there will not be
         * duplicated data on the tableview */

        oblist.clear();

        Connection conn = MySQLConnection.connectToDB();
        // Select all excpect the default one that has id = 0
        String query = "SELECT *, username AS inserted_by FROM supplier " +
                "INNER JOIN users ON users.id = supplier.user_id " +
                "WHERE NOT supplier_id = 0";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Supplier(rs.getInt("supplier_id"), rs.getString("brandname"), rs.getString("profession"), rs.getString("taxcode"), rs.getString("address"), rs.getString("area"), rs.getString("zipcode"), rs.getString("DOY"), rs.getString("surname"), rs.getString("name"), rs.getString("phone1"), rs.getString("phone2"), rs.getString("faxnumber"), rs.getString("email"), rs.getString("website"), rs.getString("notes"), rs.getString("vatregime_id"), rs.getString("payingway_id"), rs.getString("pricelist_id"), rs.getString("user_id"), rs.getInt("check_taxcode"), rs.getInt("check_doy")));
            }

            col1_id.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
            col1_brandname.setCellValueFactory(new PropertyValueFactory<>("brandname"));
            col1_taxcode.setCellValueFactory(new PropertyValueFactory<>("taxcode"));
            col1_address.setCellValueFactory(new PropertyValueFactory<>("address"));
            col1_email.setCellValueFactory(new PropertyValueFactory<>("email"));

            table1.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    public void searchEngine(){

        //Initial filtered list
        FilteredList<Supplier> filterData = new FilteredList<>(oblist, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Supplier -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (Supplier.getBrandname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on brandname
                } else if (String.valueOf(Supplier.getTaxcode()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on taxcode
                } else if (Supplier.getAddress().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on address
                } else if (Supplier.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on email
                } else if (String.valueOf(Supplier.getSupplierID()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on supplier id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<Supplier> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table1.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table1.setItems(sortData);

    }

    public void RefreshData() { //create this public method so it can be used by AddSupplierController
        loadData();
        countRows();
        clearInfoTable();
    }

    private void countRows(){

        // from the total number we substract 1 as we dont want row with id 0 to be count as a row on table
        Integer numberOfRows = Integer.valueOf(commons.CountRows("supplier")) - 1; // we need to convert it to Integer so the substraction can be done
        String numberOfRowsToString = String.valueOf(numberOfRows); // convert to string so it can be displayed on textfield
        registrationNumber.setText(numberOfRowsToString);

    }

    public void showProgramUser(String userName){ //display who is the user

        ProgramUserField.setText(userName);

    }

    public void editSupplierWindow() throws IOException {

        if(selectedID.getText().equals("")){
            alert.show("Warning", "Select a supplier from table!", Alert.AlertType.WARNING);
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/suppliers/EditSupplierWindow.fxml"));
            Parent root = loader.load();

            //pass selected data from this controller to the EditSupplierController
            EditSupplierController editSupplierController = loader.getController();
            editSupplierController.getData(table1.getSelectionModel().getSelectedItem());

            Stage editSupplierStage = new Stage();
            Scene editSupplierScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            editSupplierStage.getIcons().add(image);
            editSupplierStage.setX(203);
            editSupplierStage.setY(106);
            editSupplierStage.setScene(editSupplierScene);
            editSupplierStage.setTitle("Suppliers (Edit supplier)");
            editSupplierStage.setAlwaysOnTop(true);
            editSupplierStage.setResizable(false);
            editSupplierStage.show();
        }

    }

}
