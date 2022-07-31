package com.tigergraph.tg.service;

import com.tigergraph.tg.model.requestbody.AllLinksRequestBody;
import com.tigergraph.tg.model.requestbody.EntityType;
import com.tigergraph.tg.repository.CastRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CastService {

    private final CastRepository castRepository;

    public CastService(CastRepository castRepository) {
        this.castRepository = castRepository;
    }

    public List<List<Object>> getAllPathsFromASourceSetToTargetSet(AllLinksRequestBody jsonPayload) {
        return castRepository.getAllPathsFromASourceSetToTargetSet(jsonPayload).orElse(null);
    }

    public List<List<Object>> getAllLinksForCasts(Integer pathLength) {
        return castRepository.getAllLinksForCasts(pathLength, new ArrayList<>()).orElse(null);
    }

    public List<List<Object>> getAllLinksForCastsTraverseMovie(Integer pathLength) {
        ArrayList<EntityType> filter = new ArrayList<>();
        filter.add(new EntityType("Movie"));
        filter.add(new EntityType("Cast"));
        return castRepository.getAllLinksForCasts(pathLength, filter).orElse(null);
    }

    public List<List<Object>> getAllLinksWithTomatoMeter(Integer score, Integer pathLength) {
        return castRepository.getAllLinksWithTomatoMeter(score, pathLength).orElse(null);
    }

    public List<List<Object>> getAllLinksWithAwardNumber(Integer awards, Integer pathLength) {
        return castRepository.getAllLinksWithAwardNumber(awards, pathLength).orElse(null);
    }

    public List<List<Object>> getAllLinksWithImdbRatingNumber(Double rating, Integer pathLength) {
        return castRepository.getAllLinksWithImdbRatingNumber(rating, pathLength).orElse(null);
    }

    public List<List<Object>> getAllIndirectLinksWithAwardNumber(Integer awards, AllLinksRequestBody jsonPayload) {
        return castRepository.getAllIndirectLinksWithAwardNumber(awards, jsonPayload).orElse(null);
    }

    public List<List<Object>> getLinkBetweenTwoCastsTraverseMovie(String src, String tgt, Integer pathLength) {
        return castRepository.getLinkBetweenTwoCastsTraverseMovie(src, tgt, pathLength).orElse(null);
    }

    public List<List<Object>> getDirectLinkBetweenTwoCastsTraverseMovie(String src, String tgt) {
        return castRepository.getDirectLinkBetweenTwoCastsTraverseMovie(src, tgt).orElse(null);
    }

    public List<List<Object>> getLinkBetweenCastAndMovieTraverseMovie(String src, String tgt, Integer pathLength) {
        return castRepository.getLinkBetweenCastAndMovieTraverseMovie(src, tgt, pathLength).orElse(null);
    }
}
