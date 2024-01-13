package com.howtographql.hakernews.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hakernews.data.SigninPayload;
import com.howtographql.hakernews.data.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload signinPayload) {
        return signinPayload.getUser();
    }
}
