package com.ianplunkett.productsearch.Service;

import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Model.QueryModel;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;

import java.io.IOException;

public interface SearchService {
    ProductJSON search(QueryModel queryModel, BooleanQuery.Builder builder) throws ParseException, IOException;
}

