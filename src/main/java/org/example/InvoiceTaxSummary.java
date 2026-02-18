package org.example;

public class InvoiceTaxSummary {
    private int id;
    private double HT;
    private double TVA;
    private double TTC;

    public InvoiceTaxSummary(int id, double HT, double TVA, double TTC) {
        this.id = id;
        this.HT = HT;
        this.TVA = TVA;
        this.TTC = TTC;
    }

    public int getId() {
        return id;
    }

    public double getHT() {
        return HT;
    }

    public double getTVA() {
        return TVA;
    }

    public double getTTC() {
        return TTC;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHT(double HT) {
        this.HT = HT;
    }

    public void setTVA(double TVA) {
        this.TVA = TVA;
    }

    public void setTTC(double TTC) {
        this.TTC = TTC;
    }

    @Override
    public String toString() {
        return "InvoiceTaxSummary{" +
                "id=" + id +
                ", HT=" + HT +
                ", TVA=" + TVA +
                ", TTC=" + TTC +
                '}';
    }
}
