package database.toolbox;

import database.DatabaseMaintenance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RestoreController {

    @FXML
    private Button choosePathButton;

    @FXML
    private TextField database;

    @FXML
    private Button exitButton;

    @FXML
    private TextField file;

    @FXML
    private PasswordField password;

    @FXML
    private TextField port;

    @FXML
    private Button restoreButton;

    @FXML
    private TextField server;

    @FXML
    private TextField username;

    DatabaseMaintenance db = new DatabaseMaintenance(); //create DatabaseMaintenance object

    @FXML
    private void initialize(){

        server.setText(db.getServerName());
        database.setText(db.getDatabaseName());
        username.setText(db.getUserName());
        password.setText(db.getUserPassword());
        port.setText(db.getPort());

    }

    @FXML
    private void ChoosePath(ActionEvent event) {

        FileChooser fc = new FileChooser(); // create file chooser object
        fc.setInitialDirectory(new File("C:\\ERP_System\\output")); // set initial directory path
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql") // filter all sql files
        );

        File selectedFile = fc.showOpenDialog(null); // open dialog window to choose path

        if(selectedFile != null){ // if selected file is not null, add path to textfield

            file.setText(selectedFile.getAbsolutePath()); // set the path to the textfield

        }

    }

    @FXML
    private void restoreDatabase(ActionEvent event) {

        String fileWithPath = file.getText();

        db.restoreDatabase(fileWithPath); // call restore method

    }

    @FXML
    private void exit(ActionEvent event) {

        Stage restoreStage = (Stage) exitButton.getScene().getWindow();
        restoreStage.close();

    }

}
