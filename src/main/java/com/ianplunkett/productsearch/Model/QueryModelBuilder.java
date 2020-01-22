package com.ianplunkett.productsearch.Model;

import org.springframework.stereotype.Component;

@Component
public class QueryModelBuilder {
    private String query;
    private String minPrice;
    private String maxPrice;
    private Double minReviewRating;
    private Double maxReviewRating;
    private Integer minReviewCount;
    private Integer maxReviewCount;
    private Boolean inStock;

    public QueryModelBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    public QueryModelBuilder setMinPrice(String minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public QueryModelBuilder setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public QueryModelBuilder setMinReviewRating(Double minReviewRating) {
        this.minReviewRating = minReviewRating;
        return this;
    }

    public QueryModelBuilder setMaxReviewRating(Double maxReviewRating) {
        this.maxReviewRating = maxReviewRating;
        return this;
    }

    public QueryModelBuilder setMinReviewCount(Integer minReviewCount) {
        this.minReviewCount = minReviewCount;
        return this;
    }

    public QueryModelBuilder setMaxReviewCount(Integer maxReviewCount) {
        this.maxReviewCount = maxReviewCount;
        return this;
    }

    public QueryModelBuilder setInStock(Boolean inStock) {
        this.inStock = inStock;
        return this;
    }

    public QueryModel createQueryModel() {
        return new QueryModel(query, minPrice, maxPrice, minReviewRating, maxReviewRating, minReviewCount, maxReviewCount, inStock);
    }
}
