package org.example;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public  long countAllVotes(){
        DBConnection db = new DBConnection();
        long count=0;
        Connection conn = db.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select count(id) as total_votes from vote
"""
        )) {
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                count= rs.getLong(1);
            }
            return count;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<VoteTypeCount> countVotesByType(){
        DBConnection db = new DBConnection();
        List<VoteTypeCount> voteTypeCounts = new ArrayList<>();
        Connection conn = db.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select vote_type,count(id) as total_votes from vote group by vote_type
"""
        )){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                voteTypeCounts.add(new VoteTypeCount(
                        VoteType.valueOf(rs.getString("vote_type")),
                        rs.getInt("total_votes")
                ));
            }
            return voteTypeCounts;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<CandidateVoteCount> countValidVotesByCandidate(){
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        List<CandidateVoteCount> candidateVoteCounts = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select candidate.name, count(case
when vote_type = 'VALID' then vote_type
end) as total_votes 
from vote join candidate on candidate.id=vote.candidate_id
group by candidate.name
"""
        )){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                candidateVoteCounts.add(new CandidateVoteCount(
                        rs.getString("name"),
                        rs.getInt("total_votes")
                ));
            }
            return candidateVoteCounts;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public VoteSummary computeVoteSummary(){
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select count(case
when vote_type = 'VALID' then vote_type
end) as valid_count,count(case
when vote_type = 'BLANK' then vote_type
end) as blank_count,count(case
when vote_type = 'NULL' then vote_type
end) as null_count
from vote

"""
        )){
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new VoteSummary(
                        rs.getInt("valid_count"),
                        rs.getInt("blank_count"),
                        rs.getInt("null_count")
                );
            }                return null;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public double computeTurnoutRate(){
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        double turnoutRate = 0;
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select count(voter_id) as total_voters, count(voter.id) as all_voters
from vote join voter on voter.id=vote.voter_id
group by voter.id
"""
        )){
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                turnoutRate = rs.getDouble("total_voters")/rs.getDouble("all_voters")*100;
            }
            return turnoutRate;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public ElectionResult findWinner(){
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(
                """
select candidate.name as name, count(case
when vote_type = 'VALID' then vote_type
end) as total_votes 
from vote join candidate on candidate.id=vote.candidate_id
group by candidate.name
order by total_votes desc
"""
        )){
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new ElectionResult(
                        rs.getString("name"),
                        rs.getInt("total_votes")
                );
            }return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}