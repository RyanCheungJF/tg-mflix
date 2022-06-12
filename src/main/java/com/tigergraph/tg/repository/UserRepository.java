package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.model.User;
import com.tigergraph.tg.service.*;
import com.tigergraph.tg.util.HandlerUtil;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements TigerGraphRepository<User, String> {

    @Autowired
    StatementUtil stmtUtil;

    private final String INTERPRETED = "run interpreted(u=?)";

    public Optional<User> getUserById(String id) {
        String query = "GET User(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();
            rs.next();
            return Optional.of(UserService.reconstructUser(rs));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<User>> getAllUsers() {
        String queryBody = "INTERPRET QUERY () FOR GRAPH internship {\n"
            + "Users = {User.*};\n"
            + "result = SELECT u FROM Users:u -()- ;"
            + "PRINT result;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "");
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<User> result = new ArrayList<>();
                do {
                    while (rs.next()) {
                        result.add(UserService.reconstructUser(rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
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
        }
        return Optional.empty();
    }

    public Optional<String> deleteUser(String id) {
        String query = "DELETE User(id=?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, id);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                rs.next();
                // First parameter specifies how many nodes are deleted. If zero, means node isn't in graph
                if (rs.getObject(1).equals(0)) {
                    return Optional.of(String.format("No node with the following id %s deleted", id));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(rs.getObject(2)).append(" node with id ").append(id).append(" deleted");
                return Optional.of(sb.toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    /**
    public Optional<List<Movie>> getUserOneHop(String id) {
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH internship {\n"
                + "start = {u};\n"
                + "result = SELECT m FROM start:s -(Comments_On)- Movie:m;\n"
                + "PRINT result;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
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

    public Optional<List<User>> getNeighbouringUsers(String id) {
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH internship {\n"
                + "start = {u};\n"
                + "result = SELECT v FROM start:s -(Comments_On>)- Movie -(reverse_Comments_On>)- User:v\n"
                + "WHERE u != v;\n"
                + "PRINT result;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<User> result = new ArrayList<>();
                do {
                    while (rs.next()) {
                        result.add(UserService.reconstructUser(rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
    */

    public Optional<List<Object>> getKHops(String id, Integer hops) {
        /**
            start = {u} : initialzes a set for the user which we want to start the search from
            (<_|_>)*{num} : we want to match this pattern using * {num} amount of times
            the _ states that we can use any edge to traverse
            the <, > specifies direction of the edge
            the | specifies or
            hence, it means we can use any edge in any direction to make our {num} hops
        */
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH internship {\n"
                + "start = {u};\n"
                + String.format("result = SELECT v FROM start:s -((<_|_>)*%s)- :v\n", hops)
                // without a limit >= 3 hop queries result may be up to 20000k+ lines of JSON
                + "WHERE u != v LIMIT 25;\n"
                + "PRINT result;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object> result = new ArrayList<>();
                do {
                    java.sql.ResultSetMetaData metaData = rs.getMetaData();
                    String nodeName = metaData.getCatalogName(1);
                    while (rs.next()) {
                        result.add(HandlerUtil.reconstructHandler(nodeName, rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Comment>> getCommentsByUser(String id) {
        /**
            can't initialize a set {u} for edges, it only works for nodes
            to instead get edges, we can use accumulators
            we use global accums (specified with @@) which is made available to any node/ edge traversed in the query
            here we do not care about the target vertex, so it has no type at the end, but just has an alias :t
            we want Comments_On edges only, so we specify it with a query Comments_On:e
            we then add it to our list of edges and return it after, we do this for every outgoing edge from user
        */
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH internship {\n"
                + "ListAccum<edge> @@edgeList;\n"
                + "start = {u};\n"
                + "result = SELECT t FROM start:s -(Comments_On:e)- :t ACCUM @@edgeList += e;\n"
                + "PRINT @@edgeList;\n"
                + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Comment> result = new ArrayList<>();
                do {
                    while (rs.next()) {
                        result.add(CommentService.reconstructComment(rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
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
        String queryBody = "INTERPRET QUERY () FOR GRAPH internship {\n"
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
                        result.add(HandlerUtil.reconstructHandler(nodeName, rs));
                    }
                } while (!rs.isLast());
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
