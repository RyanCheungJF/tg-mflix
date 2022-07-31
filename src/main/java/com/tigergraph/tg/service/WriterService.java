package com.tigergraph.tg.service;

import com.tigergraph.tg.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WriterService {

    @Autowired
    private WriterRepository writerRepository;

    public WriterService(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }
}
