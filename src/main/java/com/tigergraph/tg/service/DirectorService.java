package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Director;
import com.tigergraph.tg.repository.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    public static Director reconstructDirector(java.sql.ResultSet rs) throws SQLException {
        return new Director(rs.getString(1));
    }
}
