package com.app.productbackend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;




import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ProductImages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
        @JsonBackReference
    private Product product;
}