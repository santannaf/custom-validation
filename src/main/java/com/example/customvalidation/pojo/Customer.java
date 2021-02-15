package com.example.customvalidation.pojo;

import com.example.customvalidation.annotation.Conditional;
import com.example.customvalidation.annotation.Conditionals;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Conditionals({
        @Conditional(selected = "email", isNullOrEmpty = false, requires = {"lastname", "name", "age"}),
})
public class Customer {
    private String name;
    private Integer age;
    private String lastname;
    private String email;
    @Builder.Default
    private UUID id = UUID.randomUUID();
}