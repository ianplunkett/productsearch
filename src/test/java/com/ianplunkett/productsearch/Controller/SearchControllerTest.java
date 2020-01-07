package com.ianplunkett.productsearch.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ianplunkett.productsearch.Model.ProductJSON;
import com.ianplunkett.productsearch.Service.IndexerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchControllerTest {

    @Autowired
    IndexerService indexerService;

    @BeforeAll
    void beforeAll() throws IOException {
        InputStream inputStream = SearchControllerTest.class.getClassLoader().getResourceAsStream("productList.json");
        ObjectMapper mapper = new ObjectMapper();
        assert inputStream != null;
        ProductJSON productJSON = mapper.readValue(inputStream, ProductJSON.class);
        indexerService.fetchProductListing(productJSON);
        indexerService.createIndex();
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getValidSearch() throws Exception {

        String validSearch = "/search?maxPrice=$20.00&minPrice=$0.00";

        mockMvc.perform(get(validSearch))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void getInvalidSearch() throws Exception {
        String validSearch = "/search?inStock=17";

        mockMvc.perform(get(validSearch))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}