package com.howtographql.hakernews.repository;


import com.howtographql.hakernews.data.Scalars;
import com.howtographql.hakernews.data.Vote;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.howtographql.hakernews.util.Constant.*;
import static com.mongodb.client.model.Filters.eq;

public class VoteRepository {

    private final MongoCollection<Document> votes;

    public VoteRepository(MongoCollection<Document> votes) {
        this.votes = votes;
    }

    public List<Vote> getAllVotes() {
        List<Vote> allVotes = new ArrayList<>();

        for(Document doc: votes.find()) {
            allVotes.add(convertDocToVote(doc));
        }
        return allVotes;
    }

    public List<Vote> findByUserId(String userId) {
        List<Vote> list = new ArrayList<>();

        for(Document doc : votes.find(eq(USER_ID, userId))) {
            list.add(convertDocToVote(doc));
        }
        return list;
    }

    public List<Vote> findByLinkId(String linkId) {
        List<Vote> list = new ArrayList<>();

        for (Document doc : votes.find(eq(LINK_ID, linkId))) {
            list.add(convertDocToVote(doc));
        }
        return list;
    }

    public Vote saveVote(Vote vote) {
        Document doc = new Document();
        doc.append(USER_ID, vote.getUserId());
        doc.append(LINK_ID, vote.getLinkId());
        doc.append(CREATED_AT, Scalars.dateTime.getCoercing().serialize(vote.getCreatedAt()));
        votes.insertOne(doc);
        return new Vote(
                doc.get(ID).toString(),
                vote.getCreatedAt(),
                vote.getUserId(),
                vote.getLinkId());
    }

    private Vote convertDocToVote(Document doc) {
        return new Vote(
                doc.get(ID).toString(),
                ZonedDateTime.parse(doc.getString(CREATED_AT)),
                doc.getString(USER_ID),
                doc.getString(LINK_ID)
        );
    }
}
