package com.app.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "ProductImages")
@Data
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_url")
    private String imageUrl;

    // Many images → one product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}