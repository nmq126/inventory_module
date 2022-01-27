package com.example.inventorymodule.service.impl;

import com.example.inventorymodule.entity.Product;
import com.example.inventorymodule.repository.ProductRepository;
import com.example.inventorymodule.response.ResponseHandler;
import com.example.inventorymodule.service.ProductService;
import com.example.inventorymodule.specification.ProductSpecification;
import com.example.inventorymodule.specification.SearchBody;
import com.example.inventorymodule.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity findAll(SearchBody searchBody) throws Exception {
        Specification specification = Specification.where(null);

        if (searchBody.getName() != null && searchBody.getName().length() > 0){
            specification = specification.and(new ProductSpecification(new SearchCriteria("name", ":", searchBody.getName())));
        }

        if (searchBody.getId() != null){
            specification = specification.and(new ProductSpecification(new SearchCriteria("id", ":", searchBody.getId())));
        }

        try {
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(searchBody.getPage() - 1, searchBody.getLimit(), sort);
            Page<Product> products = productRepository.findAll(specification, pageable);
            if (products.isEmpty()) {
                return  new ResponseEntity(ResponseHandler.ResponseHandlerBuilder.aResponseHandler()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withMessage("No product satisfied")
                        .build(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(ResponseHandler.ResponseHandlerBuilder.aResponseHandler()
                    .withStatus(HttpStatus.OK.value())
                    .withMessage("Okela")
                    .withData("products", products)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
