package com.ianplunkett.productsearch.Service;

import com.ianplunkett.productsearch.Model.ProductJSON;

import java.io.IOException;

public interface IndexerService {
    int createIndex() throws IOException;

    void fetchProductListing(ProductJSON productJSON);
}