package objects;

import database.MySQLConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Item {

    private Integer itemID;
    private String code;
    private String description;
    private String barcode;
    private double purchase_price;
    private double wholesale_price;
    private double retail_price;
    private double discount;
    private double remaining;
    private Integer supplierID;
    private String supplierBrandname;
    private Integer vatCategorieID;
    private Integer measurementUnitID;
    private Integer retailContainVat;
    private Integer wholesaleContainVat;
    SpecialAlert alert = new SpecialAlert();

    public Item(){}

    public Item(Integer itemID, String code, String description, String barcode, double purchase_price, double wholesale_price, double retail_price, double discount, double remaining, Integer supplierID, String supplierBrandname, Integer vatCategorieID, Integer measurementUnitID, Integer retailContainVat, Integer wholesaleContainVat){

        this.itemID = itemID;
        this.code = code;
        this.description = description;
        this.barcode = barcode;
        this.purchase_price = purchase_price;
        this.wholesale_price = wholesale_price;
        this.retail_price = retail_price;
        this.discount = discount;
        this.remaining = remaining;
        this.supplierID = supplierID;
        this.supplierBrandname = supplierBrandname;
        this.vatCategorieID = vatCategorieID;
        this.measurementUnitID = measurementUnitID;
        this.retailContainVat = retailContainVat;
        this.wholesaleContainVat = wholesaleContainVat;

    }

    // CRUD METHODS
    public void addItem(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO items (id, code, description, barcode, purchase_price, wholesale_price, retail_price, discount, supplierID, vat_categorieID, measurement_unitID, retail_contains_vat, wholesale_contains_vat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setInt(1, itemID);
            prst.setString(2, code);
            prst.setString(3, description);
            prst.setString(4, barcode);
            prst.setDouble(5, purchase_price);
            prst.setDouble(6, wholesale_price);
            prst.setDouble(7, retail_price);
            prst.setDouble(8, discount);
            prst.setInt(9, supplierID);
            prst.setInt(10, vatCategorieID);
            prst.setInt(11, measurementUnitID);
            prst.setInt(12, retailContainVat);
            prst.setInt(13, wholesaleContainVat);
            prst.executeUpdate();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void editItem(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE items "
                + "SET code = ?,"
                + "description = ?,"
                + "barcode = ?,"
                + "purchase_price = ?,"
                + "wholesale_price = ?,"
                + "retail_price = ?,"
                + "discount = ?,"
                + "supplierID = ?,"
                + "vat_categorieID = ?,"
                + "measurement_unitID = ?,"
                + "retail_contains_vat = ?,"
                + "wholesale_contains_vat = ? "
                + "WHERE id = ?";

        try{
            Optional<ButtonType> result = alert.showConfirmation("Update Confirmation", "This item will be updated with the new data, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setString(1, code);
                prst.setString(2, description);
                prst.setString(3, barcode);
                prst.setDouble(4, purchase_price);
                prst.setDouble(5, wholesale_price);
                prst.setDouble(6, retail_price);
                prst.setDouble(7, discount);
                prst.setInt(8, supplierID);
                prst.setInt(9, vatCategorieID);
                prst.setInt(10, measurementUnitID);
                prst.setInt(11, retailContainVat);
                prst.setInt(12, wholesaleContainVat);
                prst.setInt(13, itemID);
                prst.executeUpdate();
            }
        } catch(SQLException ex){
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void deleteItem(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM items WHERE id = ?";
        PreparedStatement prst;

        try {
            Optional<ButtonType> result = alert.showConfirmation("Delete Confirmation", "This item will be deleted permanently, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setInt(1, itemID);
                prst.execute();
            }

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void updateItemWithSupplierAndPurchasePrice(Integer supplierID, Double price, String itemCode){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE items "
                + "SET supplierID = ?, "
                + "purchase_price = ? "
                + "WHERE code = '" + itemCode + "'";

        try{

            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierID);
            prst.setDouble(2, price);
            prst.executeUpdate();

        } catch(SQLException ex){
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    // CHECKING METHOD
    public boolean checkIfItemCodeExists(String code){ // check if item exist on insert

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT * FROM items WHERE code = ? ";

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, code);
            rs = prst.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch(SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
            return false;
        }

    }

    // GENERATING METHOD
    public String generateNextItemCode(String previousItemCode){

        String defaultItemCode = "0001";

        if(previousItemCode != null){

            Integer lastItemCodeAsNumber = Integer.valueOf(previousItemCode);
            Integer newItemCodeAsNumber = lastItemCodeAsNumber + 1;

            if(newItemCodeAsNumber < 10){
                return String.valueOf("000" + newItemCodeAsNumber);
            } else if(newItemCodeAsNumber >= 10 && newItemCodeAsNumber < 100){
                return String.valueOf("00" + newItemCodeAsNumber);
            } else if(newItemCodeAsNumber >= 100 && newItemCodeAsNumber < 1000){
                return String.valueOf("0" + newItemCodeAsNumber);
            } else if(newItemCodeAsNumber >= 1000){
                return String.valueOf(newItemCodeAsNumber);
            }

        } else {
            return defaultItemCode;
        }

        return defaultItemCode;

    }

    // SETTERS AND GETTERS
    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(double purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(double wholesale_price) {
        this.wholesale_price = wholesale_price;
    }

    public double getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(double retail_price) {
        this.retail_price = retail_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierBrandname() {
        return supplierBrandname;
    }

    public void setSupplierBrandname(String supplierBrandname) {
        this.supplierBrandname = supplierBrandname;
    }

    public Integer getVatCategorieID() {
        return vatCategorieID;
    }

    public void setVatCategorieID(Integer vatCategorieID) {
        this.vatCategorieID = vatCategorieID;
    }

    public Integer getMeasurementUnitID() {
        return measurementUnitID;
    }

    public void setMeasurementUnitID(Integer measurementUnitID) {
        this.measurementUnitID = measurementUnitID;
    }
}
