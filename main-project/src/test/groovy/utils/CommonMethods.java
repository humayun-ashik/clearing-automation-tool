package utils;

//import org.apache.log4j.Logger;

import core.calculation.feeComCalculation;

import java.io.FileWriter;
import java.io.IOException;
import org.omg.CORBA.WStringSeqHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

public class CommonMethods {

    public static FileOutputStream fos;
    public static PrintStream ps;

    public CommonMethods()  {
    }

    public CommonMethods(String fileName) throws IOException {
//        File file = new File(fileName);
//        fos = new FileOutputStream(file);
//        ps = new PrintStream(fos);
//        this.writeToTextFile();
    }

    public static void finalize(String fileName) throws IOException {
        ps.close();
        fos.close();
//        ps =  System.out;
        System.setOut(System.out);
        System.out.println("Result has been written in " + fileName + " file");
    }

    public static List<String[]> getNrQueryResult(Queries queries, String lastClearingTime, String processingCode, ExecuteQuery executeQuery) {
        List<String[]> NrQueryResult = feeComCalculation.NrQueryResult(queries, lastClearingTime, executeQuery, processingCode);
        for (String[] col : NrQueryResult) {
            String nrNo = col[0];
//            writeToTextFile(fileName);
            System.out.println(nrNo);
        }
        return NrQueryResult;
    }
    public static void getViewFeeComResult(Queries queries, String processingCode, ExecuteQuery executeQuery, String accessChannel, String userSegment) {
        List<String[]> viewFeeComResult = feeComCalculation.viewFeeComResult(queries, processingCode, executeQuery, accessChannel, userSegment);
        int nCol = ExecuteQuery.nCol;
        for (String[] col : viewFeeComResult) {
            for (int i = 0; i < nCol; ++i) {
                if (i == 0 || i == 2) {
                    System.out.print(String.format(" %-40s ", col[i]));
                } else {
                    System.out.print(String.format(" %-30s ", col[i]));
                }

            }
            System.out.println();
        }
    }


    public static List<String> getFeeComQueryResult(Queries queries, List<String[]> NrQueryResult, ExecuteQuery executeQuery) {
        int iteration = 0;
        List<String> feeItems = new ArrayList<String>();
        for (String[] col : NrQueryResult) {
            //Select number of iteration
            //TODO: Select NR number rendomly from List
            if (iteration++ >= 3) {
                break;
            }
            String nrNo = col[0];
            List<String[]> feeComQueryResult = feeComCalculation.feeComQueryResult(queries, nrNo, executeQuery);
            int nCol = ExecuteQuery.nCol;
            for (String[] col1 : feeComQueryResult) {
                for (int i = 0; i < nCol; ++i) {
                    if (i == 2) {
                        System.out.print(String.format(" %-35s ", col1[i]));
                        feeItems.add(col1[i]);
//
                    } else {
                        System.out.print(String.format(" %-30s ", col1[i]));
                    }

                }
                System.out.println();
            }
        }
        return feeItems;
    }

    public static List<String> getAndViewFeeComQueryResult(Queries queries, List<String[]> NrQueryResult, ExecuteQuery executeQuery, String processingCode, String accessChannel) {
        int iteration = 0;
        List<String> feeItems = new ArrayList<String>();
        for (String[] col : NrQueryResult) {
            //Select number of iteration
            //TODO: Select NR number rendomly from List
//            if (iteration++ >= 3) {
//                break;
//            }
            String nrNo = col[0];
            List<String[]> feeComQueryResult = feeComCalculation.feeComQueryResult(queries, nrNo, executeQuery);

            int nCol = ExecuteQuery.nCol;
            for (String[] col1 : feeComQueryResult) {
                for (int i = 0; i < nCol; ++i) {
                    if (i == 2) {
                        System.out.print(String.format(" %-35s ", col1[i]));
                        feeItems.add(col1[i]);
//
                    } else {
                        System.out.print(String.format(" %-30s ", col1[i]));
                    }

                }
                System.out.println();
            }
            List<String[]> viewDynamicFeeComQueryResult = feeComCalculation.viewDynamicFeeComResult(nrNo, queries, processingCode, executeQuery, accessChannel);
            nCol = ExecuteQuery.nCol;
            for (String[] col1 : viewDynamicFeeComQueryResult) {
                for (int i = 0; i < nCol; ++i) {
                    if (i == 0 || i == 2) {
                        System.out.print(String.format(" %-40s ", col1[i]));
                    } else {
                        System.out.print(String.format(" %-30s ", col1[i]));
                    }

                }
                System.out.println();
            }

        }
        return feeItems;
    }
    public static void getAccumulatedDailyClearingQuery(List<String> feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery) {
        String processedFeeItems = processedFeeItems(feeItems);
        List<String[]> AccumulatedDailyClearingQueryResult = feeComCalculation.AccumulatedDailyClearingQueryResult(processedFeeItems, queries, lastClearingTime, executeQuery);
        int nCol = ExecuteQuery.nCol;
        for (String[] col1 : AccumulatedDailyClearingQueryResult) {
            for (int i = 0; i < nCol; ++i) {
                if (i == 0) {
                    System.out.print(String.format(" %-35s ", col1[i]));
//
                } else {
                    System.out.print(String.format(" %-30s ", col1[i]));
                }

            }
            System.out.println();
        }
    }

    public static void getAccumulatedSettlementQuery(List<String> feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery) {
        String processedFeeItems = processedFeeItems(feeItems);
        List<String[]> AccumulatedSettlementQueryResult = feeComCalculation.AccumulatedSettlementQueryResult(processedFeeItems, queries, lastClearingTime, executeQuery);
        int nCol = ExecuteQuery.nCol;
        for (String[] col1 : AccumulatedSettlementQueryResult) {
            for (int i = 0; i < nCol; ++i) {
                if (i == 1) {
                    System.out.print(String.format(" %-35s ", col1[i]));
//
                } else {
                    System.out.print(String.format(" %-30s ", col1[i]));
                }

            }
            System.out.println();
        }
    }

    public static void getCumulativeSettlementLogQuery(List<String> feeItems, Queries queries, String lastClearingTime, ExecuteQuery executeQuery) {
        String processedFeeItems = processedFeeItems(feeItems);
        List<String[]> CumulativeSettlementLogQueryResult = feeComCalculation.CumulativeSettlementLogQueryResult(processedFeeItems, queries, lastClearingTime, executeQuery);
        int nCol = ExecuteQuery.nCol;
        for (String[] col1 : CumulativeSettlementLogQueryResult) {
            for (int i = 0; i < nCol; ++i) {
                if (i == 0) {
                    System.out.print(String.format(" %-35s ", col1[i]));
//
                } else {
                    System.out.print(String.format(" %-30s ", col1[i]));
                }

            }
            System.out.println();
        }
    }
    public static String processedFeeItems(List<String> feeItems){
        String str = "";
        for (int i = 0; i < feeItems.size(); i++) {
            str += "\'" + feeItems.get(i) + "\'" + ",";
        }
        str = str.substring(0, str.length() - 1);
        return  str;
    }

    public static void writeToTextFile(String fileName) throws IOException {

//        PrintStream console = System.out;
        File file = new File(fileName);
        fos = new FileOutputStream(file);
        ps = new PrintStream(fos);
        System.setOut(ps);
//        fos.close();
//        ps.close();
//        System.out.println("This goes to out.txt");

//        System.setOut(console);
//        System.out.println("Result has been written in " + filename + " file");

    }

}
