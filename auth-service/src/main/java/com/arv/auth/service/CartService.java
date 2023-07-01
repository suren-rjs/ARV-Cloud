package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Cart;
import com.arv.auth.model.document.User;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.repository.crud.CartRepository;
import com.arv.auth.repository.data.CartDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements CartDtoRepository {
    @Autowired
    private GlobalCommonService globalCommonService;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public void save(CartDto cartDto) {
        User user = globalCommonService.getCurrentUser();
        Cart item = (Cart) globalCommonService.convertObject(cartDto, Cart.class);
        item.setUserId(user.getId());
        if (cartRepository.existsByProductIdAndUserId(cartDto.getProductId(), user.getId()))
            throw new RuntimeException("Item already exist!");
        cartRepository.save(item);
    }

    @Override
    public List<Cart> findAll() {
        User user = globalCommonService.getCurrentUser();
        return cartRepository.findAllByUserId(user.getId());
    }

    @Override
    public void delete(String id) {
        User user = globalCommonService.getCurrentUser();
        cartRepository.deleteByIdAndUserId(id, user.getId());
    }

    @Override
    public void deleteAll() {
        User user = globalCommonService.getCurrentUser();
        cartRepository.deleteAllByUserId(user.getId());
    }
}
