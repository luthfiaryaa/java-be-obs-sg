package com.vii.beobssg.controller;

import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import com.vii.beobssg.model.vo.ItemVO;
import com.vii.beobssg.model.vo.PaginatedResponseVO;
import com.vii.beobssg.service.AppService;
import com.vii.beobssg.model.vo.ApiResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AppService service;

    @GetMapping("/items/{id}")
    public ApiResult<ItemVO> getItem(@PathVariable Long id) {
        return service.getItem(id);
    }

    @GetMapping("/items/getAll")
    public ApiResult<PaginatedResponseVO<ItemVO>> getItems(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return service.getAllItems(PageRequest.of(page, size));
    }

    @PostMapping("/items")
    public ApiResult<ItemDO> createItem(@Valid @RequestBody ItemDO item) {
        return service.saveItem(item);
    }

    @PutMapping("/items/{id}")
    public ApiResult<ItemDO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDO item) {
        return service.updateItem(id, item);
    }

    @DeleteMapping("/items/{id}")
    public ApiResult<Object> deleteItem(@PathVariable Long id) {
        boolean result = service.deleteItem(id);
        if (result) {
            return ApiResult.success("data deleted successfully");
        }
        return ApiResult.error("data could not be deleted");
    }

    @GetMapping("/inventories/getAll")
    public ApiResult<PaginatedResponseVO<InventoryDO>> getInventories(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return service.getAllInventories(PageRequest.of(page, size));
    }

    @PostMapping("/items/{itemId}/inventory")
    public ApiResult<InventoryDO> addInventory(@PathVariable Long itemId, @RequestBody InventoryDO inventory) {
        return service.saveInventory(itemId, inventory);
    }

    @DeleteMapping("/inventories/{id}")
    public ApiResult<Object> deleteInventory(@PathVariable Long id) {
        boolean result = service.deleteInventory(id);
        if (result) {
            return ApiResult.success("data deleted successfully");
        }
        return ApiResult.error("data could not be deleted");
    }

    @GetMapping("/orders/getAll")
    public ApiResult<PaginatedResponseVO<OrderDO>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return service.getAllOrders(PageRequest.of(page, size));
    }

    @PostMapping("/items/{itemId}/order")
    public ApiResult<Object> createOrder(@PathVariable Long itemId, @RequestBody OrderDO order) {
        boolean result = service.saveOrder(itemId, order);
        if (!result) {
            ApiResult.error(String.format("Insufficient stock for item: %s", itemId));
        }
        return ApiResult.success("Order added successfully");
    }

    @DeleteMapping("/orders/{id}")
    public ApiResult<Object> deleteOrder(@PathVariable Long id) {
        boolean result = service.deleteOrder(id);;
        if (result) {
            return ApiResult.success("data deleted successfully");
        }
        return ApiResult.error("data could not be deleted");
    }
}
