package com.howtographql.hakernews;

import com.coxautodev.graphql.tools.SchemaParser;
import com.howtographql.hakernews.auth.AuthContext;
import com.howtographql.hakernews.data.Scalars;
import com.howtographql.hakernews.data.User;
import com.howtographql.hakernews.exception.SanitizedError;
import com.howtographql.hakernews.repository.LinkRepository;
import com.howtographql.hakernews.repository.UserRepository;
import com.howtographql.hakernews.repository.VoteRepository;
import com.howtographql.hakernews.resolver.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.howtographql.hakernews.util.Constant.*;

@WebServlet(urlPatterns = GRAPH_QL_URL_PATTERN)
public class GraphQLEndpoint extends SimpleGraphQLServlet {
    private static final String SCHEMA_GRAPHQLS = "schema.graphqls";
    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;
    private static final VoteRepository voteRepository;

    static {
        MongoDatabase mongo = new MongoClient().getDatabase(DATABASE_NAME);
        linkRepository = new LinkRepository(mongo.getCollection(LINKS_COLLECTION));
        userRepository = new UserRepository(mongo.getCollection(USERS_COLLECTION));
        voteRepository = new VoteRepository(mongo.getCollection(VOTES_COLLECTION));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                           .file(SCHEMA_GRAPHQLS) //parse the schema file created earlier
                           .resolvers(new Query(linkRepository, voteRepository),
                                   new Mutation(linkRepository, userRepository, voteRepository),
                                   new SigninResolver(),
                                   new LinkResolver(userRepository),
                                   new VoteResolver(userRepository, linkRepository))
                           .scalars(Scalars.dateTime)
                           .build()
                           .makeExecutableSchema();
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request.map(req -> req.getHeader(AUTHORIZATION))
                           .filter(id -> !id.isEmpty())
                           .map(id -> id.replace(BEARER, ""))
                           .map(userRepository::findById)
                           .orElse(null);
        return new AuthContext(user, request, response);
    }

    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
                     .filter(this::hasDataFetchingError)
                     .map(this::convertError)
                     .collect(Collectors.toList());
    }

    private boolean hasDataFetchingError(GraphQLError e) {
        return e instanceof ExceptionWhileDataFetching || super.isClientError(e);
    }

    private GraphQLError convertError(GraphQLError error) {
        if(error instanceof ExceptionWhileDataFetching dataFetching) {
            return new SanitizedError(dataFetching);
        }
        return error;
    }
}
