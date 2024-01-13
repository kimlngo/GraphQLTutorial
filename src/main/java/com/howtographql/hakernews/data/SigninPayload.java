package com.howtographql.hakernews.data;

public class SigninPayload {
    private final String token; //simply userId
    private final User user;

    public SigninPayload(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
