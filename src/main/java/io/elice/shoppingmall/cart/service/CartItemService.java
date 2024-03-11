package io.elice.shoppingmall.cart.service;

import io.elice.shoppingmall.cart.dto.CartItemResponseDto;
import io.elice.shoppingmall.cart.dto.CartItemUpdateDto;
import io.elice.shoppingmall.cart.entity.Cart;
import io.elice.shoppingmall.cart.entity.CartItem;
import io.elice.shoppingmall.cart.repository.CartItemRepository;
import io.elice.shoppingmall.cart.repository.CartRepository;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.repository.ProductRepository;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartItem addItemToCart(Long id, Long productId, int qty) {

        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(id);
        Optional<Product> foundProduct = productRepository.findById(productId);

        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다!");
        }
        if(foundProduct.isEmpty()) {
            throw new IllegalArgumentException("추가하려는 제품이 존재하지 않습니다!");
        }

        CartItem cartItem = CartItem.builder()
                .amount(qty)
                .cart(foundCart.get())
                .product(foundProduct.get())
                .build();

        cartItemRepository.save(cartItem);
        foundCart.get().addCartItem(cartItem);

        return cartItem;
    }

    public List<CartItemResponseDto> getAllItemsInCart(Long id) {

        List<CartItem> foundItems = cartItemRepository.findAllByCart_User_Id(id);
        List<CartItemResponseDto> returnItems = new ArrayList<>();

        for(CartItem item : foundItems) {

            CartItemResponseDto dto = CartItemResponseDto.builder()
                    .id(item.getId())
                    .createdAt(item.getCreatedAt())
                    .amount(item.getAmount())
                    .productName(item.getProduct().getProductName())
                    .price(item.getProduct().getPrice())
                    .brandName(item.getProduct().getBrandName())
                    .build();

            returnItems.add(dto);
        }

        return returnItems;
    }

    @Transactional
    public CartItem updateItemQuantity(Long id, Long cartItemId, CartItemUpdateDto cartItemUpdateDto) {

        Optional<Cart> foundCart = cartRepository.findCartByUser_Id(id);
        Optional<CartItem> foundCartItem = cartItemRepository.findById(cartItemId);

        if(foundCart.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다!");
        }
        if(foundCartItem.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 제품이 존재하지 않습니다!");
        }

        CartItem toUpdateCartItem = foundCartItem.get();

        toUpdateCartItem.updateQuantity(cartItemUpdateDto.getQuantity());

        return cartItemRepository.save(toUpdateCartItem);
    }

    @Transactional
    public String deleteAllSelected(Long userId, List<Long> selectedItemIds) {

        Optional<User> foundUser = userRepository.findById(userId);

        if(foundUser.isEmpty()) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다!");
        }

        if(cartRepository.findCartByUser_Id(userId).isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않습니다!");
        }

        if(cartItemRepository.findAllById(selectedItemIds).isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다!");
        }

        cartItemRepository.deleteAllById(selectedItemIds);

        return foundUser.get().getNickname();
    }


}
