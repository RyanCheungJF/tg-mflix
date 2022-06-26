package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Cast;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CastRepository implements TigerGraphRepository<Cast, String> {

    private final String INTERPRETED = "run interpreted(u=?)";
    private final StatementUtil stmtUtil;

    public CastRepository(StatementUtil stmtUtil) {
        this.stmtUtil = stmtUtil;
    }

    // wip, still debugging
    public Optional<List<Object>> getNearbyCastMembers(String id) {
        String queryBody = "INTERPRET QUERY (VERTEX<Cast> u) FOR GRAPH internship SYNTAX v1 {\n"
            + "  MinAccum<INT> @min_dis;\n"
            + "  OrAccum @or_visited;\n"
            + "  ListAccum<VERTEX> @path_list;\n"
            + "  SetAccum<EDGE> @@edge_set;\n"
            + "  Source (ANY) = {u};\n"
            + "  Source = SELECT s FROM Source:s ACCUM s.@or_visited += true, s.@min_dis = 0, s.@path_list = s;\n"
            + "  ResultSet (ANY) = {u};\n"
            + "  WHILE(Source.size() > 0) DO\n"
            + "      Source = SELECT t FROM Source:s -(_:e)- _:t WHERE t.@or_visited == false\n"
            + "         ACCUM t.@min_dis += s.@min_dis + 1, t.@path_list = s.@path_list + [t], t.@or_visited += true;\n"
            + "      ResultSet = ResultSet UNION Source;\n"
            + "  END;\n"
            + "  ResultSet = SELECT s FROM ResultSet:s LIMIT 100;\n"
            + "  PRINT ResultSet[ResultSet.@min_dis, ResultSet.@path_list];\n"
            /** to get set of edges used (though seems to be for another query)
            + "  ResultSet = SELECT s FROM ResultSet:s -(_:e)- _:t ACCUM @@edge_set += e;\n"
            + "  PRINT @@edge_set;\n"
             */
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, "Musidora");
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object> result = new ArrayList<>();
                do {
                    java.sql.ResultSetMetaData metaData = rs.getMetaData();
                    System.out.println("Table: " + metaData.getCatalogName(1));
                    System.out.print(metaData.getColumnName(1));
                    for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                        System.out.print("\t" + metaData.getColumnName(i));
                    }
                    System.out.println("");
                    while (rs.next()) {
                        System.out.print(rs.getObject(1));
                        for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                            Object obj = rs.getObject(i);
                            System.out.print("\t" + String.valueOf(obj));
                        }
                        System.out.println("");
                    }
                    if (metaData.getCatalogName(1).equals("Cast")) {
                        System.out.println(rs.getInt("ResultSet.@min_dis"));
                        System.out.println(rs.getObject("ResultSet.@path_list"));
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
