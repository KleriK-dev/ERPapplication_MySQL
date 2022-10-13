package database.toolbox;

import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class BusinessDataController {

    @FXML
    private Button cacnelButton;

    @FXML
    private TextField five;

    @FXML
    private TextField four;

    @FXML
    private TextField one;

    @FXML
    private Button saveButton;

    @FXML
    private TextField seven;

    @FXML
    private TextField six;

    @FXML
    private TextField three;

    @FXML
    private TextField two;

    SpecialAlert alert = new SpecialAlert();

    @FXML
    public void initialize(){

        loadInformations();

    }

    @FXML
    void closeWindow(ActionEvent event) {

        Stage supplierStage = (Stage) cacnelButton.getScene().getWindow();
        supplierStage.close();

    }

    @FXML
    void saveInformations(ActionEvent event) {

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE business_data "
                + "SET one = ?,"
                + "two = ?,"
                + "three = ?,"
                + "four = ?,"
                + "five = ?,"
                + "six = ?,"
                + "seven = ? ";

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, one.getText().trim());
            prst.setString(2, two.getText().trim());
            prst.setString(3, three.getText().trim());
            prst.setString(4, four.getText().trim());
            prst.setString(5, five.getText().trim());
            prst.setString(6, six.getText().trim());
            prst.setString(7, seven.getText().trim());
            prst.executeUpdate();

            } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage supplierStage = (Stage) saveButton.getScene().getWindow();
        supplierStage.close();

    }

    public void loadInformations(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM business_data";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                one.setText(rs.getString("one"));
                two.setText(rs.getString("two"));
                three.setText(rs.getString("three"));
                four.setText(rs.getString("four"));
                five.setText(rs.getString("five"));
                six.setText(rs.getString("six"));
                seven.setText(rs.getString("seven"));

            }

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

}
