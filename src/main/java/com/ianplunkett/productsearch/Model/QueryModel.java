package com.ianplunkett.productsearch.Model;

public class QueryModel {
    private String q;
    private String minPrice;
    private String maxPrice;
    private Double minReviewRating;
    private Double maxReviewRating;
    private Integer minReviewCount;
    private Integer maxReviewCount;
    private Boolean inStock;

    public QueryModel(String q, String minPrice, String maxPrice, Double minReviewRating, Double maxReviewRating, Integer minReviewCount, Integer maxReviewCount, Boolean inStock) {
        this.q = q;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minReviewRating = minReviewRating;
        this.maxReviewRating = maxReviewRating;
        this.minReviewCount = minReviewCount;
        this.maxReviewCount = maxReviewCount;
        this.inStock = inStock;
    }

    public String getQ() {
        return q;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public Double getMinReviewRating() {
        return minReviewRating;
    }

    public Double getMaxReviewRating() {
        return maxReviewRating;
    }

    public Integer getMinReviewCount() {
        return minReviewCount;
    }

    public Integer getMaxReviewCount() {
        return maxReviewCount;
    }

    public Boolean getInStock() {
        return inStock;
    }
}
