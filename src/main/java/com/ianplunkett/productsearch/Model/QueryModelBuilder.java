package com.ianplunkett.productsearch.Model;

import org.springframework.stereotype.Component;

@Component
public class QueryModelBuilder {
    private String q;
    private String minPrice;
    private String maxPrice;
    private Double minReviewRating;
    private Double maxReviewRating;
    private Integer minReviewCount;
    private Integer maxReviewCount;
    private Boolean inStock;

    public QueryModelBuilder setQ(String q) {
        this.q = q;
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
        return new QueryModel(q, minPrice, maxPrice, minReviewRating, maxReviewRating, minReviewCount, maxReviewCount, inStock);
    }
}
