package com.example.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "favourites")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Favourite {
    @Id
    @GeneratedValue
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    AppUser appUser;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    Product product;
}
