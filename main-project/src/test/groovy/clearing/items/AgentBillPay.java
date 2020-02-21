package clearing.items;
//import utils.ExcelFileReader;

import main.MainRunner;
import utils.CommonMethods;
import utils.Constants;
import utils.ExecuteQuery;
import utils.Queries;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AgentBillPay {
    String lastClearingTime = MainRunner.currentTime;
    String fileName = "resources/cuBillPay_clearingOutput.log";
    List<String[]> NrQueryResult = null;
    List<String[]> feeComQueryResult = null;
    List<Double> accumulation = new ArrayList<>(11);
    List<String[]> viewFeeComResult = null;
    List<String[]> AccumulatedDailyClearingQueryResult = null;
    List<String[]> AccumulatedSettlementQueryResult = null;
    List<String[]> CumulativeSettlementLogQueryResult = null;
    List<String> feeItems = null;
    private Connection db;

    public AgentBillPay(Connection db) {
        this.db = db;
    }

    int nCol = 0;
    Queries queries = new Queries();
    public void RunClearing() throws IOException {
        ExecuteQuery executeQuery = new ExecuteQuery(db);
//            System.out.println("Result has been written in " + fileName + " file");
//            CommonMethods commonMethods = new CommonMethods(fileName);
        NrQueryResult = CommonMethods.getNrQueryResult(queries, lastClearingTime, Constants.AgBillPay_ProcessingCode, executeQuery);

        feeItems = CommonMethods.getAndViewFeeComQueryResult(queries, NrQueryResult, executeQuery, Constants.AgBillPay_ProcessingCode, "USSD");
        CommonMethods.getAccumulatedDailyClearingQuery(feeItems, queries, lastClearingTime, executeQuery);
        CommonMethods.getAccumulatedSettlementQuery(feeItems, queries, lastClearingTime, executeQuery);
        CommonMethods.getCumulativeSettlementLogQuery(feeItems, queries, lastClearingTime, executeQuery);

    }
}
