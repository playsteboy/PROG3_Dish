package org.example;

public class InvoiceTotal {
    private int id;
    private String customerName;
    private InvoiceStatus status;
    private Double amount;

    public InvoiceTotal(int id, String customerName, InvoiceStatus status, Double amount) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.amount = amount;
    }

    public InvoiceTotal() {
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InvoiceTotal{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", status=" + status +
                ", amount=" + amount +
                '}';
    }
}
