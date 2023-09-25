package com.example.kakao.cart;

import com.example.kakao.product.Product;
import com.example.kakao.product.option.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    // (기능3) 장바구니 조회
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO {
        private Integer totalPrice; // 장바구니 cartPrice의 총합, 스칼라
        private List<ProductDTO> products; // 상품1, 상품2, ... 상품n

        // 생성자
        public FindAllByUserDTO(List<Cart> cartList) {
            // totalPrice 스트림 생성
            this.totalPrice = cartList.stream()
                    // 가격 총합 = (옵션 가격 * 옵션 개수)
                    // 스트림에 들어가면 타입이 없다? map을 int로 가공해준다 mapToInt
                    .mapToInt(cart -> cart.getOption().getPrice() * cart.getQuantity())
                    // 최종연산
                    .sum();
            // products 스트림 생성
            this.products = cartList.stream()
                    // 물길에 옵션의 프로덕트를 뽑아낸다. P1, P1, P2
                    .map(cart -> cart.getOption().getProduct())
                    // 중복 제거 -> P1, P2 남아있음
                    .distinct()
                    // 중복 제거한 Product를 화면에 맞게 DTO로 가공
                    .map(product -> new ProductDTO(product, cartList))
                    // 수집
                    .collect(Collectors.toList());
        }

        @Getter
        @Setter
        class ProductDTO {
            private Integer productId;  // 제품ID
            private String productName; // 제품이름
            private List<CartDTO> carts; // 장바구니 리스트

            // 생성자
            public ProductDTO(Product product, List<Cart> cartList) {
                this.productId = product.getId();
                this.productName = product.getProductName();
                // 스트림 생성
                this.carts = cartList.stream()
                        // 상품ID가 중복되는것을 필터링
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        // 필터링한 장바구니를 화면에 맞게 DTO로 가공
                        .map(cart -> new CartDTO(cart))
                        // 수집
                        .collect(Collectors.toList());
            }

            @Getter
            @Setter
            public class CartDTO {
                private Integer cartId; // 장바구니 ID
                private Integer cartQuantity; // 장바구니의 옵션 수량
                private Integer cartPrice; // 장바구니의 옵션 가격
                private OptionDTO option; // 장바구니의 상품 옵션

                // 생성자
                public CartDTO(Cart cart) {
                    this.cartId = cart.getId();
                    this.cartQuantity = cart.getQuantity();
                    this.cartPrice = cart.getPrice();
                    this.option = new OptionDTO(cart.getOption()); // 장바구니안에 있는 옵션을 담아준다.
                }

                @Getter
                @Setter
                public class OptionDTO {
                    private Integer optionId; // 옵션 ID
                    private String optionName; // 옵션이름
                    private Integer optionPrice; // 옵션 가격

                    // 생성자
                    public OptionDTO(Option option) {
                        this.optionId = option.getId();
                        this.optionName = option.getOptionName();
                        this.optionPrice = option.getPrice();
                    }
                }
            }
        }
    }
}

// public class CartResponse {
//
//     // (기능3) 장바구니 조회
//     @ToString
//     @Getter
//     @Setter
//     public static class FindAllByUserDTO {
//         private int totalPrice; // 가격 총합, 스칼라
//         private List<ProductDTO> productDTOList; // 전체는 JSON Object
//
//
//         public FindAllByUserDTO(List<Cart> cartList) {
//             cartList.stream()
//                     .forEach(cart -> this.totalPrice += cart.getPrice());
//
//             this.productDTOList = cartList.stream()
//                     .map(cart -> cart.getOption().getProduct())
//                     .distinct()
//                     .map(product -> new ProductDTO(cartList, product))
//                     .collect(Collectors.toList());
//         }
//
//
//         @Getter
//         @Setter
//         @ToString
//         public class ProductDTO {
//             private Integer id;
//             private String name;
//             private List<CartAndOptionDTO> cartAndOptionDTOList;
//
//             public ProductDTO(List<Cart> cartList, Product product) {
//                 this.id = product.getId();
//                 this.name = product.getProductName();
//
//                 this.cartAndOptionDTOList = cartList.stream()
//                         .filter(cart -> cart.getOption().getProduct().equals(product))
//                         .map(cart -> new CartAndOptionDTO(cart))
//                         .collect(Collectors.toList());
//             }
//         }
//
//
//         @Getter
//         @Setter
//         @ToString
//         public class CartAndOptionDTO {
//             private Integer cartId;
//             private Integer cartQuantity;
//             private Integer cartPrice;
//             private Integer optionId;
//             private String optionName;
//             private Integer optionPrice;
//
//             public CartAndOptionDTO(Cart cart) {
//                 this.cartId = cart.getId();
//                 this.cartQuantity = cart.getQuantity();
//                 this.cartPrice = cart.getPrice();
//                 this.optionId = cart.getOption().getId();
//                 this.optionName = cart.getOption().getOptionName();
//                 this.optionPrice = cart.getOption().getPrice();
//             }
//         }
//
//
//     }
//
//
// }
