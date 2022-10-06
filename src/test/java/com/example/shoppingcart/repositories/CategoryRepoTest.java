package com.example.shoppingcart.repositories;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.repository.CategoryRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;


@SpringBootTest
public class CategoryRepoTest {
    @Autowired
    private CategoryRepo categoryRepo;


    @Test
    @Transactional
    public void testUpdating() {
        Category category = new Category("cat1",null);
        categoryRepo.save(category);
        System.out.println(category);
        String newName = "cat10";
        category.setName(newName);
        categoryRepo.save(category);
        Category category2 = categoryRepo.getReferenceById(category.getId());
        Assertions.assertEquals(category2.getName(), newName);
    }
}
