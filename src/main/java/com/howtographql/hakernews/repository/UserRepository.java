package com.howtographql.hakernews.repository;

import com.howtographql.hakernews.data.User;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.howtographql.hakernews.util.Constant.*;
import static com.mongodb.client.model.Filters.eq;

public class UserRepository {

    private final MongoCollection<Document> users;

    public UserRepository(MongoCollection<Document> users) {
        this.users = users;
    }

    public User findByEmail(String email) {
        Document doc = users.find(eq(EMAIL, email))
                            .first();
        return user(doc);
    }

    public User findById(String id) {
        Document doc = users.find(eq(ID, new ObjectId(id)))
                            .first();
        return user(doc);
    }

    public User saveUser(User user) {
        Document doc = new Document();
        doc.append(NAME, user.getName());
        doc.append(EMAIL, user.getEmail());
        doc.append(PASSWORD, user.getPassword());
        users.insertOne(doc);
        return new User(doc.get(ID).toString(),
                doc.get(NAME).toString(),
                doc.get(EMAIL).toString(),
                doc.get(PASSWORD).toString());
    }

    private User user(Document doc) {
        if (doc == null) {
            return null;
        }
        return new User(
                doc.get(ID).toString(),
                doc.getString(NAME),
                doc.getString(EMAIL),
                doc.getString(PASSWORD));
    }
}
