package objects;

import database.MySQLConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import specialAlerts.SpecialAlert;

import java.sql.*;
import java.util.Optional;

public class SupplierInvoice {

    private Integer supplierInvoiceID;
    private String insertionDate;
    private String insertionTime;
    private Integer supplierID;
    private String supplierBrandname;
    private Integer invoiceTypeID;
    private String invoiceTypeDescription;
    private String invoiceNumber;
    private Integer paymentID;
    private String trackingPurpose;
    private String fromPlace;
    private String toPlace;
    private String licensePlate;
    private String vatRegimeDescription;
    private String programUser;
    private double initialValue;
    private double discountPercentage;
    private double discountValue;
    private double valueNoVAT;
    private double VAT;
    private double quantity;
    private double total;
    private String remarks;
    SpecialAlert alert = new SpecialAlert();
    
    //Constructors
    public SupplierInvoice(){}

    public SupplierInvoice(Integer supplierInvoiceID, String insertionDate, String insertionTime, Integer supplierID, String supplierBrandname, Integer invoiceTypeID, String invoiceTypeDescription, String invoiceNumber, Integer paymentID, String trackingPurpose, String fromPlace, String toPlace, String licensePlate, String vatRegimeDescription, String programUser, double initialValue, double discountPercentage, double discountValue, double valueNoVAT, double VAT, double quantity, double total, String remarks) {
        this.supplierInvoiceID = supplierInvoiceID;
        this.insertionDate = insertionDate;
        this.insertionTime = insertionTime;
        this.supplierID = supplierID;
        this.supplierBrandname = supplierBrandname;
        this.invoiceTypeID = invoiceTypeID;
        this.invoiceTypeDescription = invoiceTypeDescription;
        this.invoiceNumber = invoiceNumber;
        this.paymentID = paymentID;
        this.trackingPurpose = trackingPurpose;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.licensePlate = licensePlate;
        this.vatRegimeDescription = vatRegimeDescription;
        this.programUser = programUser;
        this.initialValue = initialValue;
        this.discountPercentage = discountPercentage;
        this.discountValue = discountValue;
        this.valueNoVAT = valueNoVAT;
        this.VAT = VAT;
        this.quantity = quantity;
        this.total = total;
        this.remarks = remarks;
    }
    
    //CRUD methods
    public void addSupplierInvoice(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO supplier_invoices (id, invoice_number, date, time, initial_value, discount_percent, discount_value, value_beforevat, vat_value, quantity, total, purpose_of_tracking, from_place, to_place, license_plate, supplier_id, invoicetype_id, payment_id, vatregime_id, user_id, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM vat_regime WHERE description = ?), (SELECT id FROM users WHERE username = ?), ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierInvoiceID);
            prst.setString(2, invoiceNumber);
            prst.setString(3, insertionDate);
            prst.setString(4, insertionTime);
            prst.setDouble(5, initialValue);
            prst.setDouble(6, discountPercentage);
            prst.setDouble(7, discountValue);
            prst.setDouble(8, valueNoVAT);
            prst.setDouble(9, VAT);
            prst.setDouble(10, quantity);
            prst.setDouble(11, total);
            prst.setString(12, trackingPurpose);
            prst.setString(13, fromPlace);
            prst.setString(14, toPlace);
            prst.setString(15, licensePlate);
            prst.setInt(16, supplierID);
            prst.setInt(17, invoiceTypeID);
            prst.setInt(18, paymentID);
            prst.setString(19, vatRegimeDescription);
            prst.setString(20, programUser);
            prst.setString(21, remarks);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void deleteSupplierInvoice(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM supplier_invoices WHERE id = ?";
        PreparedStatement prst;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierInvoiceID);
            prst.execute();
        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public boolean checkIfSelectedSupplierHasTaxCode(Integer supplierID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT taxcode FROM supplier WHERE supplier_id = " + supplierID;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                if(rs.getString("taxcode").equals("")){ //if returned taxcode is empty
                    return false; // return false
                } else if (rs.getString("taxcode").length() == 9){ // check if returned taxcode length is equal to 9
                    return true; // return true
                }

            }

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        return false;

    }

    //CALCULATION METHODS
    public double sumOfQuantities(ObservableList<ItemTransaction> e) {

        double sumQuantities = 0.0;

        for (ItemTransaction items : e) {
            sumQuantities += items.getQuantity();
        }

        return sumQuantities;
    }

    public double sumOfInitialValueWholesale(ObservableList<ItemTransaction> e){

        double sumItemTotals = 0.0;

        for (ItemTransaction items : e) {
            sumItemTotals += items.getTotal();
        }

        return sumItemTotals;
    }

    public double sumTotalForWholesale(ObservableList<ItemTransaction> e){

        double sumTotal = 0.0;
        double s = 0.0;

        for (ItemTransaction items : e) {
            s = 100 + items.getVat();
            sumTotal += (s * items.getTotal()) / 100;
        }

        return sumTotal;
    }


    //Setters and Getters
    public Integer getSupplierInvoiceID() {
        return supplierInvoiceID;
    }

    public void setSupplierInvoiceID(Integer supplierInvoiceID) {
        this.supplierInvoiceID = supplierInvoiceID;
    }

    public String getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(String insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(String insertionTime) {
        this.insertionTime = insertionTime;
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

    public Integer getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(Integer invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceTypeDescription() {
        return invoiceTypeDescription;
    }

    public void setInvoiceTypeDescription(String invoiceTypeDescription) {
        this.invoiceTypeDescription = invoiceTypeDescription;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public String getTrackingPurpose() {
        return trackingPurpose;
    }

    public void setTrackingPurpose(String trackingPurpose) {
        this.trackingPurpose = trackingPurpose;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVatRegimeDescription() {
        return vatRegimeDescription;
    }

    public void setVatRegimeDescription(String vatRegimeDescription) {
        this.vatRegimeDescription = vatRegimeDescription;
    }

    public String getProgramUser() {
        return programUser;
    }

    public void setProgramUser(String programUser) {
        this.programUser = programUser;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getValueNoVAT() {
        return valueNoVAT;
    }

    public void setValueNoVAT(double valueNoVAT) {
        this.valueNoVAT = valueNoVAT;
    }

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
