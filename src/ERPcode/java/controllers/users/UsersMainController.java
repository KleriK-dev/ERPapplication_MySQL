package controllers.users;

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
import objects.User;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class UsersMainController {

    @FXML
    private Button addButton;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, Integer> col_id;

    @FXML
    private TableColumn<User, String> col_namesurname;

    @FXML
    private TableColumn<User, String> col_username;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label id_label;

    @FXML
    private MenuItem menuAddButton;

    @FXML
    private MenuItem menuDeleteButton;

    @FXML
    private MenuItem menuEditButton;

    @FXML
    private Label namesurname_label;

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
    private Label username_label;

    ObservableList<User> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    int index = -1; // create an integer so we can use it to handle clicked rows on table

    /* From line 87 to line 95
    * create an instance of this controller with a constructor
    * and create a getter so it can return this instance
    * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
    * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */
    private static UsersMainController instance;

    public UsersMainController(){
        instance = this;
    }

    public static UsersMainController getInstance(){
        return instance;
    }

    @FXML
    public void initialize() { // initializer that loads and count data every time the stage is opened

        loadData();
        searchEngine();
        countRows();

    }

    @FXML
    private void CloseUsersWindow(ActionEvent event) { // method to close the stage

        Stage customerStage = (Stage) closeButton.getScene().getWindow(); // by pressing the closebutton get the scene
        customerStage.close(); // and then close it

    }

    @FXML
    private void DeleteUser(ActionEvent event) {

        if ((selectedID.getText()).equals("")) { // check if selectedID textfield is empty and show an alert
            alert.show("Warning", "Select a user from table!", Alert.AlertType.WARNING);
        } else if ((selectedID.getText()).equals("1")){
                alert.show("Warning", "This is administrator user, it can not be deleted!", Alert.AlertType.WARNING);
        } else {

            int user_id = Integer.parseInt(selectedID.getText()); // get the value of textfield that has the id from the selected row and parse it to an integer
            User user = new User(user_id, null, null, null); // use user contructor to pass the variable
            user.deleteUser(); // use deleteUser method of the object User
            loadData(); // load new rows
            countRows(); // count new rows
            selectedID.clear(); // empty the selectedid textfield
            clearInfoTable(); // empty textfields from the info table as this user does not exist anymore
        }

    }

    @FXML
    private void HandleSelectedRow(MouseEvent event) { // method that handles the click on a row of tableview

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(col_id.getCellData(index).toString()); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            displayUserInfo(); // add users info to the textfields
        }

        //on double click to a row, open the edit user window
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    try {
                        openEditUserWindow();
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
                displayUserInfo(); // add user info to the textfields
            }

        }

        if(event.getCode() == KeyCode.ENTER){

            if(selectedID.getText().equals("")) {
                alert.show("Warning", "Select user from table!", Alert.AlertType.WARNING);
            } else {
                openEditUserWindow();
            }

        }

    }

    @FXML
    private void OpenAddUserWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/users/AddUserWindow.fxml"));
        Parent root = loader.load();

        AddUserController addUserController = loader.getController();
        addUserController.getUserID(commons.getNextID("users", "id"));

        Stage addUserStage = new Stage();
        Scene addUserScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        addUserStage.getIcons().add(image);
        addUserStage.setX(203);
        addUserStage.setY(106);
        addUserStage.setScene(addUserScene);
        addUserStage.setTitle("Users (Add user)");
        addUserStage.setAlwaysOnTop(true);
        addUserStage.setResizable(false);
        addUserStage.show();

    }

    @FXML
    private void OpenEditUserWindow(ActionEvent event) throws IOException {

        openEditUserWindow();

    }

    @FXML
    private void RefreshData(ActionEvent event) {
        loadData();
        countRows();
        clearInfoTable();
    }

    @FXML
    private void SearchUser(ActionEvent event) {

        searchEngine();

    }

    @FXML
    void SearchUserFromTextfield(MouseEvent event) {
        searchEngine();
    }

    private void displayUserInfo(){

        index = table.getSelectionModel().getSelectedIndex();
        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement prst;
        ResultSet rs;
        Integer userID = col_id.getCellData(index);

        String usname = null;
        String usname_surname = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, userID);
            rs = prst.executeQuery();
            while (rs.next()) {
                usname = rs.getString("username");
                usname_surname = rs.getString("name_surname");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        id_label.setText(String.valueOf(userID));
        username_label.setText(usname);
        namesurname_label.setText(usname_surname);

    }

    private void clearInfoTable(){

        id_label.setText("");
        username_label.setText("");
        namesurname_label.setText("");
        selectedID.setText("");

    }

    private void loadData(){

        /* Clear the observablelist and then run the query
        * as if user presses refresh button there will not be
        * duplicated data on the tableview */

        oblist.clear();

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM users";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new User(rs.getInt("id"), rs.getString("username"),null, rs.getString("name_surname")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
            col_namesurname.setCellValueFactory(new PropertyValueFactory<>("namesurname"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    private void searchEngine(){

        //Initial filtered list
        FilteredList<User> filterData = new FilteredList<>(oblist, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(User -> {

                // If no search value then display all records with no changes
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (User.getUsername().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // means we found a match in username
                } else if (User.getNamesurname().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match in fullname (namesurname)
                } else if (String.valueOf(User.getId()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // means we found a match in user id
                } else {
                    return false; // no matches
                }

            });
        });

        // sort with sortedlist the filtered data
        SortedList<User> sortData = new SortedList<>(filterData);

        // bind sorted result with Table View
        sortData.comparatorProperty().bind(table.comparatorProperty());

        // Apply filtered and sorted data to the Table View
        table.setItems(sortData);

    }

    public void RefreshData() { //create this public method so it can be used by AddUserController
        loadData();
        countRows();
        clearInfoTable();
    }

    private void countRows(){

        registrationNumber.setText(commons.CountRows("users"));

    }

    public void openEditUserWindow() throws IOException {

        if(selectedID.getText().equals("")){
            alert.show("Warning", "Select a user from table!", Alert.AlertType.WARNING);
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/users/EditUserWindow.fxml"));
            Parent root = loader.load();

            //pass selected data from this controller to the EditUserController
            EditUserController controller = loader.getController();
            controller.getData(table.getSelectionModel().getSelectedItem());

            Stage editUserStage = new Stage();
            Scene editUserScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            editUserStage.getIcons().add(image);
            editUserStage.setScene(editUserScene);
            editUserStage.setX(203);
            editUserStage.setY(106);
            editUserStage.setTitle("Users (Edit user)");
            editUserStage.setAlwaysOnTop(true);
            editUserStage.setResizable(false);
            editUserStage.show();
        }

    }

}
