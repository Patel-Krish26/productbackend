package com.app.productbackend.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Orders") // MUST match DB (not "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "total_amount")
    private double totalAmount;

    private String status;


@JsonManagedReference
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> items;
}