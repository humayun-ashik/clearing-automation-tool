package utils;//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ExecuteQuery {

    private Connection connection;
    public static int nCol;

    public ExecuteQuery(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public List<String[]> executeSqlQuery(String sqlQuery, String title) {

        ResultSet resultSet = null;
        List<String[]> table = new ArrayList<>();
        try {
//            System.out.println("==========================================================================================================================================================================================================================================================================================================================================================\n");
            System.out.println("[QUERY] :> " + sqlQuery);
            System.out.println("\n====================================================================================================================================  " + title + "  =========================================================================================================================================================================================");

            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery();
            nCol = resultSet.getMetaData().getColumnCount();
            // Print Column name
            ResultSetMetaData columns = resultSet.getMetaData();
            System.out.println("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------");
            for (int i = 1; i <= nCol; i++) {
                if (i == 3) {
                    System.out.print(format(" %-35s ", columns.getColumnName(i)));
                } else {
                    System.out.print(format(" %-30s ", columns.getColumnName(i)));
                }
            }
            System.out.println("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------");

            while (resultSet.next()) {
                String[] row = new String[nCol];
                for (int iCol = 1; iCol <= nCol; iCol++) {
                    Object obj = resultSet.getObject(iCol);
                    row[iCol - 1] = (obj == null) ? null : obj.toString();
                }
                table.add(row);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return table;
    }
}

//    //https://stackoverflow.com/questions/10768170/how-do-i-declare-a-2d-string-arraylist
//    public List< List<String>> getQueryResult(Connection db, String query){
//        try {
//            ExecuteQuery executeQuery = new ExecuteQuery(query, db);
//            ResultSet resultSet = executeQuery.executeSqlQuery();
//            while (resultSet.next()) {
//                String approval_date_time = resultSet.getString("approval_date_time");
//                System.out.println("approval_date_time " + approval_date_time);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }



/*
*     ExecuteQuery executeQuery = new ExecuteQuery(sqlQuery, db);
    ResultSet resultSet = executeQuery.executeSqlQuery();
    //System.out.printf("resultSet %d%n", resultSet);
        try

    {
        //int rowCount = resultSet.last() ? resultSet.getRow() : 0;
        int rowCount = -1;
        while (resultSet.next()) {
//                rowCount = resultSet.getInt("rowCount");
            String Player_id = resultSet.getString("PLAYER_ID");
            System.out.println(Player_id);
//                System.out.println(pid);
        }
        //System.out.println("Count : " + resultSet.getRow());
//            System.out.println("Count : " + rowCount);

    } catch(SQLException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
* */