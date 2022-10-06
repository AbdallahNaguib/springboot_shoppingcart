package com.example.shoppingcart.services.interfaces;

import com.example.shoppingcart.model.Favourite;
import com.example.shoppingcart.model.dto.FavouriteDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.services.FavouriteService;

import java.util.List;

public interface IFavouriteService {
    void save(Long productId);

    List<ProductDTO.Retrieve> findAllFavouritesForAuthenticatedUser();
}
