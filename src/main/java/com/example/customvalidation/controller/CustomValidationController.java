package com.example.customvalidation.controller;

import com.example.customvalidation.pojo.Address;
import com.example.customvalidation.pojo.Customer;
import com.example.customvalidation.service.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("custom-validation")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomValidationController {

    private final CepService cepService;

    @PostMapping
    ResponseEntity<?> customValidation(@RequestBody Customer customer) throws URISyntaxException, InterruptedException,
            ExecutionException {
        Address address = cepService.address(customer.getCodePostal());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        customer.setAddress(address);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}