package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTestController {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order(){
        Item item = new Item();
        item.setId(20L);
        item.setName("test item");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("test item desc");

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        User user = new User();
        user.setId(20L);
        user.setUsername("user");
        user.setCart(cart);
        when(userRepository.findByUsername("user")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("user");
        assertNotNull(response);
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(cart.getTotal(), userOrder.getTotal());
    }

    @Test
    public void getUserOrders(){
        Item item = new Item();
        item.setId(20L);
        item.setName("item");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("description");

        User user = new User();
        user.setId(20L);
        user.setUsername("user");
        UserOrder order = new UserOrder();
        order.setId(20L);
        order.setItems(Arrays.asList(item, item,item));
        order.setUser(user);
        order.setTotal(new BigDecimal(1));
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user");

        assertNotNull(response);
        assertEquals(order.getItems().size(), 3);
    }

}