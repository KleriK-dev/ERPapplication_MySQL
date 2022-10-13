package commons;

import database.MySQLConnection;
import javafx.scene.control.Alert;
import specialAlerts.SpecialAlert;

import java.sql.*;

// THIS CLASS PURPOSE IS TO CONTAIN ALL THE METHODS
// THAT ARE USED BY DIFFERENT CONTROLLERS AND CLASSES
// THIS WAY TIME AND ROWS OF CODE ARE SHORTEN
public class CommonMethods {

    SpecialAlert alert = new SpecialAlert();

    public CommonMethods(){}


    public String CountRows(String tableName){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT COUNT(*) AS number_of_" + tableName + " FROM " + tableName;
        Statement st;
        ResultSet rs;

        String numberOfRows = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numberOfRows = rs.getString("number_of_" +  tableName);
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
            ex.getErrorCode();
            ex.getSQLState();
        }

        return numberOfRows;

    }

    // create a method that selects the user's username by his ID
    public String insertedByUser(String user_id) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT username AS inserted_by FROM users " +
                "WHERE id =" + user_id;

        String username = null;

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                username = rs.getString("inserted_by");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
            ex.getErrorCode();
            ex.getSQLState();
        }
        return username;
    }

    // create a method that selects the descriptions by their ID in different selection windows
    public String getSelectionDescription(String tableName, String id) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM " + tableName +
                " WHERE id =" + id;

        String description = null;

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                description = rs.getString("description");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
            ex.getErrorCode();
            ex.getSQLState();
        }
        return description;
    }

    // method that works like AUTO_INCREMENT and with the help of this method we can display the new id to user's screen
    public String getNextID(String tableName, String columnName){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT " + columnName + " FROM " + tableName + " ORDER BY " + columnName + " DESC LIMIT 1 "; //select last row's id of this table
        Statement st;
        ResultSet rs;

        String lastID = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                lastID = rs.getString(columnName);
            }

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

        String defaultId = "1";

        if(lastID != null){

            Integer lastIdAsNumber = Integer.valueOf(lastID);
            Integer newIdAsNumber = lastIdAsNumber + 1; // add one so it can be the next ud

            return String.valueOf(newIdAsNumber);

        }

        return defaultId; // return id 1 if there is no rows

    }

    public String getPriceColumnBasedOnCustomerOrSupplierPriceList(String tableName, String columnName, String id){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM pricelist WHERE id = (SELECT pricelist_id FROM " + tableName + " WHERE " + columnName + " = " + id + ")";

        Statement st;
        ResultSet rs;

        String result = "";

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                result = rs.getString("description");
            }
        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String priceList = null;

        if(result.equals("Wholesale")){

            priceList = "wholesale_price";

        } else {

            priceList = "retail_price";

        }

        return priceList;

    }

    public int checkCustomerOrSupplierVATregime(String tableName, String columnName, String id){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT vatregime_id FROM " + tableName + " WHERE " + columnName + " = " + id;

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                if(rs.getString("vatregime_id").equals("1")){
                    return 1;
                } else if(rs.getString("vatregime_id").equals("2")){
                    return 2;
                } else if(rs.getString("vatregime_id").equals("3")){
                    return 3;
                }

            }
        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        return 1;

    }

    public double getItemVAT(String itemcode){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT vat_percentage FROM vat_categorie WHERE id = (SELECT vat_categorieID FROM items WHERE code = '" + itemcode + "')";

        Statement st;
        ResultSet rs;

        Double vatPercentage = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                vatPercentage = rs.getDouble("vat_percentage");

            }
        } catch (SQLException ex) { //it will catch null exception when the user has not selected a customer
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        return vatPercentage;


    }


}
