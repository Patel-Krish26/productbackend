package com.app.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity // Marks this class as DB table
@Table(name = "Products")
@Data // Lombok: generates getters/setters automatically
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private double price;

    private String category;

    private int stock;

    // One product → many images
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;
}