package com.example.shoppingcart;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.dto.UserDTO;
import com.example.shoppingcart.repository.CategoryRepo;
import com.example.shoppingcart.repository.ProductRepo;
import com.example.shoppingcart.services.interfaces.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ShoppingcartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingcartApplication.class, args);
    }


    @Bean
    CommandLineRunner run(CategoryRepo categoryRepo, IUserService userService, ProductRepo productRepo) {
        return args -> {
            userService.createCustomer(UserDTO.Create.builder()
                    .username("abdo")
                    .password("password")
                    .build());

            AppUser admin = userService.createAdmin(UserDTO.Create.builder()
                    .username("admin")
                    .password("password")
                    .build());
            userService.createAdmin(UserDTO.Create.builder()
                    .username("admin2")
                    .password("password")
                    .build());
            Category category = new Category("cat1", admin);
            Category category2 = new Category("cat2", admin);
            categoryRepo.save(category);
            categoryRepo.save(category2);
            categoryRepo.save(new Category("cat3", admin));

            productRepo.save(Product.builder().category(category).build());
            productRepo.save(Product.builder().category(category).build());
            productRepo.save(Product.builder().category(category2).build());
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

