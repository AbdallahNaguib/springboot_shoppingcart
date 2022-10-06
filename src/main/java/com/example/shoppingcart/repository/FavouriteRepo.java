package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Favourite;
import com.example.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepo extends JpaRepository<Favourite, Long> {
    @Query(nativeQuery = true, value = "select products.* from favourites left join " +
            "products on favourites.product_id = products.id where favourites.app_user_id = :userId")
    List<Product> findAllForUserId(@Param("userId") Long userId);
}
