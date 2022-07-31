package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.vertex.User;
import com.tigergraph.tg.util.HandlerUtil;
import com.tigergraph.tg.util.StatementUtil;
import com.tigergraph.tg.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements TigerGraphRepository<User, String> {

    private final String INTERPRETED = "run interpreted(u=?)";
    private final StatementUtil stmtUtil;

    public UserRepository(StatementUtil stmtUtil) {
        this.stmtUtil = stmtUtil;
    }

    public Optional<User> getUserById(String id) {
        String query = "GET User(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();
            rs.next();
            return Optional.of(UserUtil.reconstructUser(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<User>> getAllUsers() {
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
            + "Users = {User.*};\n"
            + "result = SELECT u FROM Users:u -()- ;"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<User> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(UserUtil.reconstructUser(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<User> addUser(User newUser) {
        String query = "INSERT INTO VERTEX User(id, password, name, email) VALUES(?, ?, ?, ?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, newUser.getId());
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.getName());
            stmt.setString(4, newUser.getEmail());
            stmt.addBatch();
            stmt.executeBatch();
            return Optional.of(newUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<ResponseEntity<String>> deleteUser(String id) {
        String query = "DELETE User(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                rs.next();
                // First parameter specifies how many nodes are deleted. If zero, means node isn't in graph
                if (rs.getObject(1).equals(0)) {
                    return Optional.of(new ResponseEntity<>(
                        String.format("No node with the following id %s deleted", id), HttpStatus.NOT_FOUND));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(rs.getObject(2)).append(" node with id ").append(id).append(" deleted");
                return Optional.of(new ResponseEntity<>(sb.toString(), HttpStatus.OK));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<Object>> getKHops(String id, Integer hops) {
        /**
            start = {u} : initialzes a set for the user which we want to start the search from
            (<_|_>)*{num} : we want to match this pattern using * {num} amount of times
            the _ states that we can use any edge to traverse
            the <, > specifies direction of the edge
            the | specifies or
            hence, it means we can use any edge in any direction to make our {num} hops
        */
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH mflix {\n"
                + "start = {u};\n"
                + String.format("result = SELECT v FROM start:s -((<_|_>)*%s)- :v\n", hops)
                // without a limit >= 3 hop queries result may be up to 20000k+ lines of JSON
                + "WHERE u != v LIMIT 25;\n"
                + "PRINT result;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object> result = new ArrayList<>();
                do {
                    java.sql.ResultSetMetaData metaData = rs.getMetaData();
                    String nodeName = metaData.getCatalogName(1);
                    while (rs.next()) {
                        result.add(HandlerUtil.reconstructFromResultSetHandler(nodeName, rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<List<Object>> matchUsersByNumberOfComments(Integer num) {
        /**
            since we do not take in any user id, we initialize the start set to be all user nodes
            we use a local accumulator (specified by @) to attach a temp 'cnt' var to each node in our graph in runtime
            this accum is just to keep track of the no. of outgoing edges a node has
            in Neighbours we get back all user nodes, but each comes attached with a 'cnt' on number of outgoing edges
            in Ver we filter out to get the nodes matching our endpoint param of {num} edges only
            we also get the edges that the nodes in Ver have in the same line, similar to getCommentsByUser
        */
        String queryBody = "INTERPRET QUERY () FOR GRAPH mflix {\n"
                + "SumAccum<INT> @cnt = 0;\n"
                + "ListAccum<edge> @@edgeList;\n"
                + "start = {User.*};\n"
                + "Neighbours = SELECT s FROM start:s -()- ACCUM s.@cnt += 1;\n"
                + "Ver = SELECT n FROM Neighbours:n -(Comments_On:e)- \n"
                + String.format("WHERE n.@cnt == %s ACCUM @@edgeList += e;\n", num)
                + "PRINT Ver;\n"
                + "PRINT @@edgeList;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            // this has to be set to an empty string if we're not taking in parameters
            stmt.setString(1, "");
            stmt.setString(2, queryBody);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object> result = new ArrayList<>();
                do {
                    java.sql.ResultSetMetaData metaData = rs.getMetaData();
                    String nodeName = metaData.getCatalogName(1);
                    while (rs.next()) {
                        result.add(HandlerUtil.reconstructFromResultSetHandler(nodeName, rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
