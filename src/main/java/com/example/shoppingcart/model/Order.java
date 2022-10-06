package com.example.shoppingcart.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private Product product;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private AppUser user;
}
