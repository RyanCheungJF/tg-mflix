package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.util.MovieUtil;
import com.tigergraph.tg.util.ParameterUtil;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository implements TigerGraphRepository<Movie, String> {

    private final String INTERPRETED = "run interpreted(u=?)";
    private final StatementUtil stmtUtil;

    public MovieRepository(StatementUtil stmtUtil) {
        this.stmtUtil = stmtUtil;
    }

    public Optional<Movie> getMovie(String id) {
        String query = "GET Movie(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();
            rs.next();
            return Optional.of(MovieUtil.reconstructMovie(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Movie>> getByTitle(String phrase) {
        String queryBody = "INTERPRET QUERY () FOR GRAPH internship {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT m FROM Movie:m WHERE m.title LIKE \"%" + ParameterUtil.decodeParameter(phrase) + "%\";\n"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MovieUtil.reconstructMovie(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Movie>> getByScore(Double score) {
        String queryBody = "INTERPRET QUERY (DOUBLE s) FOR GRAPH internship {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT t FROM Movies -(Rated_As>)- Imdb:i -(reverse_Rated_As>) - Movie:t WHERE i.rating >= s;\n"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, String.valueOf(score));
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MovieUtil.reconstructMovie(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
