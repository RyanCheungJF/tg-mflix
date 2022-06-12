package com.tigergraph.tg.util;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.service.*;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HandlerUtil {

    public static Object reconstructHandler(String className, ResultSet rs) throws SQLException {
        if (className.equals("User")) {
            return (UserService.reconstructUser(rs));
        } else if (className.equals("Movie")) {
            return (MovieService.reconstructMovie(rs));
        } else if (className.equals("Cast")) {
            return (CastService.reconstructCast(rs));
        } else if (className.equals("Director")) {
            return (DirectorService.reconstructDirector(rs));
        } else if (className.equals("Genre")) {
            return (GenreService.reconstructGenre(rs));
        } else if (className.equals("Writer")) {
            return (WriterService.reconstructWriter(rs));
        } else if (className.equals("Imdb")) {
            return (ImdbService.reconstructImdb(rs));
        } else if (className.equals("Comments_On")) {
            return (CommentService.reconstructComment(rs));
        } else {
            throw new SQLException("Fits neither of the classes");
        }
    }
}
