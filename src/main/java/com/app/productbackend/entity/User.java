package com.app.productbackend.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "Users") // MUST match init.sql
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;
    private String role;
}