package com.arv.auth.model.dto.request.order;

import com.arv.auth.model.dto.request.cart.CartDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    List<CartDto> list;
}
