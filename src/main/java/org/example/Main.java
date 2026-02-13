package org.example;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        System.out.println(
        dataRetriever.getStockStatusEvolution("Day",LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5))
        );
    }
}