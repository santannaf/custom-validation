package com.example.customvalidation.service;

import com.example.customvalidation.pojo.Address;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class CepService {

    public Address address(String codePostal) throws URISyntaxException, InterruptedException, ExecutionException {
        String uri = String.format("https://viacep.com.br/ws/%s/json/", codePostal);

        return HttpClient.newHttpClient()
                .sendAsync(HttpRequest.newBuilder()
                                .uri(new URI(uri))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::toAddress)
                .get();
    }

    private Address toAddress(String content) {
        try {
            Address o = new ObjectMapper().readValue(content, new TypeReference<>() {});
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Address>> violations = validator.validate(o);
            if (!violations.isEmpty()) {
                throw new Exception("error");
            }

            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}