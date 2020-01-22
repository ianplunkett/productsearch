package com.ianplunkett.productsearch.Controller;

import com.ianplunkett.productsearch.Model.ProductConstants;
import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Model.QueryModel;
import com.ianplunkett.productsearch.Model.QueryModelBuilder;
import com.ianplunkett.productsearch.Service.SearchService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SearchController {
    private final SearchService searchService;
    private final QueryModelBuilder queryModelBuilder;

    public SearchController(SearchService searchService, QueryModelBuilder queryModelBuilder) {
        this.searchService = searchService;
        this.queryModelBuilder = queryModelBuilder;
    }

    @GetMapping("/search")
    public ProductJSON search(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = ProductConstants.MIN_PRICE, required = false) String minPrice,
            @RequestParam(value = ProductConstants.MAX_PRICE, required = false) String maxPrice,
            @RequestParam(value = ProductConstants.MIN_REVIEW_RATING, required = false) Double minReviewRating,
            @RequestParam(value = ProductConstants.MAX_REVIEW_RATING, required = false) Double maxReviewRating,
            @RequestParam(value = ProductConstants.MIN_REVIEW_COUNT, required = false) Integer minReviewCount,
            @RequestParam(value = ProductConstants.MAX_REVIEW_COUNT, required = false) Integer maxReviewCount,
            @RequestParam(value = ProductConstants.IN_STOCK, required = false) Boolean inStock)
            throws ParseException, IOException {

        QueryModel queryModel = queryModelBuilder.setQuery(q)
                .setMinPrice(minPrice)
                .setMaxPrice(maxPrice)
                .setMinReviewRating(minReviewRating)
                .setMaxReviewRating(maxReviewRating)
                .setMinReviewCount(minReviewCount)
                .setMaxReviewCount(maxReviewCount)
                .setInStock(inStock)
                .createQueryModel();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        return searchService.search(queryModel, builder);
    }
}
