package com.example;

import com.example.entity.ExcelData;
import com.example.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public void saveProducts(List<Product> productList){

        productRepository.saveAll(productList);

    }

    public void deleteProducts(){

    }
}
