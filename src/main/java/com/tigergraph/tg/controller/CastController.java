package com.tigergraph.tg.controller;

import com.tigergraph.tg.service.CastService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CastController {

    private final CastService castService;

    public CastController(CastService castService) {
        this.castService = castService;
    }

    @GetMapping(value = "/cast/neighbours", params = "id")
    public List<Object> getNearbyCastMembers(@RequestParam String id) {
        return castService.getNearbyCastMembers(id);
    }
}
