package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Genre;
import com.tigergraph.tg.model.Writer;
import com.tigergraph.tg.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class WriterService {

    @Autowired
    private WriterRepository writerRepository;

    public static Writer reconstructWriter(java.sql.ResultSet rs) throws SQLException {
        return new Writer(rs.getString(1));
    }
}
