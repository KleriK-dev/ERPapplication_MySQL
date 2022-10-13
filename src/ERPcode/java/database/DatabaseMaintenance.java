package database;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseMaintenance implements DBInfo{

    private String serverName = SERVER_NAME;
    private String databaseName = DB_NAME;
    private String userName = USER;
    private String userPassword = PASSWORD;
    private String port = DB_PORT;
    SpecialAlert alert = new SpecialAlert();

    public DatabaseMaintenance(){}

    public void backupDatabase(String filePath){

        Process process = null;

        try{
            Runtime runtime = Runtime.getRuntime(); // create runtime object
            process = runtime.exec("C:/mysql/bin/mysqldump -u" +
                    userName + " --add-drop-database -B " +
                    databaseName + " -r " + filePath); //execute to cmd this command
            int processComplete = process.waitFor(); //get process

            if(processComplete == 0){ // if its 0, file created successfully
                alert.show("Information", "Process completed!", Alert.AlertType.INFORMATION);
            } else {
                alert.show("Error", "Process failed!", Alert.AlertType.ERROR);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void restoreDatabase(String filePath){

        String[] restoreCommand = new String[]{"C:/mysql/bin/mysql.exe",
                "--user="+userName, "--password="+userPassword, "-e", "source "+filePath};

        Process process;

        try{
            Runtime runtime = Runtime.getRuntime(); // create runtime object
            process = runtime.exec(restoreCommand); //execute to cmd this command

            int processComplete = process.waitFor(); //get process

            if(processComplete == 0){ // if its 0, file created successfully
                alert.show("Information", "Process completed!", Alert.AlertType.INFORMATION);
            } else {
                alert.show("Error", "Process failed!", Alert.AlertType.ERROR);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void cleanDatabase(){

        //cleaning in a logical order based on database logic
        clearUsers();
        clearItemTransactions();
        clearCustomerInvoices();
        clearSupplierInvoices();
        clearItems();
        clearCustomers();
        clearSuppliers();
        cleanInvoiceNumbering1();
        cleanInvoiceNumbering2();
        cleanInvoiceNumbering3();
        cleanInvoiceNumbering4();
        cleanInvoiceNumbering5();
        alert.show("Information", "Task completed!", Alert.AlertType.INFORMATION);

    }

    //clearDataMethods
    public void clearUsers(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM users WHERE NOT id = 1";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() +
                    "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE users AUTO_INCREMENT = 2";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() +
                    "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearCustomers(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM customer";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE customer AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearCustomerInvoices(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM customer_invoices";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE customer_invoices AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearSuppliers(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM supplier WHERE NOT supplier_id = 0";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE supplier AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearSupplierInvoices(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM supplier_invoices";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE supplier_invoices AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearItems(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM items";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE items AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void clearItemTransactions(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM item_transactions1";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE item_transactions1 AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void cleanInvoiceNumbering1(){

            Connection conn = MySQLConnection.connectToDB();
            String query = "DELETE FROM invoicenumbering_1";
            PreparedStatement prst;

            try {

                prst = conn.prepareStatement(query);
                prst.execute();

            } catch (SQLException ex) {
                alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
            }

            String query2 = "ALTER TABLE invoicenumbering_1 AUTO_INCREMENT = 1";
            PreparedStatement prst2;

            try {

                prst2 = conn.prepareStatement(query2);
                prst2.execute();

            } catch (SQLException ex) {
                alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
            }

    }

    public void cleanInvoiceNumbering2(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM invoicenumbering_2";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE invoicenumbering_2 AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void cleanInvoiceNumbering3(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM invoicenumbering_3";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE invoicenumbering_3 AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void cleanInvoiceNumbering4(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM invoicenumbering_4";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE invoicenumbering_4 AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void cleanInvoiceNumbering5(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM invoicenumbering_5";
        PreparedStatement prst;

        try {

            prst = conn.prepareStatement(query);
            prst.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        String query2 = "ALTER TABLE invoicenumbering_5 AUTO_INCREMENT = 1";
        PreparedStatement prst2;

        try {

            prst2 = conn.prepareStatement(query2);
            prst2.execute();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }


    // Only getters because we don't want to change data here
    public String getServerName() {
        return serverName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getPort() {
        return port;
    }
}
