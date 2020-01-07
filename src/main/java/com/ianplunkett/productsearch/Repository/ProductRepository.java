package com.ianplunkett.productsearch.Repository;

import com.ianplunkett.productsearch.Model.ProductModel;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<ProductModel, UUID> {
}
