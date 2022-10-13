package database.toolbox;

import database.DBInfo;
import database.DatabaseMaintenance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupController{

    @FXML
    private Button backupButton;

    @FXML
    private Button choosePathButton;

    @FXML
    private TextField database;

    @FXML
    private Button exitButton;

    @FXML
    private TextField file;

    @FXML
    private TextField password;

    @FXML
    private TextField port;

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

        String date = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date()); // get current date and format it

        file.setText("C:\\ERP_System\\output\\MyBackup_" + date + ".sql");

    }

    @FXML
    void ChoosePath(ActionEvent event) {

        FileChooser fc = new FileChooser(); // create file chooser object
        fc.setInitialDirectory(new File("C:\\ERP_System\\output")); // set initial directory path
        fc.setInitialFileName("MyBackup");

        File selectedFile = fc.showSaveDialog(null); // open dialog window to choose path

        String date = new SimpleDateFormat("dd_MM_yyyy__HH_mm_ss").format(new Date()); // get current date and format it

        if(selectedFile != null){ // if selected file is not null, add path to textfield

            file.setText(selectedFile.getAbsolutePath() + "_" + date + ".sql"); // set the path to the textfield

        }

    }

    @FXML
    void backupDatabase(ActionEvent event) {

        String filePath = file.getText();

        db.backupDatabase(filePath); // call backup method

    }

    @FXML
    void exit(ActionEvent event) {

        Stage backupStage = (Stage) exitButton.getScene().getWindow();
        backupStage.close();

    }

}
