package objects;

import database.MySQLConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import specialAlerts.SpecialAlert;

import java.sql.*;
import java.util.Optional;

public class User {

    // we use Integer and not int because Integer can get null value
    // and we need null value to constructors, so we can let database to autoincrement in that id
    private Integer id;
    private String username;
    private String password;
    private String namesurname;
    SpecialAlert alert = new SpecialAlert();

    public User(){} //empty constructor

    public User(Integer id, String username, String password, String namesurname){ //constructor that gets four data

        this.id = id;
        this.username = username;
        this.password = password;
        this.namesurname = namesurname;

    }

    // CRUD METHODS
    public void addUser(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;

        String query = "INSERT INTO users (id, username, password, name_surname) VALUES (?, ?, ?, ?)";

        try {

            prst = conn.prepareStatement(query);
            prst.setInt(1, id);
            prst.setString(2, username);
            prst.setString(3, password);
            prst.setString(4, namesurname);
            prst.executeUpdate();

        } catch (SQLException ex) {
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void editUser(){

        Connection conn = MySQLConnection.connectToDB();
        PreparedStatement prst;
        String query = "UPDATE users "
                + "SET username = ?,"
                + "password = ?,"
                + "name_surname = ?"
                + "WHERE id = ?";

        try{
            Optional<ButtonType> result = alert.showConfirmation("Update Confirmation", "This user will be updated with the new data, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setString(1, username);
                prst.setString(2, password);
                prst.setString(3, namesurname);
                prst.setInt(4, id);
                prst.executeUpdate();
            }
        } catch(SQLException ex){
            alert.show("Error", "Error code: " + ex.getErrorCode() + "\nError message: " + ex.getMessage() + "\nSQL state is: " + ex.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void deleteUser(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement prst;

        try {
            Optional<ButtonType> result = alert.showConfirmation("Delete Confirmation", "This user will be deleted permanently, do you want to continue?");

            if(result.get() == ButtonType.OK){
                prst = conn.prepareStatement(query);
                prst.setInt(1, id);
                prst.execute();
            }

        } catch (SQLException ex) {
            alert.show("Warning", "This user has inserted data, can't be deleted!", Alert.AlertType.WARNING);
        }

    }

    // CHECKING METHODS
    public boolean checkSameUsername(){

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT * FROM users WHERE username = ? ";

        try{
            prst = conn.prepareStatement(query); //prepare the query
            prst.setString(1, username); //set to the first ? the username
            rs = prst.executeQuery(); //execute the query
            if(rs.next()){ // if the selection has row that means that the user with the inputed username it exists
                return true; // then return true
            } else {
                return false;
            }
        } catch(Exception ex) {
            alert.show("Error", "Error message: " + ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean checkSameUsernameOnUpdate(){

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        // select all users that has the same username expect the one that has the same id as the one we are updating
        String query = "SELECT * FROM users WHERE username = ? AND NOT id = ? ";

        try{
            prst = conn.prepareStatement(query); //prepare the query
            prst.setString(1, username); //set to the first ? the username
            prst.setInt(2, id); //set to the second ? the id
            rs = prst.executeQuery(); //execute the query
            if(rs.next()){ // if the selection has row that means that the user with the inputed username it exists
                return true; // then return false
            } else {
                return false;
            }
        } catch(Exception ex) {
            alert.show("Error", "Error message: " + ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }

    }

    public boolean checkUserLogin(){

        Connection conn = MySQLConnection.connectToDB(); //create connection
        ResultSet rs = null;
        PreparedStatement prst = null;

        String query = "SELECT * FROM users WHERE username = ? AND password = ? "; //store select query to a string

        try{
            prst = conn.prepareStatement(query); //prepare the query
            prst.setString(1, username); //set to the first ? the username
            prst.setString(2, password); //set to the second ? the password
            rs = prst.executeQuery(); //execute the query
            if(rs.next()){ //if the selection gives data that means the inputs are correct
                return true; //so return true
            } else {
                alert.show("Warning", "Wrong username or password", Alert.AlertType.WARNING);
                return false; //else return false
            }
        } catch(Exception ex) {
            alert.show("Error", "Error message: " + ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }

    }

    // SETTERS AND GETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamesurname() {
        return namesurname;
    }

    public void setNamesurname(String namesurname) {
        this.namesurname = namesurname;
    }
}
