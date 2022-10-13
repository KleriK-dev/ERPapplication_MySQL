package objects;

import database.MySQLConnection;
import javafx.scene.control.Alert;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class ItemTransaction {

    private Integer id;
    private String date;
    private String time;
    private String item_code;
    private String item_description;
    private Double quantity;
    private Double unit_price;
    private Double vat;
    private Double discount;
    private String etiology;
    private Double total;
    private Integer transaction_code;
    private Integer invoiceID;
    SpecialAlert alert = new SpecialAlert();

    public ItemTransaction(){}

    public ItemTransaction(Integer id, String date, String time, String item_code, String item_description, Double quantity, Double unit_price, Double vat, Double discount, String etiology, Double total, Integer transaction_code, Integer invoiceID) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.item_code = item_code;
        this.item_description = item_description;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.vat = vat;
        this.discount = discount;
        this.etiology = etiology;
        this.total = total;
        this.transaction_code = transaction_code;
        this.invoiceID = invoiceID;
    }

    //CRUD METHODS
    public void makeTransaction(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO item_transactions1 (date, time, item_code, item_description, quantity, unit_price, discount, vat, etiology, total, transaction_code, customer_invoiceID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setString(1, date);
            prst.setString(2, time);
            prst.setString(3, item_code);
            prst.setString(4, item_description);
            prst.setDouble(5, quantity);
            prst.setDouble(6, unit_price);
            prst.setDouble(7, discount);
            prst.setDouble(8, vat);
            prst.setString(9, etiology);
            prst.setDouble(10, total);
            prst.setInt(11, transaction_code);
            prst.setInt(12, invoiceID);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void deleteTransactions(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM item_transactions1 WHERE customer_invoiceID = ? AND transaction_code = ? ";
        PreparedStatement prst;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, invoiceID);
            prst.setInt(2, transaction_code);
            prst.execute();
        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public boolean checkItemRemaining(String itemCode, double Quantity){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT remaining FROM items WHERE code = '" + itemCode + "'";

        Statement st;
        ResultSet rs;

        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while(rs.next()){

                double Remaing = rs.getDouble("remaining") - Quantity;

                if(Remaing > 0){ //that means we have items

                    return true; // return true

                } else if (Remaing <= 0){ //we have 0 items or negative

                    alert.show("Warning", "Item with item code " + itemCode + " has a remaining of: " + Remaing, Alert.AlertType.WARNING);
                    return false; // return false

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public void updateItemRemaining(String factor){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "UPDATE items " +
                "SET remaining = (remaining" + factor + " ?) " +
                "WHERE code = ? ";

        try {

            prst = conn.prepareStatement(query);
            prst.setDouble(1, quantity);
            prst.setString(2, item_code);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getEtiology() {
        return etiology;
    }

    public void setEtiology(String etiology) {
        this.etiology = etiology;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(Integer transaction_code) {
        this.transaction_code = transaction_code;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }
}
