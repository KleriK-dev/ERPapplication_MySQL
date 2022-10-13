package database;

import javafx.scene.control.Alert;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements DBInfo{

    static Connection conn = null;
    static SpecialAlert alert = new SpecialAlert();

    public static Connection connectToDB(){

        try{
            conn = DriverManager.getConnection(DB_NAME_WITH_ENCODING, USER, PASSWORD);
            return conn;
        } catch(SQLException ex){
            alert.show("Error", "Could not connect to MySQL database!", Alert.AlertType.ERROR);
            return null;
        }

    }

}


