package com.example.shoppingcart.model.dto;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        public static Retrieve mapFromEntity(Category category) {
            return new Retrieve(category.getId(), category.getName(), category.getUser().getUsername());
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
