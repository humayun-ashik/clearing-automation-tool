package connections;//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

    private String hostName;
    private String port;
    private String sid;
    private String dbUserName;
    private String dbPassword;
//    private static Logger logger = LoggerFactory.getLogger(DBConnection.class);

    public DBConnection() {
        this.hostName = Constants.QADB;
        this.port = Constants.QADB_port;
        this.sid = Constants.QADB_sid;
        this.dbPassword = Constants.QADB_Password;
        this.dbUserName = Constants.QADB_Username;

    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public String getSid() {
        return sid;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Connection setUpConnection() throws InterruptedException {
        final String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        final String hostName = getHostName();
        final String port = getPort();
        final String sid = getSid();
        final String dbUserName = getDbUserName();
        final String dbPassword = getDbPassword();
        final String dbURL = "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + sid;

        Connection connection = null;

        try {
            Class.forName(jdbcDriver);
//            logger.info("Starting connection to database...");
            System.out.println("Starting connection to database...");;
            connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
            //System.out.println("Connectioon" + connection.getClientInfo());
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException occurred : " + ex.getMessage() + "\nStack trace for the exception: " + ex.getStackTrace());
        } catch (SQLException ex) {
            System.out.println("SQLException occurred : " + ex.getMessage() + "\nStack trace for the exception: " + ex.getStackTrace());
        } catch (Exception ex) {
            System.out.println("Exception occurred : " + ex.getMessage() + "\nStack trace for the exception: " + ex.getStackTrace());
        }

        return connection;
    }

    public void closeConnections(Connection connection, ResultSet resultSet) {
        try {
//            logger.info("Closing connection to database...");
            resultSet.close();
            connection.close();
        } catch (SQLException ex) {
//            logger.error("Exception occurred : " + ex.getMessage() + "\nStack trace for the exception: " + ex.getStackTrace());
        }
    }

    public void closeConnections(Connection connection) {
        try {
//            logger.info("Closing connection to database...");
            connection.close();
        } catch (SQLException ex) {
//            logger.error("Exception occurred : " + ex.getMessage() + "\nStack trace for the exception: " + ex.getStackTrace());
        }
    }
}