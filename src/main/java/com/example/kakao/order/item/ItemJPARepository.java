package com.example.kakao.order.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemJPARepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOrderId(int orderId);
}
