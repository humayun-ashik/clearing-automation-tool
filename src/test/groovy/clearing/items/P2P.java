package clearing.items;

import main.MainRunner;
import utils.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class P2P {
    String lastClearingTime = MainRunner.currentTime;
    String fileName = "resources/p2p_clearingOutput.log";
    List<String[]> NrQueryResult = null;
    List<String[]> feeComQueryResult = null;
    List<Double> accumulation = new ArrayList<>(11);
    List<String[]> viewFeeComResult = null;
    List<String[]> AccumulatedDailyClearingQueryResult = null;
    List<String[]> AccumulatedSettlementQueryResult = null;
    List<String[]> CumulativeSettlementLogQueryResult = null;
    List<String> feeItems = null;
    private Connection db;

    public P2P(Connection db) {
        this.db = db;
    }

    int nCol = 0;
    Queries queries = new Queries();

    public void RunClearing() throws IOException {
        ExecuteQuery executeQuery = new ExecuteQuery(db);
        System.out.println("Result has been written in " + fileName + " file");
//        CommonMethods commonMethods = new CommonMethods(fileName);
//        CommonMethods.writeToTextFile(fileName);
        NrQueryResult = CommonMethods.getNrQueryResult(queries, lastClearingTime, Constants.p2p_ProcessingCode, executeQuery);
        CommonMethods.getViewFeeComResult(queries, Constants.p2p_ProcessingCode, executeQuery,  "USSD", Constants.generalCustomer_userSegment);
        CommonMethods.getViewFeeComResult(queries, Constants.p2p_ProcessingCode, executeQuery,  "ALL", Constants.generalCustomer_userSegment);
        feeItems = CommonMethods.getFeeComQueryResult(queries, NrQueryResult, executeQuery);

        CommonMethods.getAccumulatedDailyClearingQuery(feeItems, queries, lastClearingTime, executeQuery);
        CommonMethods.getAccumulatedSettlementQuery(feeItems, queries, lastClearingTime, executeQuery);
        CommonMethods.getCumulativeSettlementLogQuery(feeItems, queries, lastClearingTime, executeQuery);

//        System.gc();
//        System.runFinalization();
//        CommonMethods.finalize(fileName);
//        System.out.println("Print sample");
//        commonMethodsNew.finalize(fileName);

    }


}
