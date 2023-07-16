package com.arv.auth.model.dto.request.order;

import com.arv.auth.model.document.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderList {
    List<Order> list;
    int currentPage;
    long totalCount;
    int totalPages;
}
