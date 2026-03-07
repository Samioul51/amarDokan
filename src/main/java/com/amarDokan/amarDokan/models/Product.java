package com.amarDokan.amarDokan.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String category;

    private Double price;

    private Integer stock;

    private String image;

    private Integer discount;

    private Double discountPrice;

    private Boolean isActive;
}
