package controllers.customers;

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
import objects.Customer;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class CustomersMainController {

    @FXML
    private Button addButton;

    @FXML
    private Label address_label;

    @FXML
    private Label area_label;

    @FXML
    private Label brandname_label;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Customer> table1;

    @FXML
    private TableColumn<Customer, String> col1_address;

    @FXML
    private TableColumn<Customer, String> col1_brandname;

    @FXML
    private TableColumn<Customer, String> col1_email;

    @FXML
    private TableColumn<Customer, Integer> col1_id;

    @FXML
    private TableColumn<Customer, Integer> col1_taxcode;

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
    private MenuItem menuAddButton;

    @FXML
    private MenuItem menuDeleteButton;

    @FXML
    private MenuItem menuEditButton;

    @FXML
    private MenuItem menuViewButton;

    @FXML
    private Label name_label;

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
    private TextField ProgramUserField;

    @FXML
    private Label insertedBy_label;

    @FXML
    private Button viewButton;

    // create observablelist to get the data from query and display them to table
    ObservableList<Customer> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    /* From line 132 to line 136
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */
    private static CustomersMainController instance;

    public CustomersMainController(){
        instance = this;
    }

    public static CustomersMainController getInstance(){
        return instance;
    }

    @FXML
    public void initialize() {

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    private void OpenAddCustomerWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customers/NewCustomerWindow.fxml"));
        Parent root = loader.load();

        //pass user to newCustomer Controller
        NewCustomerController newCustomersMainController = loader.getController();
        newCustomersMainController.showProgramUser(ProgramUserField.getText());
        newCustomersMainController.displayCustomerID(commons.getNextID("customer", "customer_id"));

        Stage addCustomerStage = new Stage();
        Scene addCustomerScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        addCustomerStage.getIcons().add(image);
        addCustomerStage.setX(203);
        addCustomerStage.setY(106);
        addCustomerStage.setScene(addCustomerScene);
        addCustomerStage.setTitle("Customers (New customer)");
        addCustomerStage.setAlwaysOnTop(true);
        addCustomerStage.setResizable(false);
        addCustomerStage.show();

    }

    @FXML
    private void OpenViewCustomerWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")) {
            alert.show("Warning", "Select a customer from table!", Alert.AlertType.WARNING);
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/customers/ViewCustomerWindow.fxml"));
            Parent root = loader.load();

            //pass selected data from this controller to the ViewCustomerController
            ViewCustomerController controller = loader.getController();
            controller.getData(table1.getSelectionModel().getSelectedItem());

            Stage showCustomerStage = new Stage();
            Scene showCustomerScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            showCustomerStage.getIcons().add(image);
            showCustomerStage.setX(203);
            showCustomerStage.setY(106);
            showCustomerStage.setScene(showCustomerScene);
            showCustomerStage.setTitle("Customers (View customer)");
            showCustomerStage.setAlwaysOnTop(true);
            showCustomerStage.setResizable(false);
            showCustomerStage.show();
        }

    }

    @FXML
    private void OpenEditCustomerWindow(ActionEvent event) throws IOException {

        if(selectedID.getText().equals("")) {
            alert.show("Warning", "Select a customer from table!", Alert.AlertType.WARNING);
        } else {
            openEditCustomerWindow();
        }
    }


    @FXML
    private void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    private void DeleteCustomer(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select a customer from table!", Alert.AlertType.WARNING);
        } else {

            int customer_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            Customer customer = new Customer(customer_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null); // use customer contructor to pass the variable
            customer.deleteCustomer(); // use deleteCustomer method of the object Customer
            loadData(); // load new rows
            countRows(); // count new rows
            selectedID.clear(); // empty the selectedid textfield
            clearInfoTable(); // empty textfields from the info table as this customer does not exist anymore
        }

    }

    @FXML
    private void CloseCustomersWindow(ActionEvent event) {
        Stage customerStage = (Stage) closeButton.getScene().getWindow();
        customerStage.close();
    }

    @FXML
    private void SearchCustomer(ActionEvent actionEvent) {

        searchEngine();

    }

    @FXML
    void SearchCustomerFromTextfield(MouseEvent event) {
        searchEngine();
    }

    @FXML
    private void HandleSelectedRow(MouseEvent mouseEvent) {

        index = table1.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col1_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displayCustomerInfo(); // add customers info to the textfields
        }

        //on double click to a row, open the edit customer window
        table1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    try {
                        openEditCustomerWindow();
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
                displayCustomerInfo(); // add customers info to the textfields
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select a customer from table!", Alert.AlertType.WARNING);
            } else {
                openEditCustomerWindow();
            }

        }

    }

    private void displayCustomerInfo(){

        index = table1.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT *, username AS inserted_by FROM customer " +
                "INNER JOIN users ON users.id = customer.user_id " +
                "WHERE users.id = (SELECT user_id FROM customer WHERE customer_id = ?) " +
                "AND customer.customer_id = ?";
        PreparedStatement prst;
        ResultSet rs;
        Integer customerID = col1_id.getCellData(index);

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
            prst.setInt(1, customerID);
            prst.setInt(2, customerID);
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

        id_label.setText(String.valueOf(customerID));
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
        String query = "SELECT * FROM customer ";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Customer(rs.getInt("customer_id"), rs.getString("brandname"), rs.getString("profession"), rs.getString("taxcode"), rs.getString("address"), rs.getString("area"), rs.getString("zipcode"), rs.getString("DOY"), rs.getString("surname"), rs.getString("name"), rs.getString("phone1"), rs.getString("phone2"), rs.getString("faxnumber"), rs.getString("email"), rs.getString("website"), rs.getString("notes"), rs.getString("vatregime_id"), rs.getString("payingway_id"), rs.getString("pricelist_id"), rs.getString("user_id"), rs.getInt("check_taxcode"), rs.getInt("check_doy")));
            }

            col1_id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
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
        FilteredList<Customer> filterData = new FilteredList<>(oblist, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Customer -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (Customer.getBrandname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match on brandname
                } else if (Customer.getTaxcode().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on taxcode
                } else if (Customer.getAddress().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on address
                } else if (Customer.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on email
                } else if (String.valueOf(Customer.getCustomerID()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match on customer id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<Customer> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table1.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table1.setItems(sortData);

    }

    public void RefreshData() { //create this public method so it can be used by AddCustomerController
        loadData();
        countRows();
        clearInfoTable();
    }

    private void countRows(){

        registrationNumber.setText(commons.CountRows("customer"));

    }

    public void showProgramUser(String userName){ //display who is the user

        ProgramUserField.setText(userName);

    }

    public void openEditCustomerWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customers/EditCustomerWindow.fxml"));
        Parent root = loader.load();

        //pass selected data from this controller to the EditCustomerController
        EditCustomerController controller = loader.getController();
        controller.getData(table1.getSelectionModel().getSelectedItem());

        Stage editCustomerStage = new Stage();
        Scene editCustomerScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        editCustomerStage.getIcons().add(image);
        editCustomerStage.setX(203);
        editCustomerStage.setY(106);
        editCustomerStage.setScene(editCustomerScene);
        editCustomerStage.setTitle("Customers (Edit customer)");
        editCustomerStage.setAlwaysOnTop(true);
        editCustomerStage.setResizable(false);
        editCustomerStage.show();

    }

}
