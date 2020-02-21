package core.calculation;

import utils.Constants;
import utils.ExecuteQuery;

import java.util.List;

public class VerifyWithDB {


    int basicRate = Constants.p2p_BasicRate;
    public static void ExpectedCalculation(List<String[]> viewFeeComResult){
        for (String[] col : viewFeeComResult) {
            int nCol = ExecuteQuery.nCol;
//            for (int i = 0; i < nCol; i++) {
//                System.out.print(col[i] + " ");
//
//            }
            System.out.println(col[2]+ "\t\t\t\t\t"+ col[3] + "\t");
        }

    }
    public static void ActualCalculation(){
        double trx_amount = 1000;


    }

}
