package com.ianplunkett.productsearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ProductSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductSearchApplication.class, args);
    }

    // This bean provides any easy to use HTTP client against REST endpoints
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    /* The following beans are needed to perform lucence indexing and searching */
    @Bean
    StandardAnalyzer standardAnalyzer() {
        return new StandardAnalyzer();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QueryParser queryParser(StandardAnalyzer analyzer) {
        return new QueryParser("productDescription", analyzer);
    }

    @Bean
    Directory directory() throws IOException {
        Path indexPath = Files.createTempDirectory("tempIndex");
        return FSDirectory.open(indexPath);
    }

    @Bean
    IndexWriterConfig indexWriterConfig(StandardAnalyzer analyzer) {
        return new IndexWriterConfig(analyzer);
    }

    @Bean
    IndexSearcher indexSearcher(Directory directory) throws IOException {
        IndexReader indexReader = DirectoryReader.open(directory);
        return new IndexSearcher(indexReader);
    }

    @Bean
    IndexWriter indexWriter(Directory directory, IndexWriterConfig indexWriterConfig) throws IOException {
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(directory, indexWriterConfig);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Document document() {
        return new Document();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    StringField stringField(String name, String value) {
        return new StringField(name, value, Field.Store.NO);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    TextField textField(String name, String value) {
        return new TextField(name, value, Field.Store.NO);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    IntPoint intPoint(String name, int value) {
        return new IntPoint(name, value);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    DoublePoint doublePoint(String name, double value) {
        return new DoublePoint(name, value);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BooleanQuery.Builder builder() {
        return new BooleanQuery.Builder();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    StoredField storedField(String name, String value) {
        return new StoredField(name, value);
    }
    /* The previous beans are needed to perform lucence indexing and searching */

}
