package com.example.inventorymodule.controller;

import com.example.inventorymodule.service.ProductService;
import com.example.inventorymodule.specification.SearchBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id
    ) throws Exception {
        SearchBody searchBody = SearchBody.SearchBodyBuilder.aSearchBody()
                .withPage(page)
                .withLimit(limit)
                .withName(name)
                .withId(id)
                .build();
        return productService.findAll(searchBody);
    }

}
