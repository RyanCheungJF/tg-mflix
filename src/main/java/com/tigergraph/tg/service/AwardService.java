package com.tigergraph.tg.service;

import com.tigergraph.tg.model.vertex.Award;
import com.tigergraph.tg.model.vertex.Movie;
import com.tigergraph.tg.repository.AwardRepository;
import com.tigergraph.tg.util.AwardUtil;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class AwardService {

    private final AwardRepository awardRepository;

    public AwardService(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }

    /**
     * Gets rid of single quotations and replaces them with double
     *
     * @param s json to clean
     * @return string in json form to be read in by jackson
     */
    public static String processJson(String s) {
        String trim = s.substring(1, s.length() - 1);
        return trim.replace('\'', '"');
    }

    public static Optional<Award> extractAwardFromMovie(Movie movie) {
        // some movies do not have awards -> are marked with 'NA' as a default value
        if (movie.equals(null) || movie.getAwards().equals("NA")) {
            return Optional.empty();
        }
        try {
            return Optional.of(AwardUtil.reconstructAward(movie));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
