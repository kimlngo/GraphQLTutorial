package com.howtographql.hakernews.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hakernews.data.Link;
import com.howtographql.hakernews.data.User;
import com.howtographql.hakernews.data.Vote;
import com.howtographql.hakernews.repository.LinkRepository;
import com.howtographql.hakernews.repository.UserRepository;

public class VoteResolver implements GraphQLResolver<Vote> {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    public VoteResolver(UserRepository userRepository, LinkRepository linkRepository) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
    }

    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }

    public Link link(Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }
}
