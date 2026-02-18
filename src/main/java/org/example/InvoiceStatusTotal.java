package org.example;

public class InvoiceStatusTotal {
    private int id;
    private InvoiceStatus status;
    private Double unit_price;

    public InvoiceStatusTotal() {
    }
    public InvoiceStatusTotal(int id, InvoiceStatus status, Double unit_price) {
        this.id = id;
        this.status = status;
        this.unit_price = unit_price;
    }

    public int getId() {
        return id;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    @Override
    public String toString() {
        return "InvoiceStatusTotal{" +
                "id=" + id +
                ", status=" + status +
                ", unit_price=" + unit_price +
                '}';
    }
}
