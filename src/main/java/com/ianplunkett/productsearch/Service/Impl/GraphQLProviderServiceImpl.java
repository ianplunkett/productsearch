package com.ianplunkett.productsearch.Service.Impl;

import com.ianplunkett.productsearch.Fetcher.ProductSearchDataFetcher;
import com.ianplunkett.productsearch.Service.GraphQLProviderService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


@Component
public class GraphQLProviderServiceImpl implements GraphQLProviderService {

    private GraphQL graphQL;

    private final ProductSearchDataFetcher productSearchDataFetcher;

    public GraphQLProviderServiceImpl(ProductSearchDataFetcher productSearchDataFetcher) {
        this.productSearchDataFetcher = productSearchDataFetcher;
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @Override
    @PostConstruct
    public void init() throws IOException {
        String schema = "schema.graphql";
        InputStream inputStream = GraphQLProviderServiceImpl.class.getClassLoader().getResourceAsStream(schema);
        assert inputStream != null;
        String sdl = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("getProductSearch", productSearchDataFetcher))
                .build();
    }

}
