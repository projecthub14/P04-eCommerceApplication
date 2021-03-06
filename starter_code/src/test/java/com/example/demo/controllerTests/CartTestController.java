package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartTestController {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);



    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void add_to_cart(){
        User user = new User();
        user.setId(20L);
        user.setUsername("user1");
        user.setCart(new Cart());
        when(userRepository.findByUsername("user1")).thenReturn(user);

        Item item = new Item();
        item.setId(20L);
        item.setName("item1");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("description");
        when(itemRepository.findById(20L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(20L);
        request.setQuantity(5);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);

        Cart cart = response.getBody();
        assertEquals(cart.getItems().size(), 5);
        assertEquals(cart.getTotal(), new BigDecimal(5));
    }

    @Test
    public void remove_from_cart(){
        Item item = new Item();
        item.setId(20L);
        item.setName("item1");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("description");
        when(itemRepository.findById(20L)).thenReturn(Optional.of(item));


        //add items to list
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(item);
        items.add(item);


        Cart cart = new Cart();
        cart.setItems(items);

        User user = new User();
        user.setId(20L);
        user.setUsername("test");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(20L);
        request.setQuantity(2);

        //remove item
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);

        Cart cartFromResponse = response.getBody();
        assertEquals(cartFromResponse.getItems().size(), 2);
    }
}
