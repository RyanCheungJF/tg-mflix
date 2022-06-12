package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.service.AwardService;
import com.tigergraph.tg.service.MovieService;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository implements TigerGraphRepository<Movie, String> {

    @Autowired
    StatementUtil stmtUtil;

    private final String INTERPRETED = "run interpreted(u=?)";

    public Optional<Movie> getMovie(String id) {
        String query = "GET Movie(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();
            rs.next();
            return Optional.of(MovieService.reconstructMovie(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Movie>> getByTitle(String phrase) {
        String queryBody = "INTERPRET QUERY () FOR GRAPH internship {\n"
            + "Movies = {Movie.*};\n"
            + "result = SELECT m FROM Movie:m WHERE m.title LIKE \"%" + phrase + "%\";\n"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Movie> result = new ArrayList<>();
                do {
                    while (rs.next()) {
                        result.add(MovieService.reconstructMovie(rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Award> getAward(String id) {
        Movie m = getMovie(id).orElse(null);
        // some movies do not have awards -> are marked with 'NA' as a default value
        if (m.equals(null) || m.getAwards().equals("NA")) {
            return Optional.empty();
        }
        try {
            return Optional.of(AwardService.reconstructAward(m));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
