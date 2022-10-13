package objects;

import database.MySQLConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import specialAlerts.SpecialAlert;

import java.sql.*;

public class CustomerInvoice {

    private Integer customerInvoiceID;
    private String insertionDate;
    private String insertionTime;
    private Integer customerID;
    private String customerBrandname;
    private Integer invoiceTypeID;
    private String invoiceTypeDescription;
    private String invoiceNumber;
    private Integer payingWayID;
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
    private Integer handwrited;
    private String remarks;
    SpecialAlert alert = new SpecialAlert();

    public CustomerInvoice(){}

    public CustomerInvoice(Integer customerInvoiceID, String insertionDate, String insertionTime, Integer customerID, String customerBrandname, Integer invoiceTypeID, String invoiceTypeDescription, String invoiceNumber, Integer payingWayID, String trackingPurpose, String fromPlace, String toPlace, String licensePlate, String vatRegimeDescription, String programUser, double initialValue, double discountPercentage, double discountValue, double valueNoVAT, double VAT, double quantity, double total, Integer handwrited, String remarks){

        this.customerInvoiceID = customerInvoiceID;
        this.insertionDate = insertionDate;
        this.insertionTime = insertionTime;
        this.customerID = customerID;
        this.customerBrandname = customerBrandname;
        this.invoiceTypeID = invoiceTypeID;
        this.invoiceTypeDescription = invoiceTypeDescription;
        this.invoiceNumber = invoiceNumber;
        this.payingWayID = payingWayID;
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
        this.handwrited = handwrited;
        this.remarks = remarks;

    }

