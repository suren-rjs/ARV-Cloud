package com.arv.auth.repository.data;

import com.arv.auth.model.document.Wishlist;
import com.arv.auth.model.dto.request.wishlist.WishlistDto;

import java.util.List;

public interface WishlistDtoRepository {
    void save(WishlistDto wishlistDto);

    List<Wishlist> findAll();

    void delete(String id);

    void deleteAll();
}
