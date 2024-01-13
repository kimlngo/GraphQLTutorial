package com.howtographql.hakernews.data;

import java.time.ZonedDateTime;

public class Vote {

    private String id;
    private ZonedDateTime createdAt;
    private String userId;
    private String linkId;

    public Vote(ZonedDateTime createdAt, String userId, String linkId) {
        this(null, createdAt, userId, linkId);
    }

    public Vote(String id, ZonedDateTime createdAt, String userId, String linkId) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.linkId = linkId;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getLinkId() {
        return linkId;
    }
}
