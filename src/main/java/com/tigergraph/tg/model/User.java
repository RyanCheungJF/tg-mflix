package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private final String type;
    private final String id;
    private final String name;
    private final String email;
    private final String password;

    public User(String id, String name, String email, String password) {
        this.type = "user";
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
