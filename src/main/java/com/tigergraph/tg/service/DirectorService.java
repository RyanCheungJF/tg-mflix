package com.tigergraph.tg.service;

import com.tigergraph.tg.repository.DirectorRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }
}
