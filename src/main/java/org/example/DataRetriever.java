package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataRetriever {
    public List<InvoiceTotal> findInvoiceTotals() throws SQLException {
        List<InvoiceTotal> invoiceTotals = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
select invoice.id as iId, invoice.customer_name iCN, SUM(invoice_line.unit_price*invoice_line.quantity) as iP
from invoice join invoice_line on invoice.id = invoice_line.invoice_id
group by(invoice.id, invoice.customer_name)
"""
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            InvoiceTotal invoiceTotal = new InvoiceTotal();
            invoiceTotal.setId(resultSet.getInt("iId"));
            invoiceTotal.setCustomerName(resultSet.getString("iCN"));
            invoiceTotal.setAmount(resultSet.getDouble("iP"));
            invoiceTotals.add(invoiceTotal);
        }
        return invoiceTotals;
    }
    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() throws SQLException {
        List<InvoiceTotal> invoiceTotals = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("""
select invoice.id as iId, invoice.customer_name iCN,invoice.status as iS , SUM(
Case 
    WHEN invoice.status = 'CONFIRMED' OR invoice.status = 'PAID' then invoice_line.unit_price*invoice_line.quantity
end) as iP
from invoice join invoice_line on invoice.id = invoice_line.invoice_id
                               where invoice.status = 'CONFIRMED' or invoice.status = 'PAID'
group by(invoice.id, invoice.customer_name) 
""");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            InvoiceTotal invoiceTotal = new InvoiceTotal();
            invoiceTotal.setId(resultSet.getInt("iId"));
            invoiceTotal.setStatus(InvoiceStatus.valueOf(resultSet.getString("iS")));
            invoiceTotal.setCustomerName(resultSet.getString("iCN"));
            invoiceTotal.setAmount(resultSet.getDouble("iP"));
            invoiceTotals.add(invoiceTotal);
        }
        return invoiceTotals;
    }
    public List<InvoiceStatusTotal> computeStatusTotal() throws SQLException {
        List<InvoiceStatusTotal> invoiceStatusTotals = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("""
select invoice.status as iS , SUM(
invoice_line.unit_price*invoice_line.quantity)
as iP
from invoice_line join invoice on invoice.id = invoice_line.invoice_id
group by(invoice.status) 
""");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            InvoiceStatusTotal invoiceStatusTotal = new InvoiceStatusTotal();
            invoiceStatusTotal.setStatus(InvoiceStatus.valueOf(resultSet.getString("iS")));
            invoiceStatusTotal.setUnit_price(resultSet.getDouble("iP"));
            invoiceStatusTotals.add(invoiceStatusTotal);
        }
        return invoiceStatusTotals;
    }
    public Double computeWeightedTurnover() throws SQLException {
        List<InvoiceStatusTotal> IStotals = computeStatusTotal();
        Double weightedTurnover = 0.0;
        for(InvoiceStatusTotal it : IStotals){
            if(it.getStatus().equals(InvoiceStatus.CONFIRMED)){
                weightedTurnover = (weightedTurnover + it.getUnit_price())/2;
            }
            if(it.getStatus().equals(InvoiceStatus.PAID)){
                weightedTurnover = (weightedTurnover + it.getUnit_price());
            }
        }
        return weightedTurnover;
    }
    public List<InvoiceTaxSummary> findInvoiceTaxSummary() throws SQLException {
        List<InvoiceTaxSummary> invoiceTaxSummary = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
select invoice.id as iId,
SUM(invoice_line.unit_price * invoice_line.quantity) AS HT,
SUM(invoice_line.unit_price * invoice_line.quantity) * (tax_config.rate / 100) AS TVA,
SUM(invoice_line.unit_price * invoice_line.quantity) * (1 + tax_config.rate / 100) AS TTC
from invoice join invoice_line on invoice.id = invoice_line.invoice_id 
join tax_config on tax_config.id = tax_config.id
group by(invoice.id,tax_config.rate) 
"""
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            invoiceTaxSummary.add(
                    new InvoiceTaxSummary(
                            resultSet.getInt("iId"),
                            resultSet.getDouble("HT"),
                            resultSet.getDouble("TVA"),
                            resultSet.getDouble("TTC")
                    )
            );
        }
        return invoiceTaxSummary;
    }
    public BigDecimal computeWeightedTurnoverTtc() throws SQLException {
        Map<InvoiceTotal, InvoiceTaxSummary> map = new HashMap<>();
        Double result = 0.0;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
                """
select invoice.id as iId,invoice.status as iS,
SUM(invoice_line.unit_price * invoice_line.quantity) AS HT,
SUM(invoice_line.unit_price * invoice_line.quantity) * (tax_config.rate / 100) AS TVA,
SUM(invoice_line.unit_price * invoice_line.quantity) * (1 + tax_config.rate / 100) AS TTC
from invoice join invoice_line on invoice.id = invoice_line.invoice_id 
join tax_config on tax_config.id = tax_config.id
group by(invoice.id,tax_config.rate,invoice.status) 
"""
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            InvoiceTotal  invoiceTotal = new InvoiceTotal();
            invoiceTotal.setId(resultSet.getInt("iId"));
            invoiceTotal.setStatus(InvoiceStatus.valueOf(resultSet.getString("iS")));
            map.put(invoiceTotal,
                    new InvoiceTaxSummary(
                            resultSet.getInt("iId"),
                            resultSet.getDouble("HT"),
                            resultSet.getDouble("TVA"),
                            resultSet.getDouble("TTC")
                    )
            );
        }
        for (Map.Entry<InvoiceTotal, InvoiceTaxSummary> entry : map.entrySet()) {
            InvoiceTotal invoiceTotal = entry.getKey();
            InvoiceTaxSummary invoiceTaxSummary = entry.getValue();
            if (invoiceTotal.getStatus().equals(InvoiceStatus.CONFIRMED)) {
                result = (result + invoiceTaxSummary.getTTC()) / 2;
            }
            if (invoiceTotal.getStatus().equals(InvoiceStatus.PAID)) {
                result = (result + invoiceTaxSummary.getTTC());
            }
        }
        return BigDecimal.valueOf(result);
    }
}