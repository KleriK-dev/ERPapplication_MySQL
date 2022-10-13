package database;

public interface DBInfo {
    String SERVER_NAME = "localhost";
    String DB_NAME = "erp_system";
    String DB_PORT = "3306";
    String DB_NAME_WITH_ENCODING = "jdbc:mysql://" +
            SERVER_NAME + "/" + DB_NAME + "?useUnicode=yes&characterEncoding=UTF-8";
    String USER = "root";
    String PASSWORD = "";
}


