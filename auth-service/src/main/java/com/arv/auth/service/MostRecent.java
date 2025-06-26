package com.arv.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostRecent {
    private List<String> recentElements;
    private int capacity;

    public void addElement(String productId) {
        if (productId == null || productId.isEmpty()) {
            return;
        }

        int index = recentElements.indexOf(productId);

        if (index != -1) {
            removeElement(index);
        }

        if (recentElements.size() == capacity) {
            removeElement(recentElements.size() - 1);
        }

        recentElements.add(0, productId);
    }

    public void removeElement(int index) {
        if (index < 0 || index >= recentElements.size()) {
            return;
        }

        recentElements.remove(index);
    }

    public List<String> getRecentElements() {
        return new ArrayList<>(recentElements);
    }
}

