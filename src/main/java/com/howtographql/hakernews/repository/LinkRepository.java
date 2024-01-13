package com.howtographql.hakernews.repository;

import com.howtographql.hakernews.data.Link;
import com.howtographql.hakernews.filter.LinkFilter;
import com.howtographql.hakernews.util.Constant;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.howtographql.hakernews.util.Constant.*;
import static com.mongodb.client.model.Filters.*;

public class LinkRepository {
    private final MongoCollection<Document> links;

    public LinkRepository(MongoCollection<Document> links) {
        this.links = links;
    }

    public Link findById(String id) {
        Document doc = links.find(eq(ID, new ObjectId(id))).first();
        return convertDocToLink(doc);
    }

    public List<Link> getAllLinks(LinkFilter linkFilter, int skip, int first) {
        Optional<Bson> mongoFilter = Optional.ofNullable(linkFilter).map(this::buildFilter);

        List<Link> allLinks = new ArrayList<>();
        FindIterable<Document> documents = mongoFilter.map(links::find)
                                                      .orElseGet(links::find);
        for(Document doc : documents.skip(skip).limit(first)) {
            allLinks.add(convertDocToLink(doc));
        }
        return allLinks;
    }

    private Bson buildFilter(LinkFilter linkFilter) {
        String descriptionPatter = linkFilter.getDescriptionContains();
        String urlPattern = linkFilter.getUrlContains();

        Bson descriptionCondition = null, urlCondition = null;

        if (descriptionPatter != null && !descriptionPatter.isEmpty()) {
            descriptionCondition = regex(DESCRIPTION, ".*" + descriptionPatter + ".*", "i");
        }

        if (urlPattern != null && !urlPattern.isEmpty()) {
            urlCondition = regex(URL, ".*" + urlPattern + ".*", "i");
        }

        if (descriptionCondition != null && urlCondition != null) {
            return and(descriptionCondition, urlCondition);
        }
        return descriptionCondition != null ? descriptionCondition : urlCondition;
    }

    public void saveLinks(Link link) {
        Document doc = new Document();
        doc.append(Constant.URL, link.getUrl());
        doc.append(Constant.DESCRIPTION, link.getDescription());
        doc.append(POSTED_BY, link.getUserId());
        links.insertOne(doc);
    }

    private Link convertDocToLink(Document doc) {
        return new Link(
                doc.get(ID).toString(),
                doc.getString(Constant.URL),
                doc.getString(Constant.DESCRIPTION),
                doc.getString(POSTED_BY));
    }
}
