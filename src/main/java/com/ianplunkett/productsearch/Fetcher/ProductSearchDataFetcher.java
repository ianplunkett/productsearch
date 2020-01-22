package com.ianplunkett.productsearch.Fetcher;

import com.ianplunkett.productsearch.Model.ProductConstants;
import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Model.QueryModel;
import com.ianplunkett.productsearch.Model.QueryModelBuilder;
import com.ianplunkett.productsearch.Service.SearchService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.lucene.search.BooleanQuery;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class ProductSearchDataFetcher implements DataFetcher<ProductJSON> {

    private final QueryModelBuilder queryModelBuilder;
    private final SearchService searchService;


    public ProductSearchDataFetcher(QueryModelBuilder queryModelBuilder, SearchService searchService) {
        this.queryModelBuilder = queryModelBuilder;
        this.searchService = searchService;
    }

    @Override
    public ProductJSON get(DataFetchingEnvironment environment) throws Exception {
        LinkedHashMap<String,?> searchParams = environment.getArgument("searchParams");

        QueryModel queryModel = queryModelBuilder.setQuery((String) searchParams.get(ProductConstants.QUERY))
                .setMinPrice((String) searchParams.get(ProductConstants.MIN_PRICE))
                .setMaxPrice((String) searchParams.get(ProductConstants.MAX_PRICE))
                .setMinReviewRating((Double) searchParams.get(ProductConstants.MIN_REVIEW_RATING))
                .setMaxReviewRating((Double) searchParams.get(ProductConstants.MAX_REVIEW_RATING))
                .setMinReviewCount((Integer) searchParams.get(ProductConstants.MIN_REVIEW_COUNT))
                .setMaxReviewCount((Integer) searchParams.get(ProductConstants.MAX_REVIEW_COUNT))
                .setInStock((Boolean) searchParams.get(ProductConstants.IN_STOCK))
                .createQueryModel();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        return searchService.search(queryModel, builder);
    }
}
