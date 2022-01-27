package com.example.inventorymodule.service;

import com.example.inventorymodule.specification.SearchBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ProductService {
    ResponseEntity findAll(SearchBody searchBody) throws Exception;
}
