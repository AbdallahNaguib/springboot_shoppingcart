package com.example.shoppingcart.model.dto;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    @AllArgsConstructor
    @Data
    public static class Create {
        private String name;

        public Category mapToEntity() {
            return Category.builder().name(name).build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Retrieve {
        private Long id;
        private String name;
        private String owner;
        private List<Product> products;
        public static Retrieve mapFromEntity(Category category) {
            List<Product> products = new ArrayList<>();
            products.add(Product.builder()
                    .id(1L).name("p1").build());
            products.add(Product.builder()
                    .id(2L).name("p2").build());
            return new Retrieve(category.getId(), category.getName(), category.getUser().getUsername(),products);
        }
        public Category mapToEntity(){
            return Category.builder().name(name).id(id).build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private String name;

        public Category mapToEntity(){
            return Category.builder().name(name).build();
        }

        public static Update mapFromEntity(Category category) {
            return new Update(category.getName());
        }
    }
}
