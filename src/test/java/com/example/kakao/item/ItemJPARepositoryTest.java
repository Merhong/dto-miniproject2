package com.example.kakao.item;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.kakao.order.item.Item;
import com.example.kakao.order.item.ItemJPARepository;

@DataJpaTest
public class ItemJPARepositoryTest {
    
    @Autowired
    private ItemJPARepository itemJPARepository;

    @Test
    public void findAllByOrderId_test(){
        List<Item> itemList = itemJPARepository.findAllByOrderId(1);
        System.out.println( "테스트1: "+itemList.get(0).getId() );
    }
}
