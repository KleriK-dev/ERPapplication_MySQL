package database.toolbox;

import database.DatabaseMaintenance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CleanDatabaseController {

    @FXML
    private Button cleanButton;

    @FXML
    private Button closeButton;

    @FXML
    void CleanDatabase(ActionEvent event) {

        DatabaseMaintenance db = new DatabaseMaintenance();
        db.cleanDatabase();

    }

    @FXML
    void closeWindow(ActionEvent event) {

        Stage cleanDatabaseStage = (Stage) closeButton.getScene().getWindow();
        cleanDatabaseStage.close();

    }

}
