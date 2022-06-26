package com.tigergraph.tg.service;

import com.tigergraph.tg.repository.CastRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CastService {

    private final CastRepository castRepository;

    public CastService(CastRepository castRepository) {
        this.castRepository = castRepository;
    }

    public List<Object> getNearbyCastMembers(String id) {
        return castRepository.getNearbyCastMembers(id).orElse(null);
    }
}
