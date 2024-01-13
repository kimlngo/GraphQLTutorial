package com.howtographql.hakernews.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hakernews.data.Link;
import com.howtographql.hakernews.data.User;
import com.howtographql.hakernews.repository.UserRepository;

public class LinkResolver implements GraphQLResolver<Link> {

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}
