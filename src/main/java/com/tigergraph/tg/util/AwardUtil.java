package com.tigergraph.tg.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.service.AwardService;

import java.sql.SQLException;

public class AwardUtil {

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
