package com.example.shoppingcart.services;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.Favourite;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.dto.FavouriteDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.repository.FavouriteRepo;
import com.example.shoppingcart.services.interfaces.IFavouriteService;
import com.example.shoppingcart.services.interfaces.IProductService;
import com.example.shoppingcart.services.interfaces.ISecurityContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class FavouriteService implements IFavouriteService {

    private final FavouriteRepo favouriteRepo;
    private final ISecurityContext securityContext;
    private final IProductService productService;

    @Override
    public void save(Long productId) {
        Product product = productService.getProductIfExistsById(productId,"favourite not saved");
        Favourite favourite = Favourite.builder()
                .appUser(securityContext.getAuthenticatedUser())
                .product(product)
                .build();
        favouriteRepo.save(favourite);
    }

    @Override
    public List<ProductDTO.Retrieve> findAllFavouritesForAuthenticatedUser() {
        Long authenticatedUserId = securityContext.getAuthenticatedUser().getId();
        Function<Product, ProductDTO.Retrieve> mapperFromEntityToDTO = ProductDTO.Retrieve::mapFromEntity;
        return favouriteRepo.findAllForUserId(authenticatedUserId).stream().map(mapperFromEntityToDTO).toList();
    }
}
