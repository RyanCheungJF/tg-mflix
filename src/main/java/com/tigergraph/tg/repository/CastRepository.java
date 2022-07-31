package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.requestbody.AllLinksRequestBody;
import com.tigergraph.tg.model.requestbody.EntityType;
import com.tigergraph.tg.model.requestbody.EntityTypeWithFilter;
import com.tigergraph.tg.model.requestbody.EntityTypeWithPrimaryKey;
import com.tigergraph.tg.model.vertex.Cast;
import com.tigergraph.tg.util.AwardUtil;
import com.tigergraph.tg.util.HandlerUtil;
import com.tigergraph.tg.util.MovieUtil;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class CastRepository implements TigerGraphRepository<Cast, String> {

    private final String INTERPRETED = "run interpreted(u=?)";
    private final StatementUtil stmtUtil;

    public CastRepository(StatementUtil stmtUtil) {
        this.stmtUtil = stmtUtil;
    }

    public Optional<List<List<Object>>> getAllPathsFromASourceSetToTargetSet(AllLinksRequestBody jsonPayload) {
        HandlerUtil.checkInputBody(jsonPayload);
        return Optional.of(HandlerUtil.iterateSourcesToFindPaths(jsonPayload.getSources(),
            jsonPayload.getVertexFilters(), jsonPayload.getMaxLength()));
    }

    public Optional<List<List<Object>>> getAllLinksForCasts(Integer pathLength,
                                                            ArrayList<EntityType> filter) {
        // we first run a query to obtain all cast members and save the result
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
            + "allCast = {Cast.*};\n"
            + "resultCast = SELECT c FROM allCast:c -()- ;\n"
            + "PRINT resultCast;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<EntityTypeWithPrimaryKey> nodeArray = new ArrayList<>();
                while (rs.next()) {
                    // we add the result into our list
                    byte[] input = rs.getString("name").getBytes(StandardCharsets.UTF_8);
                    String cleanedInput = new String(HandlerUtil.cleanByteArray(input), StandardCharsets.UTF_8);
                    nodeArray.add(new EntityTypeWithPrimaryKey("Cast", cleanedInput));
                }
                // potentially might want to add some node filters so that it doesn't run forever
                return Optional.of(HandlerUtil.iterateSourcesToFindPaths(nodeArray, filter, pathLength));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<List<Object>>> getAllLinksWithTomatoMeter(Integer score, Integer pathLength) {
        // we first find out all movies satisfying the condition, then get the cast members directly related to them
        String queryBody = "INTERPRET QUERY (DOUBLE s) FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "resultMovie = SELECT m FROM Movies:m -()- :e WHERE m.tomatoes_meter == s;\n"
            + "resultCast = SELECT c FROM resultMovie:rm -()- Cast:c;\n"
            + "PRINT resultCast;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, String.valueOf(score));
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<EntityTypeWithPrimaryKey> nodeArray = new ArrayList<>();
                while (rs.next()) {
                    // the returned names from the query may have extra bytes, so we process it
                    byte[] input = rs.getString("name").getBytes(StandardCharsets.UTF_8);
                    String cleanedInput = new String(HandlerUtil.cleanByteArray(input), StandardCharsets.UTF_8);
                    nodeArray.add(new EntityTypeWithPrimaryKey("Cast", cleanedInput));
                }
                List<List<Object>> res = HandlerUtil.iterateSourcesToFindPaths(nodeArray, new ArrayList<>(), pathLength);
                if (res.equals(new ArrayList<>())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                return Optional.of(res);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<List<Object>>> getAllLinksWithAwardNumber(Integer awards, Integer pathLength) {
        ArrayList<String> movieSet = new ArrayList<>();
        // awards is a seralized property, so we can't directly query, and as such we gather all movies first
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "resultMovie = SELECT m FROM Movies:m -()- WHERE m.id != \"\";\n"
            + "resultCast = SELECT c FROM resultMovie:rm -()- Cast:c;\n"
            + "PRINT resultMovie;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // we then process each movie, deserialize to an object, and check if it passes the condition
                    if (AwardUtil.reconstructAward(MovieUtil.reconstructMovie(rs)).getAwardsReceived() > awards) {
                        movieSet.add(rs.getString("id"));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ArrayList<EntityTypeWithPrimaryKey> nodeArray = new ArrayList<>();
        // so for each movie node in our result, we find the cast members directly attached to the filtered movies
        for (String s : movieSet) {
            String query = "INTERPRET QUERY (String s) FOR GRAPH mflix {\n"
                + "Movies = {Movie.*};\n"
                + "resultMovie = SELECT m FROM Movies:m -()- WHERE m.id == s;\n"
                + "resultCast = SELECT c FROM resultMovie:rm -()- Cast:c;\n"
                + "PRINT resultCast;\n"
                + "}\n";
            try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
                stmt.setObject(1, s);
                stmt.setString(2, query);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // the returned names from the query may have extra bytes, so we process it
                        byte[] input = rs.getString("name").getBytes(StandardCharsets.UTF_8);
                        String cleanedInput = new String(HandlerUtil.cleanByteArray(input), StandardCharsets.UTF_8);
                        nodeArray.add(new EntityTypeWithPrimaryKey("Cast", cleanedInput));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        // we then take the list of casts, and find all paths in between them
        List<List<Object>> res = HandlerUtil.iterateSourcesToFindPaths(nodeArray, new ArrayList<>(), pathLength);
        if (res.equals(new ArrayList<>())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Optional.of(res);
    }

    public Optional<List<List<Object>>> getAllLinksWithImdbRatingNumber(Double rating, Integer pathLength) {
        // we first find out all movies satisfying the condition, then get the cast members directly related to them
        String queryBody = "INTERPRET QUERY (DOUBLE r) FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "resultMovie = SELECT m FROM Movies:m -()- Imdb:i WHERE m.id != \"\" AND i.rating >= r;\n"
            + "resultCast = SELECT c FROM resultMovie:rm -()- Cast:c;\n"
            + "PRINT resultCast;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, String.valueOf(rating));
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<EntityTypeWithPrimaryKey> nodeArray = new ArrayList<>();
                while (rs.next()) {
                    // the returned names from the query may have extra bytes, so we process it
                    byte[] input = rs.getString("name").getBytes(StandardCharsets.UTF_8);
                    String cleanedInput = new String(HandlerUtil.cleanByteArray(input), StandardCharsets.UTF_8);
                    nodeArray.add(new EntityTypeWithPrimaryKey("Cast", cleanedInput));
                }
                // we then take the list of casts, and find all paths in between them
                List<List<Object>> res = HandlerUtil.iterateSourcesToFindPaths(nodeArray, new ArrayList<>(), pathLength);
                if (res.equals(new ArrayList<>())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                return Optional.of(res);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<List<Object>>> getAllIndirectLinksWithAwardNumber(Integer awards,
                                                                               AllLinksRequestBody jsonPayload) {
        HandlerUtil.checkInputBody(jsonPayload);
        ArrayList<String> movieSet = new ArrayList<>();
        // awards is a serialized property, so we can't directly query, and as such we gather all movies first
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
            + "Movies = {Movie.*};\n"
            + "resultMovie = SELECT m FROM Movies:m -()- WHERE m.id != \"\";\n"
            + "resultCast = SELECT c FROM resultMovie:rm -()- Cast:c;\n"
            + "PRINT resultMovie;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // we then process each movie, deserialize to an object, and check if it passes the condition
                    if (AwardUtil.reconstructAward(MovieUtil.reconstructMovie(rs)).getAwardsReceived() > awards) {
                        movieSet.add(rs.getString("id"));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // we then want the paths to pass through specific nodes, so we formulate the filters here
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movieSet.size(); i++) {
            if (i == movieSet.size() - 1) {
                sb.append(String.format("id==\"%s\"", movieSet.get(i)));
            } else {
                sb.append(String.format("id==\"%s\" or ", movieSet.get(i)));
            }
        }
        ArrayList<Object> filters = new ArrayList<>();
        filters.add(new EntityType("Cast"));
        filters.add(new EntityType("Director"));
        filters.add(new EntityType("Genre"));
        filters.add(new EntityType("Writer"));
        filters.add(new EntityType("User"));
        filters.add(new EntityType("Imdb"));
        filters.add(new EntityTypeWithFilter("Movie", sb.toString()));
        // query between cast members in award-winning movies, only using said movies and cast to traverse
        List<List<Object>> res = HandlerUtil.iterateSourcesToFindPaths(jsonPayload.getSources(), filters,
            jsonPayload.getMaxLength());
        if (res.equals(new ArrayList<>())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Optional.of(res);
    }

    public Optional<List<List<Object>>> getLinkBetweenTwoCastsTraverseMovie(String src, String tgt, Integer pathLength) {
        ArrayList<EntityTypeWithPrimaryKey> srcArr = new ArrayList<>();
        ArrayList<EntityTypeWithPrimaryKey> tgtArr = new ArrayList<>();
        srcArr.add(new EntityTypeWithPrimaryKey("Cast", src));
        tgtArr.add(new EntityTypeWithPrimaryKey("Cast", tgt));
        ArrayList<Object> filter = new ArrayList<>();
        filter.add(new EntityType("Cast"));
        filter.add(new EntityType("Movie"));
        AllLinksRequestBody rq = new AllLinksRequestBody(srcArr, tgtArr, filter, new ArrayList<>(), pathLength);
        List<List<Object>> res = HandlerUtil.makeAllPathsCurlRequest(rq, new HashSet<>()).orElse(new ArrayList<>());
        if (res.equals(new ArrayList<>())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Optional.of(res);
    }

    public Optional<List<List<Object>>> getDirectLinkBetweenTwoCastsTraverseMovie(String src, String tgt) {
        ArrayList<EntityTypeWithPrimaryKey> srcArr = new ArrayList<>();
        ArrayList<EntityTypeWithPrimaryKey> tgtArr = new ArrayList<>();
        srcArr.add(new EntityTypeWithPrimaryKey("Cast", src));
        tgtArr.add(new EntityTypeWithPrimaryKey("Cast", tgt));
        ArrayList<Object> filter = new ArrayList<>();
        filter.add(new EntityType("Movie"));
        AllLinksRequestBody rq = new AllLinksRequestBody(srcArr, tgtArr, filter, new ArrayList<>(), 2);
        List<List<Object>> res = HandlerUtil.makeAllPathsCurlRequest(rq, new HashSet<>()).orElse(new ArrayList<>());
        if (res.equals(new ArrayList<>())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Optional.of(res);
    }

    public Optional<List<List<Object>>> getLinkBetweenCastAndMovieTraverseMovie(String src, String tgt, Integer pathLength) {
        ArrayList<EntityTypeWithPrimaryKey> srcArr = new ArrayList<>();
        ArrayList<EntityTypeWithPrimaryKey> tgtArr = new ArrayList<>();
        srcArr.add(new EntityTypeWithPrimaryKey("Cast", src));
        tgtArr.add(new EntityTypeWithPrimaryKey("Movie", tgt));
        ArrayList<Object> filter = new ArrayList<>();
        filter.add(new EntityType("Cast"));
        filter.add(new EntityType("Movie"));
        AllLinksRequestBody rq = new AllLinksRequestBody(srcArr, tgtArr, filter, new ArrayList<>(), pathLength);
        List<List<Object>> res = HandlerUtil.makeAllPathsCurlRequest(rq, new HashSet<>()).orElse(new ArrayList<>());
        if (res.equals(new ArrayList<>())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return Optional.of(res);
    }
}
