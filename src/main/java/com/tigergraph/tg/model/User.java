package com.tigergraph.tg.model;

import java.sql.SQLException;

public class User {

    private final String type;
    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public User(String id, String password, String name, String email) {
        this.type = "user";
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
