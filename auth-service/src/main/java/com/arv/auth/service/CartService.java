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
import java.util.Optional;
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

        if (cartRepository.existsByProductIdAndUserIdAndVariant(cartDto.getProductId(), user.getId(), item.getVariant())) {
            item = cartRepository.findByProductIdAndUserIdAndVariant(cartDto.getProductId(), user.getId(), item.getVariant());
            item.setQty(cartDto.getQty());
            item.setOrderPrice(cartDto.getQty() * cartDto.getOrderPrice());
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
    public int findCountByIdAndVariant(String productId, String variant) {
        List<Cart> allItems = findAll();
        Optional<Cart> productItem = allItems.stream().filter(cart -> cart.getProductId().equals(productId) && cart.getVariant().equals(variant)).findFirst();
        return productItem.isPresent() ? productItem.get().getQty() : 0;
    }

    @Override
    public void delete(String id, String variant) {
        User user = globalCommonService.getCurrentUser();
        Cart cart = cartRepository.findByProductIdAndUserIdAndVariant(id, user.getId(), variant);
        cartRepository.delete(cart);
    }

    @Override
    public void deleteAll() {
        User user = globalCommonService.getCurrentUser();
        cartRepository.deleteAllByUserId(user.getId());
    }
}