    // CRUD METHODS
    public void addCustomerInvoice(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO customer_invoices (id, invoice_number, date, time, initial_value, discount_percent, discount_value, value_beforevat, vat_value, quantity, total, purpose_of_tracking, from_place, to_place, license_plate, customer_id, invoicetype_id, payingway_id, vatregime_id, user_id, handwrited_invoice, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM vat_regime WHERE description = ?), (SELECT id FROM users WHERE username = ?), ?, ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setInt(1, customerInvoiceID);
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
            prst.setInt(16, customerID);
            prst.setInt(17, invoiceTypeID);
            prst.setInt(18, payingWayID);
            prst.setString(19, vatRegimeDescription);
            prst.setString(20, programUser);
            prst.setInt(21, handwrited);
            prst.setString(22, remarks);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void cancelCustomerInvoice(){ //cancel customer invoice, it means to add another invoice in database that cancels that invoice

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO customer_invoices (id, invoice_number, date, time, initial_value, discount_percent, discount_value, value_beforevat, vat_value, quantity, total, purpose_of_tracking, from_place, to_place, license_plate, customer_id, invoicetype_id, payingway_id, vatregime_id, user_id, handwrited_invoice, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM vat_regime WHERE description = ?), (SELECT id FROM users WHERE username = ?), ?, ?)";

        try {
                prst = conn.prepareStatement(query);
                prst.setInt(1, customerInvoiceID);
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
                prst.setInt(16, customerID);
                prst.setInt(17, invoiceTypeID);
                prst.setInt(18, payingWayID);
                prst.setString(19, vatRegimeDescription);
                prst.setString(20, programUser);
                prst.setInt(21, handwrited);
                prst.setString(22, remarks);
                prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void updateRemarkOnInvoice(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE customer_invoices " +
                        "SET remarks = 'CANCELED' " + // add CANCELED to every canceled invoice in their remarks
                        "WHERE id = ? ";

        try{

                prst = conn.prepareStatement(query);
                prst.setInt(1, customerInvoiceID);
                prst.executeUpdate();

        } catch(SQLException ex){
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void deleteCustomerInvoice(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM customer_invoices WHERE id = ?";
        PreparedStatement prst;

        try {
            prst = conn.prepareStatement(query);
            prst.setInt(1, customerInvoiceID);
            prst.execute();
        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    // GENERATING METHODS
    public String generateInvoiceNumber(String invoiceNumber){

        String defaultInvoiceNumber = "00000001";

        if(invoiceNumber != null){

                    Integer invoiceNumberAsInteger = Integer.valueOf(invoiceNumber);
                    Integer newinvoiceNumberAsInteger = invoiceNumberAsInteger + 1;

                    if(newinvoiceNumberAsInteger < 10){
                        return String.valueOf("0000000" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 10 && newinvoiceNumberAsInteger < 100){
                        return String.valueOf("000000" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 100 && newinvoiceNumberAsInteger < 1000){
                        return String.valueOf("00000" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 1000 && newinvoiceNumberAsInteger < 10000){
                        return String.valueOf("0000" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 10000 && newinvoiceNumberAsInteger < 100000){
                        return String.valueOf("000" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 100000 && newinvoiceNumberAsInteger < 1000000){
                        return String.valueOf("00" + newinvoiceNumberAsInteger);
                    } else if(newinvoiceNumberAsInteger >= 1000000 && newinvoiceNumberAsInteger < 10000000){
                        return String.valueOf("0" + newinvoiceNumberAsInteger);
                    } else if((newinvoiceNumberAsInteger >= 10000000)){
                        return String.valueOf(newinvoiceNumberAsInteger);
            }

        } else {
            return defaultInvoiceNumber;
        }

        return defaultInvoiceNumber;

    }

    public void updtadeCounting(String invoiceType_id){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO invoicenumbering_" + invoiceType_id + " (number) VALUES ( ? )";

        String nullInsert = null; // we need to insrt null so we can let autoincrement work by it self

        try {

            prst = conn.prepareStatement(query);
            prst.setString(1, nullInsert);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);

        }

    }

    // CHECKING METHODS
    public boolean checkIfSelectedInvoiceTypeHasDelivery(Integer deliveryTypeID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT delivery FROM type_of_customer_document WHERE id = " + deliveryTypeID;
        Statement st;
        ResultSet rs;

        int delivery = 0;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                delivery = rs.getInt("delivery"); // get delivery number
            }

            if(delivery == 1){ // if its equal to 1 that means tha has delivery
                return true; // so return true
            } else{
                return false; // if its not 1 return false
            }

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

        return false;

    }

    public boolean checkIfSelectedCustomerHasTaxCode(Integer customerID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT taxcode FROM customer WHERE customer_id = " + customerID;
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

    public boolean checkBeforeInsertion(String documentType, Integer invoiceTypeID, String licensePlate, Integer customerID){

            //First check if customer documet type is invoice or receipt
            if (documentType.contains("INVOICE")) { // check if the description of the document contains the word INVOICE, that means the document is an invoice

                if (checkIfSelectedCustomerHasTaxCode(customerID)) { // When document is Invoice we have to check if the inserted customer has taxcode

                    //Then check if selected invoice has delivery
                    if (checkIfSelectedInvoiceTypeHasDelivery(invoiceTypeID)) {

                        //then make sure that there is inserted license plate
                        if (licensePlate.equals("")) { // if its empty display an alert
                            alert.show("Warning", "Add a license plate to Invoice Elements!", Alert.AlertType.WARNING);
                            return false;
                        } else { // else return true, that means that it passed all the checks and is ready for insertion

                            return true;

                        }

                    } else { //from the moment that the invoice does not have delivery we are ready for insertion without checking the license plate

                        return true; // so return true

                    }

                } else { //if selected customer does not have a taxcode, alert the user and return false
                    alert.show("Warning", "This customer does not have a taxcode!", Alert.AlertType.WARNING);
                    return false;
                }

            } else if (documentType.contains("RECEIPT")) { // now we check if the document is a Receipt

                //Then check if selected invoice has delivery
                if (checkIfSelectedInvoiceTypeHasDelivery(invoiceTypeID)) {

                    //then make sure that there is inserted license plate
                    if (licensePlate.equals("")) { // if its empty display an alert
                        alert.show("Warning", "Add a license plate to Invoice Elements!", Alert.AlertType.WARNING);
                        return false;
                    } else { // else return true for receipt

                        return true;

                    }
                } else { //from the moment that the receipt does not have delivery we are ready for insertion without checking the license plate

                    return true;

                }

            }

        return false; // by default return false

    }

    //CALCULATION METHODS
    public double sumOfQuantities(ObservableList<ItemTransaction> e){

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

    public double sumOfInitialValueRetail(ObservableList<ItemTransaction> e){

        double initialValue = 0.0;

        for (ItemTransaction items : e) {
            initialValue += items.getTotal() / ((items.getVat() / 100) + 1); //initial value is found this way: TotalValue / 1.(vatValue of item)
        }

        return initialValue;

    }

    public double sumTotalForRetail(ObservableList<ItemTransaction> e){

        double sumTotal = 0.0;

        for (ItemTransaction items : e) {
            sumTotal += items.getTotal();
        }

        return sumTotal;
    }

    // SETTERS AND GETTERS
    public Integer getCustomerInvoiceID() {
        return customerInvoiceID;
    }

    public void setCustomerInvoiceID(Integer customerInvoiceID) {
        this.customerInvoiceID = customerInvoiceID;
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

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(Integer invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getPayingWayID() {
        return payingWayID;
    }

    public void setPayingWayID(Integer payingWayID) {
        this.payingWayID = payingWayID;
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

    public Integer getHandwrited() {
        return handwrited;
    }

    public void setHandwrited(Integer handwrited) {
        this.handwrited = handwrited;
    }

    public String getCustomerBrandname() {
        return customerBrandname;
    }

    public void setCustomerBrandname(String customerBrandname) {
        this.customerBrandname = customerBrandname;
    }

    public String getInvoiceTypeDescription() {
        return invoiceTypeDescription;
    }

    public void setInvoiceTypeDescription(String invoiceTypeDescription) {
        this.invoiceTypeDescription = invoiceTypeDescription;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
