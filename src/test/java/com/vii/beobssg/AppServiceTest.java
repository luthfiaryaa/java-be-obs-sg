package com.vii.beobssg;

import com.vii.beobssg.enums.InventoryTypeEnum;
import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import com.vii.beobssg.model.vo.ApiResult;
import com.vii.beobssg.model.vo.ItemVO;
import com.vii.beobssg.model.vo.PaginatedResponseVO;
import com.vii.beobssg.repository.InventoryRepository;
import com.vii.beobssg.repository.ItemRepository;
import com.vii.beobssg.repository.OrderRepository;
import com.vii.beobssg.service.AppServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description App Test
 */
@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    private ItemRepository itemRepo;

    @Mock
    private InventoryRepository inventoryRepo;

    @Mock
    private OrderRepository orderRepo;

    @InjectMocks
    private AppServiceImpl service;

    private List<ItemDO> allItems;
    private List<OrderDO> allOrders;
    private List<InventoryDO> allInventories;

    @BeforeEach
    void setUp() {
        allItems = new ArrayList<>();
        allItems.add(new ItemDO(1L, "Pen", 5.0));
        allItems.add(new ItemDO(2L, "Book", 10.0));
        allItems.add(new ItemDO(3L, "Bag", 30.0));
        allItems.add(new ItemDO(4L, "Pencil", 3.0));
        allItems.add(new ItemDO(5L, "Shoe", 45.0));
        allItems.add(new ItemDO(6L, "Box", 5.0));
        allItems.add(new ItemDO(7L, "Cap", 25.0));

        allOrders = new ArrayList<>();
        allOrders.add(new OrderDO(1L, "O1", allItems.get(0), 2));
        allOrders.add(new OrderDO(2L, "O2", allItems.get(1), 3));
        allOrders.add(new OrderDO(3L, "O3", allItems.get(4), 4));
        allOrders.add(new OrderDO(4L, "O4", allItems.get(3), 1));
        allOrders.add(new OrderDO(5L, "O5", allItems.get(4), 2));
        allOrders.add(new OrderDO(6L, "O6", allItems.get(5), 3));
        allOrders.add(new OrderDO(7L, "O7", allItems.get(0), 5));
        allOrders.add(new OrderDO(8L, "O8", allItems.get(1), 4));
        allOrders.add(new OrderDO(9L, "O9", allItems.get(2), 2));
        allOrders.add(new OrderDO(10L, "O10", allItems.get(3), 3));

        allInventories = new ArrayList<>();

        InventoryTypeEnum T = InventoryTypeEnum.T;
        InventoryTypeEnum W = InventoryTypeEnum.W;

        allInventories.add(new InventoryDO(1L, allItems.get(0), T, 5));
        allInventories.add(new InventoryDO(2L, allItems.get(1), T, 10));
        allInventories.add(new InventoryDO(3L, allItems.get(2), T, 30));
        allInventories.add(new InventoryDO(4L, allItems.get(3), T, 3));
        allInventories.add(new InventoryDO(5L, allItems.get(4), T, 45));
        allInventories.add(new InventoryDO(6L, allItems.get(5), T, 5));
        allInventories.add(new InventoryDO(7L, allItems.get(6), T, 25));
        allInventories.add(new InventoryDO(8L, allItems.get(3), T, 7));
        allInventories.add(new InventoryDO(9L, allItems.get(4), W, 10));
    }

    @Test
    void testGetItem_Found() {
        ItemDO expectedItem = allItems.get(0);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(expectedItem));

        ApiResult<ItemDO> result = service.getItem(1L);

        assertEquals(200, result.getCode());
        assertEquals("Pen", result.getData().getName());
        assertEquals(5.0, result.getData().getPrice());
        verify(itemRepo, times(1)).findById(1L);
    }

    @Test
    void testGetItem_NotFound() {
        when(itemRepo.findById(99L)).thenReturn(Optional.empty());

        ApiResult<ItemDO> result = service.getItem(99L);

        assertEquals(500, result.getCode());
        assertEquals("Item with id 99 not found.", result.getMsg());
    }

    @Test
    void testSaveItem_Success() {
        ItemDO newItem = new ItemDO(null, "New Item", 50.0);
        ItemDO savedItem = new ItemDO(8L, "New Item", 50.0);

        when(itemRepo.save(any(ItemDO.class))).thenReturn(savedItem);

        ApiResult<ItemDO> result = service.saveItem(newItem);

        assertEquals(200, result.getCode());
        assertEquals(8L, result.getData().getId());
        assertEquals("New Item", result.getData().getName());
    }

    @Test
    void testDeleteItem_Success() {
        ItemDO itemToDelete = allItems.get(0);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(itemToDelete));

        boolean result = service.deleteItem(1L);

        assertTrue(result);
        verify(itemRepo, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateItem_Success() {
        ItemDO existingItem = allItems.get(1);
        ItemDO updateInput = new ItemDO(2L, "Updated Book", 15.0);

        when(itemRepo.findById(2L)).thenReturn(Optional.of(existingItem));
        when(itemRepo.save(any(ItemDO.class))).thenReturn(updateInput);

        ApiResult<ItemDO> result = service.updateItem(2L, updateInput);

        assertEquals(200, result.getCode());
        assertEquals("Updated Book", result.getData().getName());
        assertEquals(15.0, result.getData().getPrice());
    }

    @Test
    void testGetAllItems() {
        Page<ItemDO> page = new PageImpl<>(allItems);

        when(itemRepo.findAll(any(PageRequest.class))).thenReturn(page);
        ApiResult<PaginatedResponseVO<ItemVO>> result = service.getAllItems(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(200, result.getCode());

        List<ItemVO> dataList = result.getData().getData();
        assertFalse(dataList.isEmpty(), "data cannot be empty");
        assertEquals(7, dataList.size(), "the number of items must be 7");

        assertEquals("Pen", dataList.get(0).getName());
        assertEquals("Cap", dataList.get(6).getName());

        verify(itemRepo, times(1)).findAll(any(PageRequest.class));
    }
}