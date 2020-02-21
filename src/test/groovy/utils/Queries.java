package utils;

import java.util.List;

public class Queries {


    public String getFindNrNumbers(String lastClearingTime, String processing_code) {
        String nrNumbers = "select distinct nr_number\n" +
                "from CLR.ias_daily_tx_log \n" +
                "where created > '"+
                "05-JAN-20 07.03.11.627000000 AM"
//                lastClearingTime
                +"' and processing_code = '"
                + processing_code
                + "'";
        return nrNumbers;
    }
    public String getFeeCommissionQuery(String nrNumber) {
        String feecomQuery = "select nr_number,tx_amount, fee_item_name, fee_rate, calculated_fee_amt,fee_or_commission,\n" +
        "fee_rate_type, from_role_cd, to_role_cd, from_player_id, to_player_id\n" +
        " from CLR.calculated_fee_commission\n" +
        "where NR_NUMBER in( '" + nrNumber+ "')";

        return feecomQuery;
    }
    public String viewFeeCommissionQuery(String processing_code, String accessChannel, String userSegment) {
        String viewfeecomQuery =         "select \n" +
                "player_id, access_channel, item_name\n" +
                "fee_or_commision, fee_range_min, fee_range_max,\n" +
                "fee_rate_type, fee_rate, fee_value_min, fee_value_max \n" +
                "from clr.get_fee_info\n" +
                "where fr_effective_date in\n" +
                "(select \n" +
                "max(fr_effective_date) as fr_effective_date\n" +
                "from clr.get_fee_info\n" +
                "where processing_cd = '" + processing_code +"'\n" +
                "and access_channel = '" +accessChannel+"'"+
                "and user_segment = '" + userSegment + "'" +
                " group by item_name)\n" +
                "and processing_cd = '" + processing_code +"'\n" +
                "and access_channel = '" +accessChannel+"'"+
                "and user_segment = '" + userSegment + "'";

        return viewfeecomQuery;
    }
    public String viewDynamicFeeCommissionQuery(String processing_code, String accessChannel, String nrNo) {
        String viewDynamicfeecomQuery = "select concat('0',player_id), access_channel, item_name, fee_or_commision, fee_range_min, fee_range_max, fee_rate_type, fee_rate, fee_value_min, fee_value_max \n" +
                "from clr.get_fee_info \n" +
                "where processing_cd = '" + processing_code +
                "' and player_id in (select distinct substr(acc_id1,2) \n" +
                "from CLR.calculated_fee_commission\n" +
                "where NR_NUMBER in( '" + nrNo + "')\n"+
                "and acc_id1 is not null)\n" +
                "and (fee_range_min <= (select distinct tx_amount as amount from clr.calculated_fee_commission\n" +
                "where NR_NUMBER in( '" + nrNo + "')"+
                ")\n and fee_range_max > (select distinct tx_amount as amount from clr.calculated_fee_commission\n" +
                "where NR_NUMBER in( '" + nrNo + "'))"+
                ")\n"
                +"and access_channel = '" +accessChannel+"'";

        return viewDynamicfeecomQuery;
    }
    public String p2pAccumulatedDailyClearingQuery(String lastClearingTime, String feeItemList) {


        String p2pAccumulatedDailyClearingQuery = "select fee_item_name, sum(accumulated_fee) as total_trx, sum(transaction_count) as total_trx_count,stl_status, from_role_cd, to_role_cd, from_player_id, to_player_id\n" +
                "from clr.accumulated_daily_clearing_log\n" +
                "where created > '05-JAN-20 07.03.11.627000000 AM" +
//                                "    where created > '"+ lastClearingTime +
                "' and fee_item_name in (" + feeItemList +
                ")\n group by fee_item_name, from_role_cd, to_role_cd, from_player_id, to_player_id, stl_status\n" +
                "order by fee_item_name desc";



        return p2pAccumulatedDailyClearingQuery;
    }
    public String p2pAccumulatedSettlementQuery(String lastClearingTime, String feeItemList) {
        String p2pAccumulatedSettlementQuery = "select cumulative_log_id, fee_item_name, sum(total_fee) as total_trx, sum(transaction_count) as total_trx_count," +
                " from_clr_gl_no, to_clr_gl_no, from_stl_gl_no, to_stl_gl_no, from_role_cd, to_role_cd, from_player_id, to_player_id\n"+
                "from ACC_SYS.accumulated_settlement_log\n"+
                "    where cleared_date > '"+
                "05-JAN-20 07.03.11.627000000 AM"+
//                                lastClearingTime +
                "' and fee_item_name in (" + feeItemList +


                ") group by cumulative_log_id, fee_item_name, from_role_cd, to_role_cd, from_player_id, to_player_id, from_clr_gl_no, to_clr_gl_no, from_stl_gl_no, to_stl_gl_no \n"+
                "order by fee_item_name desc";

        return p2pAccumulatedSettlementQuery;
    }

    public String p2pCumulativeSettlementLogQuery(String lastClearingTime, String feeItemList) {
        String p2pCumulativeSettlementLogQuery = "select id, total_amount, transaction_count, stl_dt, settlement_status, from_stl_gl_no, to_stl_gl_no, from_player_id, to_player_id, from_role_cd, to_role_cd, created, updated \n" +
                "from ACC_SYS.cumulative_settlement_log\n" +
                "where id in\n" +
                "(select cumulative_log_id\n" +
                "from ACC_SYS.accumulated_settlement_log\n" +
                "where cleared_date > '"+
                "05-JAN-20 07.03.11.627000000 AM"+
//                                lastClearingTime +
                "' and fee_item_name in (" + feeItemList +



                ") and cumulative_log_id is not null)";

        return p2pCumulativeSettlementLogQuery;
    }
    public String accessChannelIdentifierQuery(String nrNo) {
        String accessChannelIdentifierQuery = "select distinct pan_entry_mode from clr.ias_daily_tx_log where nr_number in ('"+ nrNo+"')";

        return accessChannelIdentifierQuery;
    }



}
