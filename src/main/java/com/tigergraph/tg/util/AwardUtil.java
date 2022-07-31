package com.tigergraph.tg.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigergraph.tg.model.vertex.Award;
import com.tigergraph.tg.model.vertex.Movie;
import com.tigergraph.tg.service.AwardService;

import java.sql.SQLException;

public class AwardUtil {

    /**
     * Deserializes the nested object of awards modelled as an attribute in movie
     *
     * @param m movie being passed in to be deserialized
     * @return award object
     * @throws SQLException if unable to deserialize
     */
    public static Award reconstructAward(Movie m) throws SQLException {
        try {
            // only movie objects are passed in
            String json = m.getAwards();
            return new ObjectMapper().readerFor(Award.class).readValue(AwardService.processJson(json));
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }
}
