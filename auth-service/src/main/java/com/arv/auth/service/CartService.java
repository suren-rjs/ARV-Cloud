package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Cart;
import com.arv.auth.model.document.User;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.dto.response.CartItem;
import com.arv.auth.model.dto.response.CartItemsList;
import com.arv.auth.repository.crud.CartRepository;
import com.arv.auth.repository.data.CartDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService implements CartDtoRepository {
    @Autowired
    protected GlobalCommonService globalCommonService;

    @Autowired
    protected CartRepository cartRepository;

    @Override
    public void save(CartDto cartDto) {
        User user = globalCommonService.getCurrentUser();
        Cart item = (Cart) globalCommonService.convertObject(cartDto, Cart.class);
        item.setUserId(user.getId());

        if (cartRepository.existsByProductIdAndUserId(cartDto.getProductId(), user.getId())) {
            item = cartRepository.findByProductIdAndUserId(cartDto.getProductId(), user.getId());
            item.setQty(cartDto.getQty());
        }

        cartRepository.save(item);
    }

    @Override
    public List<Cart> findAll() {
        User user = globalCommonService.getCurrentUser();
        return cartRepository.findAllByUserId(user.getId());
    }

    @Override
    public CartItemsList findAllAndCount() {
        List<Cart> allItems = findAll();
        List<CartItem> cartList = allItems.stream().map(cart -> new CartItem(cart.getProductId(), cart.getQty())).collect(Collectors.toList());

        return new CartItemsList(cartList, cartList.size());
    }

    @Override
    public void delete(String id) {
        User user = globalCommonService.getCurrentUser();
        Cart cart = cartRepository.findByProductIdAndUserId(id, user.getId());
        cartRepository.delete(cart);
    }

    @Override
    public void deleteAll() {
        User user = globalCommonService.getCurrentUser();
        cartRepository.deleteAllByUserId(user.getId());
    }
}
