package com.howtographql.hakernews.resolver;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hakernews.auth.AuthContext;
import com.howtographql.hakernews.auth.AuthData;
import com.howtographql.hakernews.data.Link;
import com.howtographql.hakernews.data.SigninPayload;
import com.howtographql.hakernews.data.User;
import com.howtographql.hakernews.data.Vote;
import com.howtographql.hakernews.repository.LinkRepository;
import com.howtographql.hakernews.repository.UserRepository;
import com.howtographql.hakernews.repository.VoteRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Mutation implements GraphQLRootResolver {
    private static final String INVALID_CREDENTIALS = "Invalid credentials";
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Link createLink(String url, String description, DataFetchingEnvironment environment) {
        AuthContext authContext = environment.getContext();
        Link newLink = new Link(url, description, authContext.getUser().getId());
        linkRepository.saveLinks(newLink);
        return newLink;
    }

    public User createUser(String name, AuthData auth) {
        User newUser = new User(name, auth.getEmail(), auth.getPassword());
        return userRepository.saveUser(newUser);
    }

    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user.getPassword().equals(auth.getPassword())) {
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException(INVALID_CREDENTIALS);
    }

    public Vote createVote(String linkId, String userId) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        return voteRepository.saveVote(new Vote(now, userId, linkId));
    }
}
