package specialAlerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class SpecialAlert {

    public void show(String title, String message, Alert.AlertType alertType) {

        Alert alert = new Alert(Alert.AlertType.NONE);

        //Alert pane has to be on top of every stage when it appears
        //So we take its stage and set it always on top
        /*This needs to be in a try catch block because it will throw
         * a NullPointerException if this alert has to appear again in the same place
         */
        try{
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
        } catch(NullPointerException ex){
            //do nothing as we need to show again the dialogpane
        }

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
    }

    // this method is for confirmation alerts as we need to get the choice that the user did
    // it will return the choice and use it to if statements
    public Optional<ButtonType> showConfirmation(String title, String message){
        Alert alert = new Alert(Alert.AlertType.NONE);
        //this try catch block does the same thing as in the show method
        try{
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
        } catch(NullPointerException ex){
            //do nothing as we need to show again the dialogpane
        }

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }


}
