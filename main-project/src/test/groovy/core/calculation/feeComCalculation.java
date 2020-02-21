package core.calculation;

import utils.ExecuteQuery;
import utils.Queries;

import java.sql.Connection;
import java.util.List;

public class feeComCalculation {
    private Connection db;

    public feeComCalculation(Connection db) {
        this.db = db;
    }

    public static List<String[]> NrQueryResult(Queries queries, String lastClearingTime, ExecuteQuery executeQuery, String processingCode){
        String NrNumbersQuery = queries.getFindNrNumbers(lastClearingTime, processingCode);
        List<String[]> NrQueryResult = executeQuery.executeSqlQuery(NrNumbersQuery, "NR-NUMBERS");
        return NrQueryResult;
    }
    public static List<String[]> feeComQueryResult(Queries queries, String nrNo, ExecuteQuery executeQuery){
        String feeComQuery = queries.getFeeCommissionQuery(nrNo);
        List<String[]> feeComQueryResult = executeQuery.executeSqlQuery(feeComQuery, "CALCULATED FEE-COMMISSION");
        return feeComQueryResult;
    }

    public static List<String[]> viewFeeComResult(Queries queries, String processingCode, ExecuteQuery executeQuery, String accessChannel, String userSegment){
        String viewfeeComQuery = queries.viewFeeCommissionQuery(processingCode, accessChannel, userSegment);
        String title = "CONFIGURED FEE-COMMISSION - " + accessChannel;
        List<String[]> viewFeeComResult = executeQuery.executeSqlQuery(viewfeeComQuery, title );
        return viewFeeComResult;
    }
    public static List<String[]> viewDynamicFeeComResult(String nrNo, Queries queries, String processingCode, ExecuteQuery executeQuery, String accessChannel){
        String accessChannelIdentifierQuery = queries.accessChannelIdentifierQuery(nrNo);
        List<String[]> channel = executeQuery.executeSqlQuery(accessChannelIdentifierQuery, "accessChannel" );
        accessChannel = channel.get(0)[0];
    System.out.println(accessChannel);
        if(accessChannel.equals("77")){
            accessChannel = "USSD";
        }
        else{
            accessChannel = "APP";
        }
        System.out.println(accessChannel);
        String viewfeeComQuery = queries.viewDynamicFeeCommissionQuery(processingCode, accessChannel, nrNo);
        String title = "CONFIGURED FEE-COMMISSION - " + accessChannel;
        List<String[]> viewFeeComResult = executeQuery.executeSqlQuery(viewfeeComQuery, title );
        return viewFeeComResult;
    }
    public static List<String[]> AccumulatedDailyClearingQueryResult(String feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery){
        String AccumulatedDailyClearingQuery = queries.p2pAccumulatedDailyClearingQuery(lastClearingTime, feeItems);
        String title = "ACCUMULATED-DAILY-CLR-LOG ";
        List<String[]> AccumulatedDailyClearingQueryResult = executeQuery.executeSqlQuery(AccumulatedDailyClearingQuery, title);

        return AccumulatedDailyClearingQueryResult;
    }
    public static List<String[]> AccumulatedSettlementQueryResult(String feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery) {
        String AccumulatedSettlementQueryQuery = queries.p2pAccumulatedSettlementQuery(lastClearingTime, feeItems);
        String title = "ACCUMULATED-SETTLEMENT-LOG ";
        List<String[]> AccumulatedSettlementQueryResult = executeQuery.executeSqlQuery(AccumulatedSettlementQueryQuery, title);

        return AccumulatedSettlementQueryResult;
    }
    public static List<String[]> CumulativeSettlementLogQueryResult(String feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery) {
        String CumulativeSettlementLogQuery = queries.p2pCumulativeSettlementLogQuery(lastClearingTime, feeItems);
        String title = "CUMULATIVE-SETTLEMENT-LOG ";
        List<String[]> CumulativeSettlementLogQueryResult = executeQuery.executeSqlQuery(CumulativeSettlementLogQuery, title);

        return CumulativeSettlementLogQueryResult;
    }
}
