package com.ianplunkett.productsearch.Service.Impl;

import com.ianplunkett.productsearch.Model.ProductDocumentDto;
import com.ianplunkett.productsearch.Model.ProductFields;
import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Model.ProductModel;
import com.ianplunkett.productsearch.Repository.ProductRepository;
import com.ianplunkett.productsearch.Service.IndexerService;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class IndexerServiceImpl implements IndexerService {

    private static final Logger log = LoggerFactory.getLogger(IndexerServiceImpl.class);

    @Value("${product-listing-root-uri}")
    private String rootUri;

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;
    private final IndexWriter indexWriter;
    private final ObjectFactory<Document> documentObjectFactory;
    private final ObjectProvider<TextField> textFieldObjectProvider;
    private final ObjectProvider<IntPoint> intPointObjectProvider;
    private final ObjectProvider<DoublePoint> doublePointObjectProvider;
    private final ObjectProvider<StringField> booleanFieldObjectProvider;
    private final ObjectProvider<StoredField> storedFieldObjectProvider;


    public IndexerServiceImpl(RestTemplate restTemplate, ProductRepository productRepository, IndexWriter indexWriter, ObjectFactory<Document> documentObjectFactory, ObjectProvider<TextField> textFieldObjectProvider, ObjectProvider<IntPoint> intPointObjectProvider, ObjectProvider<DoublePoint> doublePointObjectProvider, ObjectProvider<StringField> booleanFieldObjectProvider, ObjectProvider<StoredField> storedFieldObjectProvider) {
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
        this.indexWriter = indexWriter;
        this.documentObjectFactory = documentObjectFactory;
        this.textFieldObjectProvider = textFieldObjectProvider;
        this.intPointObjectProvider = intPointObjectProvider;
        this.doublePointObjectProvider = doublePointObjectProvider;
        this.booleanFieldObjectProvider = booleanFieldObjectProvider;
        this.storedFieldObjectProvider = storedFieldObjectProvider;
    }

    // Fetch product data from external source and store in an in-memory relational db
    @Override
    public void fetchProductListing(ProductJSON productJSON) {

        // Wipe the old product listings first
        productRepository.deleteAll();

        if (productJSON == null) {
            // Default starting page and size
            int pageNumber = 1;
            int pageSize = 30;

            // Initial Request
            productJSON = fetchPage(pageNumber, pageSize);
            int totalProducts = productJSON.getTotalProducts();
            saveProducts(productJSON.getProducts());
            log.info("caching products page:" + pageNumber);

            // Page through all the product listings to build a local cache
            while (totalProducts > pageSize * pageNumber) {
                pageNumber += 1;
                productJSON = fetchPage(pageNumber, pageSize);
                assert productJSON != null;
                saveProducts(productJSON.getProducts());
                log.info("caching products page:" + pageNumber);
            }
        } else {
            saveProducts(productJSON.getProducts());
        }
    }

    // Retrieve locally cached Product Listings and build Lucene index;
    @Override
    public int createIndex() throws IOException {
        Iterable<ProductModel> products = productRepository.findAll();

        // Delete the old index first
        indexWriter.deleteAll();
        indexWriter.commit();

        log.info("Indexing Product Listing Records");
        int count = 0;
        for (ProductModel productModel : products) {
            Document document = createDocument(new ProductDocumentDto(productModel));
            try {
                indexWriter.addDocument(document);
                count += 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        indexWriter.commit();
        log.info("Indexed " + count + " records");
        return count;
    }

    // Create a Lucene Document to index
    private Document createDocument(ProductDocumentDto productDocumentDto) {
        Document document = documentObjectFactory.getObject();
        document.add(storedFieldObjectProvider.getObject(ProductFields.PRODUCT_ID, productDocumentDto.getProductId()));
        document.add(textFieldObjectProvider.getObject(ProductFields.PRODUCT_DESCRIPTION, productDocumentDto.getProductDescription()));
        document.add(intPointObjectProvider.getObject(ProductFields.PRICE, productDocumentDto.getPrice()));
        document.add(intPointObjectProvider.getObject(ProductFields.REVIEW_COUNT, productDocumentDto.getReviewCount()));
        document.add(doublePointObjectProvider.getObject(ProductFields.REVIEW_RATING, productDocumentDto.getReviewRating()));
        document.add(booleanFieldObjectProvider.getObject(ProductFields.IN_STOCK, productDocumentDto.getInStock()));
        return document;
    }

    // Fetch product listings from a remote REST endpoint
    private ProductJSON fetchPage(Integer pageNumber, Integer pageSize) {
        String resourceUri = rootUri + "/" + pageNumber.toString() + "/" + pageSize.toString();
        return restTemplate.getForObject(resourceUri, ProductJSON.class);
    }

    private void saveProducts(List<ProductModel> productList) {
        productList.forEach((productRepository::save));
    }
}