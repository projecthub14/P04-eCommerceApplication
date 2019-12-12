package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemTestController {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsByName_notFound(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_itemsFound(){

        Item item1 = new Item();
        item1.setId(20L);
        item1.setName("item1");
        item1.setPrice(BigDecimal.ONE);
        item1.setDescription("description 1");

        Item item2 = new Item();
        item2.setId(20L);
        item2.setName("item2");
        item2.setPrice(BigDecimal.ONE);
        item2.setDescription("description 2");

        Item item3 = new Item();
        item3.setId(20L);
        item3.setName("item3");
        item3.setPrice(BigDecimal.ONE);
        item3.setDescription("description 3");

        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(item1, item2, item3));
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");

        assertNotNull(response);
        List<Item> items = response.getBody();
        assertEquals(3, items.size());
        assertEquals(true, item1.getName().equals("item1"));
        assertEquals(true, item2.getName().equals("item2"));
        assertEquals(true, item3.getName().equals("item3"));
    }

    @Test
    public void getItems_itemsFound(){

        Item item1 = new Item();
        item1.setId(20L);
        item1.setName("item1");
        item1.setPrice(BigDecimal.ONE);
        item1.setDescription("description 1");

        Item item2 = new Item();
        item2.setId(20L);
        item2.setName("item2");
        item2.setPrice(BigDecimal.ONE);
        item2.setDescription("description 2");

        Item item3 = new Item();
        item3.setId(20L);
        item3.setName("item3");
        item3.setPrice(BigDecimal.ONE);
        item3.setDescription("description 3");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2,item3));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        List<Item> items = response.getBody();
        assertEquals(3, items.size());
    }

    @Test
    public void getItemById_itemsFound(){

        Item item1 = new Item();
        item1.setId(20L);
        item1.setName("item1");
        item1.setPrice(BigDecimal.ONE);
        item1.setDescription("description 1");

        Item item2 = new Item();
        item2.setId(20L);
        item2.setName("item2");
        item2.setPrice(BigDecimal.ONE);
        item2.setDescription("description 2");

        Item item3 = new Item();
        item3.setId(20L);
        item3.setName("item3");
        item3.setPrice(BigDecimal.ONE);
        item3.setDescription("description 3");

        when(itemRepository.findById(20L)).thenReturn(Optional.of(item2));
        ResponseEntity<Item> response = itemController.getItemById(20L);
        assertNotNull(response);
        Item item = response.getBody();
        assertEquals(item.getName(), "item2");
    }
}
