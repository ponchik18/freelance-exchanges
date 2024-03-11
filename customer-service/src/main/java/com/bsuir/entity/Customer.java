package com.bsuir.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("customers")
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String userId;
}