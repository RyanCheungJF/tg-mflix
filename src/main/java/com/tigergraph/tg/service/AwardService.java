package com.tigergraph.tg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.repository.AwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

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

    public static Award reconstructAward(Movie m) throws SQLException {
        try {
            // only movie objects are passed in
            String json = m.getAwards();
            return new ObjectMapper().readerFor(Award.class).readValue(AwardService.processJson(json));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
