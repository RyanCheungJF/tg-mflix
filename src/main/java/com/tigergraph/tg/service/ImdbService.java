package com.tigergraph.tg.service;

import com.tigergraph.tg.repository.ImdbRepository;
import org.springframework.stereotype.Service;

@Service
public class ImdbService {

    private final ImdbRepository imdbRepository;

    public ImdbService(ImdbRepository imdbRepository) {
        this.imdbRepository = imdbRepository;
    }
}
