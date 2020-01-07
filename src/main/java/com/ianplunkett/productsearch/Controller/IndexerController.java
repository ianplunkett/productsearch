package com.ianplunkett.productsearch.Controller;

import com.ianplunkett.productsearch.Service.IndexerService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexerController {

    final private IndexerService indexerService;


    public IndexerController(IndexerService indexerService) {
        this.indexerService = indexerService;
    }

    @PutMapping("/indexer")
    public String index() throws IOException {
        indexerService.fetchProductListing(null);
        int indexedRecords = indexerService.createIndex();
        return "{\"indexedRecord\":" + indexedRecords + "}";
    }
}
