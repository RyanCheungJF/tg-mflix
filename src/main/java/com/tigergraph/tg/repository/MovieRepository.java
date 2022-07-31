package com.tigergraph.tg.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigergraph.tg.model.requestbody.AllLinksRequestBody;
import com.tigergraph.tg.model.requestbody.EntityType;
import com.tigergraph.tg.model.requestbody.EntityTypeWithPrimaryKey;
import com.tigergraph.tg.model.vertex.Movie;
import com.tigergraph.tg.util.HandlerUtil;
import com.tigergraph.tg.util.MovieUtil;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<Movie>> getByPlot(String[] jsonPayload) {
        StringBuilder queryFilter = new StringBuilder();
        // build string used for regex filtering
        for (int i = 0; i < jsonPayload.length; i++) {
            queryFilter.append("m.plot LIKE \"%").append(jsonPayload[i]).append("%\"");
            if (i != jsonPayload.length - 1) {
                queryFilter.append(" AND ");
            } else {
                queryFilter.append(";\n");
            }
        }
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT m FROM Movie:m WHERE "
            + queryFilter
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MovieUtil.reconstructMovie(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // dbl check this, turns out imdb id might not be unique? fault in dataset
    public Optional<List<Movie>> getByImdbId(Integer id) {
        String queryBody = "INTERPRET QUERY (INT idx) FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT t FROM Movies -(Rated_As>)- Imdb:i -(reverse_Rated_As>) - Movie:t WHERE i.id == idx;\n"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, String.valueOf(id));
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MovieUtil.reconstructMovie(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<Movie>> getByImdbScore(Double score) {
        String queryBody = "INTERPRET QUERY (DOUBLE s) FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT t FROM Movies -(Rated_As>)- Imdb:i -(reverse_Rated_As>) - Movie:t WHERE i.rating >= s;\n"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, String.valueOf(score));
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(MovieUtil.reconstructMovie(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
