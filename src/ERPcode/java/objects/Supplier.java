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

public class Supplier {

    private Integer supplierID;
    private String brandname;
    private String profession;
    private String taxcode;
    private String address;
    private String area;
    private String zipcode;
    private String DOY;
    private String surname;
    private String name;
    private String phone1;
    private String phone2;
    private String faxnumber;
    private String email;
    private String website;
    private String notes;
    private String vatregimeID;
    private String payingwayID;
    private String pricelistID;
    private String userID;
    private Integer checkTaxcode;
    private Integer checkDoy;
    SpecialAlert alert = new SpecialAlert();

    public Supplier(){}

    public Supplier(Integer supplierID, String brandname, String profession, String taxcode, String address, String area, String zipcode, String DOY, String surname, String name, String phone1, String phone2, String faxnumber, String email, String website, String notes, String vatregimeID, String payingwayID, String pricelistID, String userID, Integer checkTaxcode, Integer checkDoy){

        this.supplierID = supplierID;
        this.brandname = brandname;
        this.profession = profession;
        this.taxcode = taxcode;
        this.address = address;
        this.area = area;
        this.zipcode = zipcode;
        this.DOY = DOY;
        this.surname = surname;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.faxnumber = faxnumber;
        this.email = email;
        this.website = website;
        this.notes = notes;
        this.vatregimeID = vatregimeID;
        this.payingwayID = payingwayID;
        this.pricelistID = pricelistID;
        this.userID = userID;
        this.checkTaxcode = checkTaxcode;
        this.checkDoy = checkDoy;

    }

    // CRUD METHODS
    public void addSupplier(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO supplier (supplier_id, brandname, profession, taxcode, address, area, zipcode, DOY, surname, name, phone1, phone2, faxnumber, email, website, notes, vatregime_id, payingway_id, pricelist_id, user_id, check_taxcode, check_doy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?), ?, ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setInt(1, supplierID);
            prst.setString(2, brandname);
            prst.setString(3, profession);
            prst.setString(4, taxcode);
            prst.setString(5, address);
            prst.setString(6, area);
            prst.setString(7, zipcode);
            prst.setString(8, DOY);
            prst.setString(9, surname);
            prst.setString(10, name);
            prst.setString(11, phone1);
            prst.setString(12, phone2);
            prst.setString(13, faxnumber);
            prst.setString(14, email);
            prst.setString(15, website);
            prst.setString(16, notes);
            prst.setString(17, vatregimeID);
            prst.setString(18, payingwayID);
            prst.setString(19, pricelistID);
            prst.setString(20, userID);
            prst.setInt(21, checkTaxcode);
            prst.setInt(22, checkDoy);
            prst.executeUpdate();

        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    public void editSupplier(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE supplier "
                + "SET brandname = ?,"
                + "profession = ?,"
                + "taxcode = ?,"
                + "address = ?,"
                + "area = ?,"
                + "zipcode = ?,"
                + "DOY = ?,"
                + "surname = ?,"
                + "name = ?,"
                + "phone1 = ?,"
                + "phone2 = ?,"
                + "faxnumber = ?,"
                + "email = ?,"
                + "website = ?,"
                + "notes = ?,"
                + "vatregime_id = ?,"
                + "payingway_id = ?,"
                + "pricelist_id = ?,"
                + "user_id = (SELECT id FROM users WHERE username = ?),"
                + "check_taxcode = ?,"
                + "check_doy = ? "
                + "WHERE supplier_id = ?";

        try{
            Optional<ButtonType> result = alert.showConfirmation("Update Confirmation", "This supplier will be updated with the new data, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setString(1, brandname);
                prst.setString(2, profession);
                prst.setString(3, taxcode);
                prst.setString(4, address);
                prst.setString(5, area);
                prst.setString(6, zipcode);
                prst.setString(7, DOY);
                prst.setString(8, surname);
                prst.setString(9, name);
                prst.setString(10, phone1);
                prst.setString(11, phone2);
                prst.setString(12, faxnumber);
                prst.setString(13, email);
                prst.setString(14, website);
                prst.setString(15, notes);
                prst.setString(16, vatregimeID);
                prst.setString(17, payingwayID);
                prst.setString(18, pricelistID);
                prst.setString(19, userID);
                prst.setInt(20, checkTaxcode);
                prst.setInt(21, checkDoy);
                prst.setInt(22, supplierID);
                prst.executeUpdate();
            }
        } catch(SQLException ex){
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }

    }

    public void deleteSupplier(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM supplier WHERE supplier_id = ?";
        PreparedStatement prst;

        try {
            Optional<ButtonType> result = alert.showConfirmation("Delete Confirmation", "This supplier will be deleted permanently, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setInt(1, supplierID);
                prst.execute();
            }

        } catch (SQLException ex) {
            alert.show("Error", "Unkown error occured!", Alert.AlertType.ERROR);
        }

    }

    // CHECKING METHODS
    public boolean checkIfSupplierBrandnameExists(){ // check supplier's brandname on insert

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT * FROM supplier WHERE brandname = ? ";

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, brandname);
            rs = prst.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean checkIfSupplierTaxcodeExists(){ // check supplier's taxcode on insert

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT * FROM supplier WHERE taxcode = ? ";

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, taxcode);
            rs = prst.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean checkIfSupplierTaxcodeExistsOnUpdate(){ // check supplier's taxcode on update

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        // we need to select supplier with the inserted taxcode but not the one that has the same ID as the one we are allready editing
        String query = "SELECT * FROM supplier WHERE taxcode = ? AND NOT supplier_id = ?";

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, taxcode);
            prst.setInt(2, supplierID);
            rs = prst.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean checkIfSupplierBrandnameExistsOnUpdate(){ // check supplier's brandname on update

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        // we need to select supplier with the inserted brandname but not the one that has the same ID as the one we are allready editing
        String query = "SELECT * FROM supplier WHERE brandname = ? AND NOT supplier_id = ?"; //store select query to a string

        try{
            prst = conn.prepareStatement(query);
            prst.setString(1, brandname);
            prst.setInt(2, supplierID);
            rs = prst.executeQuery();
            if(rs.next()){
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
            return false;
        }

    }

    // SETTERS AND GETTERS
    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDOY() {
        return DOY;
    }

    public void setDOY(String DOY) {
        this.DOY = DOY;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFaxnumber() {
        return faxnumber;
    }

    public void setFaxnumber(String faxnumber) {
        this.faxnumber = faxnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVatregimeID() {
        return vatregimeID;
    }

    public void setVatregimeID(String vatregimeID) {
        this.vatregimeID = vatregimeID;
    }

    public String getPayingwayID() {
        return payingwayID;
    }

    public void setPayingwayID(String payingwayID) {
        this.payingwayID = payingwayID;
    }

    public String getPricelistID() {
        return pricelistID;
    }

    public void setPricelistID(String pricelistID) {
        this.pricelistID = pricelistID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
