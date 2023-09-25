package com.example.kakao.order;

import com.example.kakao.cart.Cart;
import com.example.kakao.order.item.Item;
import com.example.kakao.product.Product;
import com.example.kakao.product.option.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    // (기능4) 주문상품 정보조회 (유저별)
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO {
        private Integer totalPrice = 0; // 가격 총합, 스칼라
        private List<CartDTO> cartDTOList; // 전체는 JSON Object

        public FindAllByUserDTO(List<Cart> cartList) {

            cartList.stream()
                    .forEach(cart -> this.totalPrice += cart.getPrice());

            this.cartDTOList = cartList.stream()
                    .map(cart -> new CartDTO(cart))
                    .collect(Collectors.toList());
        }


        @Getter
        @Setter
        @ToString
        public class CartDTO {
            private Integer cartId;
            private String productNameAndOptionName;
            private Integer cartQuantity;
            private Integer cartPrice;


            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.cartQuantity = cart.getQuantity();
                this.cartPrice = cart.getPrice();
                this.productNameAndOptionName = cart.getOption().getProduct().getProductName() + " - " + cart.getOption().getOptionName();
            }

        }


    }


    // (기능5) 주문결과 확인
    @ToString
    @Getter
    @Setter
    public static class FindByIdDTO {
        private Integer orderId;
        private List<ProductDTO> productDTOList;
        private Integer totalPrice = 0;


        public FindByIdDTO(List<Item> itemList) {

            this.orderId = itemList.get(0).getOrder().getId();

            itemList.stream()
                    .forEach(cart -> this.totalPrice += cart.getPrice());

            this.productDTOList = itemList.stream()
                    .map(item -> item.getOption().getProduct())
                    .distinct()
                    .map(product -> new ProductDTO(itemList, product))
                    .collect(Collectors.toList());
        }


        @Getter
        @Setter
        @ToString
        public class ProductDTO {
            private Integer id;
            private String name;
            private List<ItemDTO> ItemDTOList;

            public ProductDTO(List<Item> itemList, Product product) {
                this.id = product.getId();
                this.name = product.getProductName();

                this.ItemDTOList = itemList.stream()
                        .filter(item -> item.getOption().getProduct().equals(product))
                        .map(item -> new ItemDTO(item))
                        .collect(Collectors.toList());
            }
        }


        @Getter
        @Setter
        @ToString
        public class ItemDTO {
            private Integer id;
            private Integer quantity;
            private Integer price;
            private OptionDTO optionDTO;

            public ItemDTO(Item item) {
                this.id = item.getId();
                this.quantity = item.getQuantity();
                this.price = item.getPrice();

                this.optionDTO = new OptionDTO(item.getOption());
            }
        }


        @Getter
        @Setter
        @ToString
        public class OptionDTO {
            private Integer id;
            private String optionName;

            public OptionDTO(Option option) {
                this.id = option.getId();
                this.optionName = option.getOptionName();
            }
        }
    }
}
