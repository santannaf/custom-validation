package com.example.customvalidation.pojo;

import com.example.customvalidation.annotation.Conditional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Conditional(selected = "name", isNullOrEmpty = false, required = {"age"})
public class Customer {
    private String name;
    private int age;
    @Builder.Default
    private UUID id = UUID.randomUUID();
}