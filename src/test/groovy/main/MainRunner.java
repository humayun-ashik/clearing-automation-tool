package main;
import connections.DBConnection;
import connections.runClearing;
import utils.*;

import clearing.items.*;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;


public class MainRunner {
    LocalDateTime dateTime = LocalDateTime.now();       //Using Date class
    public static Date date = new Date();
    public static DateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS a");
    public static String currentTime = sdf.format(date);

    public static void main(String[] args) throws InterruptedException {
        DBConnection dbConnection = new DBConnection();
        Connection db = dbConnection.setUpConnection();

        Queries queries = new Queries();
        ExecuteQuery executeQuery = new ExecuteQuery(db);
//
        runClearing runclr = new runClearing();
//        runclr.executeFile("/home/testScriptForClearing/prepareClearingFile.sh");

        System.out.println("========================================================================================");
        System.out.println("Clearing Items:");
        System.out.println("========================================================================================");
        System.out.println("ITEM 1. [Customer --> P2P]\t\tITEM 2. [Customer --> Cash-In]\nITEM 3. [Customer --> Cash-Out]\t\tITEM 4. [Customer --> BillPay]\nITEM 5. [Agent --> BillPay]\t\tOther keys. [All items]");
        System.out.println("========================================================================================");
        // take input from user//

        Scanner in = new Scanner(System.in);
        System.out.print("Please enter your desired items: ");
        String msg = in.nextLine();
        int choice = Integer.valueOf(msg);
        switch (choice){
            case 1:
                try {
                    P2P p2p = new P2P(db);
                    p2p.RunClearing();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    CashIn cashIn = new CashIn(db);
                    cashIn.RunClearing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    CashOut cashOut = new CashOut(db);
                    cashOut.RunClearing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    CustomerBillPay cubillpay = new CustomerBillPay(db);
                    cubillpay.RunClearing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    AgentBillPay agbillpay = new AgentBillPay(db);
                    agbillpay.RunClearing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                try{
                P2P p2p = new P2P(db);
                p2p.RunClearing();
                CashIn cashIn = new CashIn(db);
                cashIn.RunClearing();
                CashOut cashOut = new CashOut(db);
                cashOut.RunClearing();
                }catch (Exception e) {
                    e.printStackTrace();
                }


        }

        dbConnection.closeConnections(db);
    }
}
