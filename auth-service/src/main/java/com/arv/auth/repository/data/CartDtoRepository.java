package com.arv.auth.repository.data;

import com.arv.auth.model.document.Cart;
import com.arv.auth.model.dto.request.cart.CartDto;

import java.util.List;

public interface CartDtoRepository {
    void save(CartDto cartDto);

    List<Cart> findAll();

    void delete(String id);

    void deleteAll();
}
