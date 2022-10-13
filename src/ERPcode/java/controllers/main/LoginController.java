package controllers.main;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import objects.User;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Optional;

public class LoginController {

    @FXML
    private Button btnok;

    @FXML
    private Button cancelButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onEnter(KeyEvent event) throws IOException {

        if (event.getCode() == KeyCode.ENTER){

            String uname = username.getText().trim().toLowerCase();
            String pass = password.getText();

            if (uname.equals("")) { //check if username is empty
                loginMessageLabel.setText("Username is Blank!"); //display username warning
            } else if(pass.equals("")){ //check if password is empty
                loginMessageLabel.setText("Password is Blank!"); //display password warning
            }else {

                User user = new User(null, uname, pass, null);

                if(user.checkUserLogin()){
                    btnok.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/main/MainWindow.fxml"));
                    Parent root = loader.load();

                    MainController maincontroller = loader.getController(); //get access to the methods of MainController
                    maincontroller.showProgramUser(user); //pass the username that will log in

                    Stage mainStage = new Stage();
                    Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
                    mainStage.getIcons().add(image);
                    mainStage.setScene(new Scene(root));
                    mainStage.setTitle("ERP System© (" + displayRowOneOfBusinessInformations() + ")");
                    mainStage.setMaximized(true);
                    mainStage.show();

                    mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {

                            we.consume();

                            Optional<ButtonType> result = alert.showConfirmation("Exit confirmation", "Do you want to exit? \nIt's recommended to take a backup before exiting!");

                            if(result.get() == ButtonType.OK){
                                Platform.exit();
                                System.exit(0);
                            }

                        }
                    });

                }

            }

        }

    }

    @FXML
    private void signin(ActionEvent event) throws IOException {

        String uname = username.getText().trim().toLowerCase();
        String pass = password.getText();

        if (uname.equals("")) { //check if username is empty
            loginMessageLabel.setText("Username is Blank!"); //display username warning
        } else if(pass.equals("")){ //check if password is empty
            loginMessageLabel.setText("Password is Blank!"); //display password warning
        }else {

            User user = new User(null, uname, pass, null);

            if (user.checkUserLogin()) {
                btnok.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/main/MainWindow.fxml"));
                Parent root = loader.load();

                MainController maincontroller = loader.getController(); //get access to the methods of MainController
                maincontroller.showProgramUser(user); //pass the user that will log in


                Stage mainStage = new Stage();
                Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
                mainStage.getIcons().add(image);
                mainStage.setScene(new Scene(root));
                mainStage.setTitle("ERP System© (" + displayRowOneOfBusinessInformations() + ")");
                mainStage.setMaximized(true);
                mainStage.show();

                mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {

                        we.consume();

                        Optional<ButtonType> result = alert.showConfirmation("Exit confirmation", "Do you want to exit? Is recommended to take a backup before exiting!");

                        if(result.get() == ButtonType.OK){
                            Platform.exit();
                            System.exit(0);
                        }

                    }
                });

            }

        }

    }

    public String displayRowOneOfBusinessInformations(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT one FROM business_data";
        Statement st;
        ResultSet rs;

        String rowOne = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                rowOne = rs.getString("one");
                return rowOne;

            }

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

        return "";

    }

}
