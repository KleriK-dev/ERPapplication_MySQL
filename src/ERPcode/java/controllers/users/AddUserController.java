package controllers.users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.User;
import specialAlerts.SpecialAlert;

public class AddUserController {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField id;

    @FXML
    private TextField namesurname;

    @FXML
    private TextField password;

    @FXML
    private Button saveButton;

    @FXML
    private TextField username;

    SpecialAlert alert = new SpecialAlert();

    @FXML
    void AddUser(ActionEvent event) {

        Integer uid = Integer.valueOf(id.getText());
        String uname = username.getText().trim();
        String upass = password.getText();
        String fullname = namesurname.getText().trim();

        if(uname.equals("") || upass.equals("")){ // check if there are empty fields
            alert.show("Warning", "Empty fields! Please fill them all!", Alert.AlertType.WARNING);
        }else{ //else create user object with the parameters

            User user = new User(uid, uname, upass, fullname);

            if(user.checkSameUsername()){ //check if username exists in database
                alert.show("Warning", "This username exists, please enter different username!", Alert.AlertType.WARNING);
            } else { // else add that user and close this window

                user.addUser();

                Stage customerStage = (Stage) saveButton.getScene().getWindow();
                customerStage.close();

                UsersMainController.getInstance().RefreshData(); // load new data by using UserMainController's method

            }

        }

    }

    @FXML
    void CloseAddUserWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    public void getUserID(String ID){

        id.setText(ID);

    }


}
