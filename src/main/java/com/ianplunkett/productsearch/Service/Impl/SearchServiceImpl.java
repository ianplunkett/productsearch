package com.ianplunkett.productsearch.Service.Impl;

import com.ianplunkett.productsearch.Model.ProductConstants;
import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Model.ProductModel;
import com.ianplunkett.productsearch.Model.QueryModel;
import com.ianplunkett.productsearch.Repository.ProductRepository;
import com.ianplunkett.productsearch.Service.SearchService;
import com.ianplunkett.productsearch.Util.MoneyConverter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SearchServiceImpl implements SearchService {

    private BooleanQuery.Builder builder;

    private final ProductRepository productRepository;

    private final IndexSearcher indexSearcher;

    private final QueryParser queryParser;

    public SearchServiceImpl(ProductRepository productRepository, IndexSearcher indexSearcher, QueryParser queryParser) {
        this.productRepository = productRepository;
        this.indexSearcher = indexSearcher;
        this.queryParser = queryParser;
    }

    @Override
    public ProductJSON search(QueryModel queryModel, BooleanQuery.Builder builder) throws ParseException, IOException {

        // Build a Lucene Query
        this.builder = builder;
        this.setInStock(queryModel.getInStock());
        this.setPriceRange(queryModel.getMinPrice(), queryModel.getMaxPrice());
        this.setReviewRatingRange(queryModel.getMinReviewRating(), queryModel.getMaxReviewRating());
        this.setReviewCountRange(queryModel.getMinReviewCount(), queryModel.getMaxReviewCount());
        this.setQueryString(queryModel.getQ());
        Query finalQuery = builder.build();

        // Search! and don't (realistically) limit the number of results that come back
        TopDocs topDocs = indexSearcher.search(finalQuery, Integer.MAX_VALUE);
        ScoreDoc[] hits = topDocs.scoreDocs;

        // Walk through list of search result ids and find the corresponding records
        // in our in-memory relational database.
        List<ProductModel> productList = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document document = indexSearcher.doc(hit.doc);
            String productId = document.get(ProductConstants.PRODUCT_ID);
            UUID uuid = UUID.fromString(productId);
            Optional<ProductModel> product = productRepository.findById(uuid);
            product.ifPresent(productList::add);
        }

        /*
           After we get a list of product search results, package the results using
           using the same format we originally received
         */
        ProductJSON productJSON = new ProductJSON();
        productJSON.setProducts(productList);
        productJSON.setPageNumber(1);
        productJSON.setPageSize(productList.size());
        productJSON.setStatusCode(200);
        productJSON.setTotalProducts(productList.size());
        return productJSON;
    }

    private void setInStock(Boolean inStock)  {
        if (inStock != null) {
            String queryString = inStock.toString();
            Query query = new TermQuery(new Term(ProductConstants.IN_STOCK, queryString));
            builder.add(query, BooleanClause.Occur.FILTER);
        }
    }

    private void setReviewCountRange(Integer minReviewCount, Integer maxReviewCount) {
        // Min & Max Review filter
        if (minReviewCount == null) {
            minReviewCount = 0;
        }

        if (maxReviewCount == null) {
            maxReviewCount = Integer.MAX_VALUE;
        }
        Query reviewCount = IntPoint.newRangeQuery(ProductConstants.REVIEW_COUNT, minReviewCount, maxReviewCount);
        builder.add(reviewCount, BooleanClause.Occur.FILTER);
    }

    private void setPriceRange(String minPrice, String maxPrice) {
        // Min & Max Price filter
        int minPriceInt = 0;
        int maxPriceInt = Integer.MAX_VALUE;

        if (minPrice != null) {
            minPriceInt = MoneyConverter.dollarsStringToInteger(minPrice);
        }

        if (maxPrice != null) {
            maxPriceInt = MoneyConverter.dollarsStringToInteger(maxPrice);
        }
        Query reviewCount = IntPoint.newRangeQuery(ProductConstants.PRICE, minPriceInt, maxPriceInt);
        builder.add(reviewCount, BooleanClause.Occur.FILTER);
    }


    private void setQueryString(String queryString) throws ParseException {
        // The search query
        if (queryString != null) {
            Query query = queryParser.parse(queryString);
            builder.add(query, BooleanClause.Occur.MUST);
        }
    }


    private void setReviewRatingRange(Double minReviewRating, Double maxReviewRating) {
        if (minReviewRating == null) {
            minReviewRating = 0.0;
        }

        if (maxReviewRating == null) {
            maxReviewRating = Double.MAX_VALUE;
        }

        Query reviewRating = DoublePoint.newRangeQuery(ProductConstants.REVIEW_RATING, minReviewRating, maxReviewRating);
        builder.add(reviewRating, BooleanClause.Occur.FILTER);
    }
}
