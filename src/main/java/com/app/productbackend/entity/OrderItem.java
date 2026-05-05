package com.app.productbackend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "OrderItems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_id")
    private int productId;

    private String productName;
    private double price;
    private int quantity;


@JsonBackReference
@ManyToOne
@JoinColumn(name = "order_id")
private Order order;
}