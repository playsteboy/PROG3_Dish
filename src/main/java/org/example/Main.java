package org.example;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)throws SQLException{
        DataRetriever dr = new DataRetriever();
        List<InvoiceTotal> Itotals = dr.findInvoiceTotals();
        for (InvoiceTotal it : Itotals) {
            System.out.printf("%d | %-7s | %.2f%n",
                    it.getId(),
                    it.getCustomerName(),
                    it.getAmount());
        }
//        List<InvoiceTotal> CPItotals = dr.findConfirmedAndPaidInvoiceTotals();
//        for (InvoiceTotal it : CPItotals) {
//            System.out.printf("%d | %-7s | %-10s | %.2f%n",
//                    it.getId(),
//                    it.getCustomerName(),
//                    it.getStatus().toString(),
//                    it.getAmount());
//        }
//        List<InvoiceStatusTotal> IStotals = dr.computeStatusTotal();
//        for (InvoiceStatusTotal it : IStotals) {
//            System.out.printf("%-10s = %.2f%n",
//                    "total_"+it.getStatus().toString().toLowerCase(),
//                    it.getUnit_price());
//        }
//        System.out.println(dr.computeWeightedTurnover());
//        List<InvoiceTaxSummary> ITStotals = dr.findInvoiceTaxSummary();
//        for (InvoiceTaxSummary it : ITStotals) {
//            System.out.printf("%-4d | HT: %8.2f | TVA: %8.2f | TTC: %8.2f%n",
//                    it.getId(),
//                    it.getHT(),
//                    it.getTVA(),
//                    it.getTTC());
//        }
//        System.out.println(dr.computeWeightedTurnoverTtc());
    }

}