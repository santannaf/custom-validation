package com.example.customvalidation.controller;

import com.example.customvalidation.pojo.Customer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("custom-validation")
public class CustomValidationController {

    @PostMapping
    Customer customValidation(@Valid @RequestBody Customer customer) {
        return customer;
    }
}