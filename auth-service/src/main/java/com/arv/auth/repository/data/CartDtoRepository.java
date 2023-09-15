package com.arv.auth.repository.data;

import com.arv.auth.model.document.Cart;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.dto.response.CartItemsList;

import java.util.List;

public interface CartDtoRepository {
    void save(CartDto cartDto);

    List<Cart> findAll();
    CartItemsList findAllAndCount();

    void delete(String id);

    void deleteAll();
}
