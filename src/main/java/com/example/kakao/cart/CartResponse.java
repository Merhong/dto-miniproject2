package com.example.kakao.cart;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.product.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CartResponse {

    // (기능3) 장바구니 조회
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO{
        private List<ProductDTO> productDTOList;
        private int totalPrice;


        public FindAllByUserDTO(List<Cart> cartList) {
            cartList.stream()
                .forEach( cart -> this.totalPrice += cart.getPrice() );

            this.productDTOList = cartList.stream()
                .map( cart -> cart.getOption().getProduct() )
                .distinct()
                .map( product -> new ProductDTO(cartList, product) )
                .collect( Collectors.toList() );
        }



        @Getter @Setter @ToString
        public class ProductDTO{
            private Integer id;
            private String name;
            private List<CartAndOptionDTO> cartAndOptionDTOList;
            
            public ProductDTO(List<Cart> cartList, Product product) {
                this.id = product.getId();
                this.name = product.getProductName();
                
                this.cartAndOptionDTOList = cartList.stream()
                    .filter( cart -> cart.getOption().getProduct().equals(product) )
                    .map( cart -> new CartAndOptionDTO(cart) )
                    .collect( Collectors.toList() );
            }
        }



        @Getter @Setter @ToString
        public class CartAndOptionDTO{
            private Integer cartId;
            private Integer cartQuantity;
            private Integer cartPrice;
            private Integer optionId;
            private String optionName;
            private Integer optionPrice;

            public CartAndOptionDTO(Cart cart) {
                this.cartId = cart.getId();
                this.cartQuantity = cart.getQuantity();
                this.cartPrice = cart.getPrice();
                this.optionId = cart.getOption().getId();
                this.optionName = cart.getOption().getOptionName();
                this.optionPrice = cart.getOption().getPrice();
            }
        }


    }
    












}
