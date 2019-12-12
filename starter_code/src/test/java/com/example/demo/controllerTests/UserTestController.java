package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserTestController {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    }

    @Test
    public void create_user(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashPassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("hashPassword", user.getPassword());

    }

    @Test
    public void findByUserName_notFound(){
        ResponseEntity<User> response = userController.findByUserName("user");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByUserName_validUser(){

        User user = new User();
        user.setId(20L);
        user.setUsername("user");
        user.setPassword("user");
        when(userRepository.findByUsername("user")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("user");
        assertNotNull(response);
        User user1 = response.getBody();
        assertEquals("user", user1.getUsername());
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void findById_notFound(){
        ResponseEntity<User> response = userController.findById(20L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findById_validUser(){

        User user = new User();
        user.setId(20L);
        user.setUsername("user");
        user.setPassword("user");
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(20L);
        assertNotNull(response);
        User user1 = response.getBody();
        assertEquals("user", user1.getUsername());
        assertEquals(200, response.getStatusCodeValue());

    }

}
