package com.tigergraph.tg.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigergraph.tg.model.edge.Casts;
import com.tigergraph.tg.model.edge.CategorizedAs;
import com.tigergraph.tg.model.edge.CommentsOn;
import com.tigergraph.tg.model.edge.DirectedBy;
import com.tigergraph.tg.model.edge.RatedAs;
import com.tigergraph.tg.model.edge.WrittenBy;
import com.tigergraph.tg.model.requestbody.AllLinksRequestBody;
import com.tigergraph.tg.model.requestbody.EntityTypeWithFilter;
import com.tigergraph.tg.model.requestbody.EntityTypeWithPrimaryKey;
import com.tigergraph.tg.model.vertex.Cast;
import com.tigergraph.tg.model.vertex.Director;
import com.tigergraph.tg.model.vertex.Genre;
import com.tigergraph.tg.model.vertex.Imdb;
import com.tigergraph.tg.model.vertex.Movie;
import com.tigergraph.tg.model.vertex.User;
import com.tigergraph.tg.model.vertex.Writer;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class HandlerUtil {

    /**
     * Returns an object based on the type of the class from a result set object
     *
     * @param className name of the class to construct
     * @param rs result set object obtained from query
     * @return an object matching the class name with attributes based on the rs object
     * @throws SQLException if unable to get an attribute of the result set object
     */
    public static Object reconstructFromResultSetHandler(String className, ResultSet rs) throws SQLException {
        switch (className) {
            case "User":
                return (UserUtil.reconstructUser(rs));
            case "Movie":
                return (MovieUtil.reconstructMovie(rs));
            case "Cast":
                return (CastUtil.reconstructCast(rs));
            case "Director":
                return (DirectorUtil.reconstructDirector(rs));
            case "Genre":
                return (GenreUtil.reconstructGenre(rs));
            case "Writer":
                return (WriterUtil.reconstructWriter(rs));
            case "Imdb":
                return (ImdbUtil.reconstructImdb(rs));
            case "Comments_On":
                return (CommentUtil.reconstructComment(rs));
            default:
                throw new SQLException("Fits neither of the classes");
        }
    }

    /**
     * Returns an object based on the type of the node class from a json object
     *
     * @param className name of the node class to construct
     * @param hm json object obtained from a query
     * @return an object matching the class name with attributes based on the json object
     * @throws JSONException if unable to get an attribute on the json object
     */
    public static Object reconstructNodeFromMapHandler(String className, JSONObject hm) throws JSONException {
        switch (className) {
            case "User":
                return new User(hm.getString("id"), hm.getString("name"), hm.getString("email"),
                    hm.getString("password"));
            case "Movie":
                return new Movie(hm.getString("id"), hm.getString("plot"), hm.getInt("num_mflix_comments"),
                    hm.getString("title"), hm.getString("awards"), hm.getString("lastUpdated"),
                    hm.getInt("tomatoes_meter"), hm.getDouble("tomatoes_rating"), hm.getInt("tomatoes_numReviews"));
            case "Cast":
                return new Cast(hm.getString("name"));
            case "Director":
                return new Director(hm.getString("name"));
            case "Genre":
                return new Genre(hm.getString("name"));
            case "Writer":
                return new Writer(hm.getString("name"));
            case "Imdb":
                return new Imdb(hm.getInt("id"), hm.getDouble("rating"), hm.getInt("votes"));
            default:
                throw new JSONException("Could not find an attribute on the json object");
        }
    }

    /**
     * Returns an object based on the type of the edge class from a json object
     *
     * @param className name of the edge class to construct
     * @param hm json object obtained from a query
     * @return an object matching the class name with attributes based on the json object
     * @throws JSONException if unable to get an attribute on the json object
     */
    public static Object reconstructEdgeFromMapHandler(String className, String from, String to, JSONObject hm)
        throws JSONException {
        switch (className) {
            case "Comments_On":
                return new CommentsOn(true, from, to, hm.getString("id"), hm.getString("email"),
                    hm.getString("movie_id"), hm.getString("text"));
            case "reverse_Comments_On":
                return new CommentsOn(false, from, to, hm.getString("id"), hm.getString("email"),
                    hm.getString("movie_id"), hm.getString("text"));
            case "Categorized_As":
                return new CategorizedAs(true, from, to);
            case "reverse_Categorized_As":
                return new CategorizedAs(false, from, to);
            case "Casts":
                return new Casts(true, from, to);
            case "reverse_Casts":
                return new Casts(false, from, to);
            case "Directed_By":
                return new DirectedBy(true, from, to);
            case "reverse_Directed_By":
                return new DirectedBy(false, from, to);
            case "Rated_As":
                return new RatedAs(true, from, to);
            case "reverse_Rated_As":
                return new RatedAs(false, from, to);
            case "Written_By":
                return new WrittenBy(true, from, to);
            case "reverse_Written_By":
                return new WrittenBy(false, from, to);
            default:
                throw new JSONException("Could not find an attribute on the json object");
        }
    }

    /**
     * Returns the connection object used to make a curl request
     *
     * @param method of request
     * @param payload json body attached to request
     * @param endpoint of request
     * @return connection object used to make requests
     * @throws IOException if failed to make a connection
     */
    public static HttpURLConnection makeCurlRequest(String method, String payload, String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("GSQL-TIMEOUT", "60000");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        byte[] input = payload.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
        return conn;
    }

    /**
     * Returns the result of making the curl request
     *
     * @param conn connection object used to make requests
     * @return json formatted as string representing the result of the request
     * @throws IOException if unable to access connection
     */
    public static String handleCurlResponse(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    /**
     * Reads the JSON formatted as string, and processes it to return the objects present
     *
     * @param res response from making the curl request
     * @return a list of nodes put into an array of objects
     * @throws JSONException if any of the fields are missing
     */
    public static List<List<Object>> deconstructResultFromCurlOld(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        try {
            JSONObject resultSet = jsonObject.getJSONArray("results").getJSONObject(0);
            JSONArray vertexSet = resultSet.getJSONArray("vertices");
            JSONArray edgeSet = resultSet.getJSONArray("edges");
            ArrayList<Object> vertexArr = new ArrayList<>();
            ArrayList<Object> edgeArr = new ArrayList<>();
            for (int i = 0; i < vertexSet.length(); i++) {
                vertexArr.add(HandlerUtil.reconstructNodeFromMapHandler(vertexSet.getJSONObject(i).getString("v_type"),
                    vertexSet.getJSONObject(i).getJSONObject("attributes")));
            }
            for (int i = 0; i < edgeSet.length(); i++) {
                JSONObject objInstance = edgeSet.getJSONObject(i);
                edgeArr.add(HandlerUtil.reconstructEdgeFromMapHandler(objInstance.getString("e_type"),
                    objInstance.getString("from_id"), objInstance.getString("to_id"),
                    objInstance.getJSONObject("attributes")));
            }
            List<List<Object>> arr = new ArrayList<>();
            arr.add(vertexArr);
            arr.add(edgeArr);
            return arr;
        } catch (JSONException err) {
            err.printStackTrace();
            throw new JSONException(jsonObject.getString("message"));
        }
    }

    /**
     * Removes extra bytes in result obtained from requests, so that it can be used for other requests
     *
     * @param input byte array obtained from request
     * @return cleaned byte array without extra bytes
     */
    public static byte[] cleanByteArray(byte[] input) {
        ArrayList<Byte> arr = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            int b = input[i];
            // accented characters often add in both of these byte numbers
            if (b != -62 && b != -125) {
                arr.add(input[i]);
            }
        }
        byte[] res = new byte[arr.size()];
        for (int i = 0; i < arr.size(); i ++) {
            res[i] = arr.get(i).byteValue();
        }
        return res;
    }

    /**
     * Checks if the input has an empty source
     *
     * @param jsonPayload request body containing information such as source and target
     */
    public static void checkInputBody(AllLinksRequestBody jsonPayload) {
        if (jsonPayload.getSources().size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns paths based on the parameters specified in the request body, passes them to a curl request
     *
     * @param jsonPayload request body containing information such as source and target
     * @return a list of nodes and edges based on all paths from source to target
     */
    public static Optional<List<List<Object>>> makeAllPathsCurlRequest(AllLinksRequestBody jsonPayload,
                                                                       HashSet<String> hs) {
        try {
            checkInputBody(jsonPayload);
            return Optional.of(deconstructResultFromCurl(handleCurlResponse(makeCurlRequest("POST",
                new ObjectMapper().writeValueAsString(jsonPayload),
                    "http://localhost:9000/allpaths/mflix")), hs, jsonPayload.getMaxLength()));
        } catch (JSONException | IOException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Reads the JSON formatted as string, and processes it to return the objects present
     *
     * @param res response from making the curl request
     * @param hs hashset of nodes that a path must contain
     * @param maxLength maximum length of all paths found
     * @return a list of nodes put into an array of objects
     * @throws JSONException if any of the fields are missing
     */
    public static List<List<Object>> deconstructResultFromCurl(String res, HashSet<String> hs, Integer maxLength)
        throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        try {
            JSONObject resultSet = jsonObject.getJSONArray("results").getJSONObject(0);
            JSONArray vertexSet = resultSet.getJSONArray("vertices");
            JSONArray edgeSet = resultSet.getJSONArray("edges");
            ArrayList<Object> vertexArr = new ArrayList<>();
            ArrayList<Object> edgeArr = new ArrayList<>();
            for (int i = 0; i < vertexSet.length(); i++) {
                hs.remove(vertexSet.getJSONObject(i).getString("v_id"));
                vertexArr.add(HandlerUtil.reconstructNodeFromMapHandler(vertexSet.getJSONObject(i).getString("v_type"),
                    vertexSet.getJSONObject(i).getJSONObject("attributes")));
            }
            for (int i = 0; i < edgeSet.length(); i++) {
                JSONObject objInstance = edgeSet.getJSONObject(i);
                edgeArr.add(HandlerUtil.reconstructEdgeFromMapHandler(objInstance.getString("e_type"),
                    objInstance.getString("from_id"), objInstance.getString("to_id"),
                    objInstance.getJSONObject("attributes")));
            }
            List<List<Object>> arr = new ArrayList<>();
            if (hs.isEmpty() && vertexArr.size() <= maxLength + 1) {
                arr.add(vertexArr);
                arr.add(edgeArr);
            }
            return arr;
        } catch (JSONException err) {
            err.printStackTrace();
            throw new JSONException(jsonObject.getString("message"));
        }
    }

    /**
     * Returns paths through this helper function by iterating combinations between a single source list
     *
     * @param srcList list of nodes to find the paths between
     * @param nodeFilter particular filter on the type of nodes to be traversed
     * @param maxLength maximum length of all paths found
     * @return a list of nodes and edges based on all paths from source to target
     */
    public static List<List<Object>> iterateSourcesToFindPaths(ArrayList<EntityTypeWithPrimaryKey> srcList,
                                                               ArrayList<? super EntityTypeWithFilter> nodeFilter,
                                                               Integer maxLength) {
        // after accumulating all nodes, we then formulate a request body
        List<List<Object>> res = new ArrayList<>();;
        ArrayList<String> arrString = new ArrayList<>();
        for (EntityTypeWithPrimaryKey entityTypeWithPrimaryKey : srcList) {
            arrString.add(entityTypeWithPrimaryKey.getId().toString());
        }
        HashSet<String> hs = new HashSet<>(arrString);
        // since we want all links in between cast members, we do a for loop for each node to find all paths
        for (int i = 0; i < srcList.size(); i++) {
            for (int j = i + 1; j < srcList.size(); j++) {
                ArrayList<EntityTypeWithPrimaryKey> src = new ArrayList<>();
                ArrayList<EntityTypeWithPrimaryKey> tgt = new ArrayList<>();
                src.add(srcList.get(i));
                tgt.add(srcList.get(j));
                AllLinksRequestBody newPayload = new AllLinksRequestBody(src, tgt, nodeFilter, new ArrayList<>(), maxLength);
                // we then send a request for each node
                List<List<Object>> tempRes = makeAllPathsCurlRequest(newPayload, hs).orElse(new ArrayList<>());
                if (tempRes.size() != 0 && tempRes.get(0).size() != 0) {
                    res.add(tempRes.get(0));
                }
                if (tempRes.size() != 0 && tempRes.get(1).size() != 0) {
                    res.add(tempRes.get(1));
                }
            }
        }
        return res;
    }
}
