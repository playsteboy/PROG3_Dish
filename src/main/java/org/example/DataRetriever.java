package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        connection.close();
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
        connection.close();
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
        connection.close();
        return invoiceStatusTotals;
    }
    public Double computeWeightedTurnover(){
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        Double weightedTurnover = 0.0;
        try(PreparedStatement pstmt = connection.prepareStatement(
                """
select sum(case
               when status = 'PAID' then il.quantity * il.unit_price * 1
               else
                   case
                       when status = 'CONFIRMED' then il.quantity * il.unit_price * 0.5
                       else
                           case when invoice.status = 'DRAFT' then il.quantity * il.unit_price * 0 else 0 end
                       end
    end)
from invoice
         join invoice_line il on invoice.id = il.invoice_id;

"""
        )){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                weightedTurnover+=rs.getDouble(1);
            }
            return  weightedTurnover;

        }catch (SQLException ex) {
            throw  new RuntimeException(ex);
        }
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
        connection.close();
        return invoiceTaxSummary;
    }
    public BigDecimal computeWeightedTurnoverTtc() {
        Double result = 0.0;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(
                """
select sum(case
               when status = 'PAID' then montant_ttc * 1
               else
                   case
                       when status = 'CONFIRMED' then montant_ttc * 0.5
                       else
                           case when invoice.status = 'DRAFT' then montant_ttc * 0 else 0 end
                       end
    end)
from invoice
         join (
              select invoice_id,
                     montant_ht,
                     montant_ht * tax_config.rate / 100              as tva,
                     montant_ht + montant_ht * tax_config.rate / 100 as montant_ttc
              from (select invoice_id, sum(quantity * unit_price) as montant_ht
                    from invoice_line
                    group by invoice_id) invoice_montant_ht,
                   tax_config) il on invoice.id = il.invoice_id;
"""
        )){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result = result+rs.getDouble(1);
            }
            return BigDecimal.valueOf(result);

        }catch (SQLException ex) {
            throw  new RuntimeException(ex);
        }
    }
}