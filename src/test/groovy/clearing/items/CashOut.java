package clearing.items;

import main.MainRunner;
import utils.CommonMethods;
import utils.Constants;
import utils.ExecuteQuery;
import utils.Queries;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class CashOut {
        String lastClearingTime = MainRunner.currentTime;
        String fileName = "resources/cashOut_clearingOutput.log";
        List<String[]> NrQueryResult = null;
        List<String[]> feeComQueryResult = null;
        List<Double> accumulation = new ArrayList<>(11);
        List<String[]> viewFeeComResult = null;
        List<String[]> AccumulatedDailyClearingQueryResult = null;
        List<String[]> AccumulatedSettlementQueryResult = null;
        List<String[]> CumulativeSettlementLogQueryResult = null;
        List<String> feeItems = null;
        private Connection db;

        public CashOut(Connection db) {
            this.db = db;
        }

        int nCol = 0;
        Queries queries = new Queries();
        public void RunClearing() throws IOException {
            ExecuteQuery executeQuery = new ExecuteQuery(db);
//            System.out.println("Result has been written in " + fileName + " file");
//            CommonMethods commonMethods = new CommonMethods(fileName);
            NrQueryResult = CommonMethods.getNrQueryResult(queries, lastClearingTime, Constants.cashOut_ProcessingCode, executeQuery);
            CommonMethods.getViewFeeComResult(queries, Constants.cashOut_ProcessingCode, executeQuery,  "USSD", Constants.generalCustomer_userSegment);
            CommonMethods.getViewFeeComResult(queries, Constants.cashOut_ProcessingCode, executeQuery,  "APP", Constants.generalCustomer_userSegment);
            feeItems = CommonMethods.getFeeComQueryResult(queries, NrQueryResult, executeQuery);

            CommonMethods.getAccumulatedDailyClearingQuery(feeItems, queries, lastClearingTime, executeQuery);
            CommonMethods.getAccumulatedSettlementQuery(feeItems, queries, lastClearingTime, executeQuery);
            CommonMethods.getCumulativeSettlementLogQuery(feeItems, queries, lastClearingTime, executeQuery);

        }

    }


