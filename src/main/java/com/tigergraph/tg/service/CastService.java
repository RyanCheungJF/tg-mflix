package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Cast;
import com.tigergraph.tg.repository.CastRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CastService {

    @Autowired
    private CastRespository castRespository;

    public static Cast reconstructCast(java.sql.ResultSet rs) throws SQLException {
        return new Cast(rs.getString(1));
    }
}
