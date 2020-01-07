package com.ianplunkett.productsearch.Model;

import com.ianplunkett.productsearch.Util.MoneyConverter;

/*
ProductDocumentDto takes a ProductModel object and formats to insert
into a lucene document for indexing purposes.
 */
public class ProductDocumentDto {

    private String productId;
    private int price;
    private String productDescription;
    private int reviewCount;
    private Double reviewRating;
    private String inStock;

    public ProductDocumentDto(ProductModel productModel) {
        this.setProductId(productModel);
        this.setPrice(productModel);
        this.setProductDescription(productModel);
        this.setReviewCount(productModel);
        this.setReviewRating(productModel);
        this.setInStock(productModel);
    }

    public String getProductId() {
        return productId;
    }

    private void setProductId(ProductModel productModel) {
        this.productId = productModel.getProductId().toString();
    }

    public int getPrice() {
        return price;
    }

    private void setPrice(ProductModel productModel) {
        this.price = MoneyConverter.dollarsStringToInteger(productModel.getPrice());
    }

    public String getProductDescription() {
        return productDescription;
    }

    private void setProductDescription(ProductModel productModel) {
        this.productDescription = productModel.getProductName() + " "
                + productModel.getShortDescription() + " "
                + productModel.getLongDescription();
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(ProductModel productModel) {
        this.reviewCount = productModel.getReviewCount();
    }

    public Double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(ProductModel productModel) {
        this.reviewRating = productModel.getReviewRating();
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(ProductModel productModel) {
        this.inStock = productModel.isInStock().toString();
    }
}