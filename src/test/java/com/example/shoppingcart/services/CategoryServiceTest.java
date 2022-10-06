package com.example.shoppingcart.services;

import com.example.shoppingcart.Constants;
import com.example.shoppingcart.error.UserNotAdminException;
import com.example.shoppingcart.error.UserNotOwningCategoryException;
import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.UserInfo;
import com.example.shoppingcart.model.dto.CategoryDTO;
import com.example.shoppingcart.repository.CategoryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepo categoryRepo;
    @Mock
    SecurityContextService securityContextService;

    List<Category> mockCategoriesTable;
    Long currentCategoryId;
    AppUser admin;

    @BeforeEach
    void setup() {
        UserInfo adminUserInfo = UserInfo.builder().username("admin").usertype(Constants.ADMIN).build();
        admin = AppUser.builder().id(1L).userInfo(adminUserInfo).build();
        currentCategoryId = 1L;
        mockCategoriesTable = new ArrayList<>();
    }

    void mockSavingInCategoryRepo() {
        when(categoryRepo.save(any())).thenAnswer(par -> {
            Category category = par.getArgument(0, Category.class);
            if (category.getId() == null) {
                // category id not set so it's an add operation
                category.setId(currentCategoryId++);
                mockCategoriesTable.add(Category.builder().user(category.getUser())
                        .name(category.getName())
                        .id(category.getId())
                        .build());
                return category;
            } else {
                // id is set , so it's an update operation
                Category savedCat = mockCategoriesTable.stream().filter(currentCat -> category.getId().equals(currentCat.getId()))
                        .findFirst().get();
                savedCat.setName(category.getName());
                return savedCat;
            }
        });
    }

    void mockDeletingFromCategoryRepo() {
        doAnswer(par -> {
            Category category = par.getArgument(0, Category.class);
            mockCategoriesTable.remove(category);
            return null;
        }).when(categoryRepo).delete(any());
    }

    void mockGetCategoryById() {
        when(categoryRepo.getReferenceById(any())).thenAnswer(par -> {
            Long id = par.getArgument(0, Long.class);
            return mockCategoriesTable.stream().filter(category -> category.getId().equals(id))
                    .findFirst().orElseThrow((Supplier<Throwable>) EntityNotFoundException::new);
        });
    }

    void mockAuthenticatedUser(AppUser user) {
        when(securityContextService.getAuthenticatedUser()).thenReturn(user);
    }

    @Test
    public void testInsertingSomeCategoriesWithValidUser() {
        mockSavingInCategoryRepo();
        categoryService.save(new CategoryDTO.Create("cat1"), admin);
        categoryService.save(new CategoryDTO.Create("cat2"), admin);
        categoryService.save(new CategoryDTO.Create("cat3"), admin);
        assertEquals(mockCategoriesTable.size(), 3);
    }

    @Test
    public void testingCheckCategoryOwner() {
        AppUser admin2 = new AppUser("admin2", "", Constants.ADMIN);
        admin.setId(1L);
        admin2.setId(2L);
        mockGetCategoryById();
        mockCategoriesTable.add(Category.builder().id(1L).user(admin).build());
        mockCategoriesTable.add(Category.builder().id(2L).user(admin2).build());
        mockAuthenticatedUser(admin);
        assertDoesNotThrow(() -> categoryService.checkCategoryIsOwnedByAuthenticatedUser(1L, ""));
        assertThrows(UserNotOwningCategoryException.class, () -> categoryService.checkCategoryIsOwnedByAuthenticatedUser(2L, ""));

        mockAuthenticatedUser(admin2);
        assertDoesNotThrow(() -> categoryService.checkCategoryIsOwnedByAuthenticatedUser(2L, ""));
        assertThrows(UserNotOwningCategoryException.class, () -> categoryService.checkCategoryIsOwnedByAuthenticatedUser(1L, ""));
    }

    @Test
    public void testInsertingSomeCategoriesWithInValidUser() {
        mockSavingInCategoryRepo();
        AppUser user = new AppUser("admin", "", Constants.CUSTOMER);
        assertThrows(UserNotAdminException.class, () -> {
            categoryService.save(new CategoryDTO.Create("cat1"), user);
        });
        assertEquals(mockCategoriesTable.size(), 0);
        user.getUserInfo().setUsertype(Constants.ADMIN);
        categoryService.save(new CategoryDTO.Create("cat1"), user);
        assertEquals(mockCategoriesTable.size(), 1);
    }

    @Test
    public void testDeleteCategoryByValidUser() {
        mockSavingInCategoryRepo();
        mockAuthenticatedUser(admin);
        mockDeletingFromCategoryRepo();
        mockGetCategoryById();
        CategoryDTO.Retrieve category = categoryService.save(new CategoryDTO.Create("cat1"), admin);
        assertEquals(mockCategoriesTable.size(), 1);
        categoryService.deleteCategoryById(category.getId());
        assertEquals(mockCategoriesTable.size(), 0);
    }

    @Test
    public void testDeleteCategoryByInValidUser() {
        UserInfo adminUserInfo = UserInfo.builder().username("admin2").usertype(Constants.ADMIN).build();
        AppUser admin2 = AppUser.builder().id(2L).userInfo(adminUserInfo).build();
        mockSavingInCategoryRepo();
        mockAuthenticatedUser(admin);
        mockGetCategoryById();
        CategoryDTO.Retrieve category = categoryService.save(new CategoryDTO.Create("cat1"), admin2);
        assertEquals(mockCategoriesTable.size(), 1);
        assertThrows(UserNotOwningCategoryException.class, () -> categoryService.deleteCategoryById(category.getId()));
        assertEquals(mockCategoriesTable.size(), 1);
    }

    @Test
    public void testUpdatingCategory() {
        mockSavingInCategoryRepo();
        mockGetCategoryById();
        mockAuthenticatedUser(admin);
        Category category1 = categoryRepo.save(new Category("cat1", admin));
        Category category2 = categoryRepo.save(new Category("cat2", admin));
        category1.setName("newCat1");
        categoryService.updateCategory(category1.getId(), CategoryDTO.Update.mapFromEntity(category1));

        int cat1Cnt = 0, cat2Cnt = 0;
        for (Category category : mockCategoriesTable) {
            if (category.getName().equals(category1.getName())) {
                cat1Cnt++;
            } else if (category.getName().equals(category2.getName())) {
                cat2Cnt++;
            }
        }
        Assertions.assertEquals(cat1Cnt, 1);
        Assertions.assertEquals(cat2Cnt, 1);
    }

    @Test
    public void testUpdatingCategoryWithInvalidUser() {
        mockSavingInCategoryRepo();
        mockGetCategoryById();
        UserInfo adminUserInfo = UserInfo.builder().username("admin2").usertype(Constants.ADMIN).build();
        AppUser admin2 = AppUser.builder().id(2L).userInfo(adminUserInfo).build();
        mockAuthenticatedUser(admin);
        Category category = categoryRepo.save(new Category("cat1", admin2));
        category.setName("cat2");
        CategoryDTO.Update categoryDTO = CategoryDTO.Update.mapFromEntity(category);
        assertThrows(UserNotOwningCategoryException.class, () -> categoryService.updateCategory(category.getId(), categoryDTO));

    }
}