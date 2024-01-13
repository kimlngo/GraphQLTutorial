package com.howtographql.hakernews.resolver;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hakernews.data.Link;
import com.howtographql.hakernews.data.Vote;
import com.howtographql.hakernews.filter.LinkFilter;
import com.howtographql.hakernews.repository.LinkRepository;
import com.howtographql.hakernews.repository.VoteRepository;

import java.util.List;

public class Query implements GraphQLRootResolver {
    private final LinkRepository linkRepository;
    private final VoteRepository voteRepository;

    public Query(LinkRepository linkRepository, VoteRepository voteRepository) {
        this.linkRepository = linkRepository;
        this.voteRepository = voteRepository;
    }

    public List<Link> allLinks(LinkFilter filter, Number skip, Number first) {
        return linkRepository.getAllLinks(filter, skip.intValue(), first.intValue());
    }

    public List<Vote> allVotes() {
        return voteRepository.getAllVotes();
    }
}
