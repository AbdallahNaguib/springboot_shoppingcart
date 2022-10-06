package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.AppUser;
import com.example.shoppingcart.model.dto.FavouriteDTO;
import com.example.shoppingcart.model.dto.ProductDTO;
import com.example.shoppingcart.services.FavouriteService;
import com.example.shoppingcart.services.SecurityContextService;
import com.example.shoppingcart.services.interfaces.IFavouriteService;
import com.example.shoppingcart.services.interfaces.ISecurityContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FavouriteController {
    private final IFavouriteService favouriteService;
    public FavouriteController(IFavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping("/favourite")
    public ResponseEntity<?> saveFavourite(@RequestParam Long productId) {
        favouriteService.save(productId);
        return ResponseEntity.ok().build();
    }
    // gets all favourites for authenticated user
    @GetMapping("/favourites")
    public ResponseEntity<List<ProductDTO.Retrieve>> findAllFavourites() {
        return ResponseEntity.ok(favouriteService.findAllFavouritesForAuthenticatedUser());
    }
}
