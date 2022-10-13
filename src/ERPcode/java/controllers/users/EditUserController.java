package controllers.users;

import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.User;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditUserController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button showPasswordButton;

    @FXML
    private TextField id;

    @FXML
    private TextField namesurname;

    @FXML
    private TextField password;

    @FXML
    private Button updateButton;

    @FXML
    private TextField username;

    private User user;
    SpecialAlert alert = new SpecialAlert();

    @FXML
    void CloseAddUserWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    void ShowPassword(ActionEvent event) {

        String editingUserID = id.getText();

        Connection conn = MySQLConnection.connectToDB();
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT password FROM users WHERE id = ?";

        String uspass = null;

        try {
            prst = conn.prepareStatement(query);
            prst.setString(1, editingUserID);
            rs = prst.executeQuery();
            while (rs.next()) {
                uspass = rs.getString("password");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        password.setText(uspass);

    }

    @FXML
    void UpdateUser(ActionEvent event) {

        //store textfields to string and int variables
        int selectedid = Integer.parseInt(id.getText());
        String newname = username.getText().trim();
        String newpass = password.getText();
        String newfullname = namesurname.getText().trim();

        if(newname.equals("") || newpass.equals("")){
            alert.show("Warning", "Empty fields! Please fill them all!", Alert.AlertType.WARNING);
        }else {

            User user = new User(selectedid, newname, newpass, newfullname); // pass them to user object

            if(user.checkSameUsernameOnUpdate()){ //check if username exists in database
                alert.show("Warning", "This username exists, please enter different username!", Alert.AlertType.WARNING);
            } else { // else add that user and close this window

                user.editUser();

                Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
                stage.close();

                // use RefreshData method of the UsersMainController class from this controller
                // so it can load the edited row on tableview without pressing refresh button
                UsersMainController.getInstance().RefreshData();

            }
        }

    }

    /* this method gets the data from previous stage table
    * and set them to the textfilds of this new stage */
    public void getData(User selectedUser) {

        user = selectedUser;

        id.setText(String.valueOf(user.getId()));
        username.setText(user.getUsername());
        namesurname.setText(user.getNamesurname());

    }
}
