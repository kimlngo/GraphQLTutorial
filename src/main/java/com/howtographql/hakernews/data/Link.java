package com.howtographql.hakernews.data;

public class Link {
    private String id;
    private String url;
    private String description;
    private String userId; //posted by who

    public Link(String url, String description, String userId) {
        this(null, url, description, userId);
    }

    public Link(String id, String url, String description, String userId) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }
}
