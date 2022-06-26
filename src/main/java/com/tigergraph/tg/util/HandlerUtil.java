package com.tigergraph.tg.util;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HandlerUtil {

    public static Object reconstructHandler(String className, ResultSet rs) throws SQLException {
        if (className.equals("User")) {
            return (UserUtil.reconstructUser(rs));
        } else if (className.equals("Movie")) {
            return (MovieUtil.reconstructMovie(rs));
        } else if (className.equals("Cast")) {
            return (CastUtil.reconstructCast(rs));
        } else if (className.equals("Director")) {
            return (DirectorUtil.reconstructDirector(rs));
        } else if (className.equals("Genre")) {
            return (GenreUtil.reconstructGenre(rs));
        } else if (className.equals("Writer")) {
            return (WriterUtil.reconstructWriter(rs));
        } else if (className.equals("Imdb")) {
            return (ImdbUtil.reconstructImdb(rs));
        } else if (className.equals("Comments_On")) {
            return (CommentUtil.reconstructComment(rs));
        } else {
            throw new SQLException("Fits neither of the classes");
        }
    }
}
