package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.User;
import com.arv.auth.model.document.Wishlist;
import com.arv.auth.model.dto.request.wishlist.WishlistDto;
import com.arv.auth.repository.crud.WishlistRepository;
import com.arv.auth.repository.data.WishlistDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService implements WishlistDtoRepository {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private GlobalCommonService globalCommonService;

    @Override
    public void save(WishlistDto wishlistDto) {
        User user = globalCommonService.getCurrentUser();
        Wishlist wishlist = (Wishlist) globalCommonService.convertObject(wishlistDto, Wishlist.class);
        wishlist.setUserId(user.getId());
        wishlistRepository.save(wishlist);
    }

    @Override
    public List<Wishlist> findAll() {
        User user = globalCommonService.getCurrentUser();
        return wishlistRepository.findAllByUserId(user.getId());
    }

    @Override
    public void delete(String id) {
        User user = globalCommonService.getCurrentUser();
        wishlistRepository.deleteByIdAndUserId(id, user.getId());
    }

    @Override
    public void deleteAll() {
        User user = globalCommonService.getCurrentUser();
        wishlistRepository.deleteAllByUserId(user.getId());
    }
}
