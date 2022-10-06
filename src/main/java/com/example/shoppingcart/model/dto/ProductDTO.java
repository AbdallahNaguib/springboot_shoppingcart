package com.example.shoppingcart.model.dto;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.ManyToOne;

public class ProductDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private String name;
        private String image;
        private String description;
        private int price;
        private int quantity;
        private Long categoryId;

        public Product mapToEntity() {
            return Product.builder()
                    .name(name)
                    .image(image)
                    .description(description)
                    .price(price)
                    .quantity(quantity)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class Retrieve {
        private String name;
        private String image;
        private String description;
        private int price;
        private int quantity;

        public static Retrieve mapFromEntity(Product product){
            return Retrieve.builder()
                    .name(product.getName())
                    .image(product.getImage())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();
        }
    }
}
