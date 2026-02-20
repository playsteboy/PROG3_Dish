package org.example;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)throws SQLException{
        DataRetriever dr = new DataRetriever();
        System.out.println(
                "total_votes"+" = "+dr.countAllVotes()

        );
//        System.out.println(
//                dr.countVotesByType().toString()
//        );
//        System.out.println(
//                dr.countValidVotesByCandidate().toString()
//        );
//        System.out.println(
//                dr.computeVoteSummary().toString()
//        );
//        System.out.println(
//                dr.computeTurnoutRate()
//        );
//        System.out.println(
//                dr.findWinner().toString()
//        );

    }

}