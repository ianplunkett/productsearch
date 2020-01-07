package com.ianplunkett.productsearch.Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.rmi.CORBA.PortableRemoteObjectDelegate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductDocumentDtoTest {

    ProductModel productModel;
    private UUID uuid = UUID.randomUUID();
    private String price = "$30.00";
    private String name = "Product1";
    private String shortDescription = "This is a short description";
    private String longDescription = "This is a long description of product 1";
    private Double reviewRating = 5.0;
    private Integer reviewCount = 10;
    private Boolean inStock = true;

    @BeforeEach
    void setUp() {

        productModel = new ProductModel();
        productModel.setProductId(uuid);
        productModel.setPrice(price);
        productModel.setProductName(name);
        productModel.setShortDescription(shortDescription);
        productModel.setLongDescription(longDescription);
        productModel.setReviewRating(reviewRating);
        productModel.setReviewCount(reviewCount);
        productModel.setInStock(inStock);
    }

    @Test
    void productIdConvertsFromUUIDToString() {
        ProductDocumentDto productDocumentDto = new ProductDocumentDto(productModel);
        assertEquals(productModel.getProductId().toString(), productDocumentDto.getProductId());
    }

    @Test
    void priceConvertsFromStringToInteger() {
        ProductDocumentDto productDocumentDto = new ProductDocumentDto(productModel);
        assertEquals(3000, productDocumentDto.getPrice());
    }

    @Test
    void productDescriptionCombinesNameShortLongDescription() {
        ProductDocumentDto productDocumentDto = new ProductDocumentDto(productModel);
        assertEquals(name + " " + shortDescription + " " + longDescription, productDocumentDto.getProductDescription());
    }


    @Test
    void inStockConvertFromBooleanToString() {
        ProductDocumentDto productDocumentDto1 = new ProductDocumentDto(productModel);
        assertEquals("true", productDocumentDto1.getInStock());
        productModel.setInStock(false);
        ProductDocumentDto productDocumentDto2 = new ProductDocumentDto(productModel);
        assertEquals("false", productDocumentDto2.getInStock());
    }

}