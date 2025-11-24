package com.vii.beobssg.service;

import com.alibaba.fastjson.JSON;
import com.vii.beobssg.enums.InventoryTypeEnum;
import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import com.vii.beobssg.model.vo.ItemVO;
import com.vii.beobssg.model.vo.PaginatedResponseVO;
import com.vii.beobssg.repository.InventoryRepository;
import com.vii.beobssg.repository.ItemRepository;
import com.vii.beobssg.repository.OrderRepository;
import com.vii.beobssg.model.vo.ApiResult;
import com.vii.beobssg.utils.PriceUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Override
    public ApiResult<PaginatedResponseVO<ItemVO>> getAllItems(Pageable pageable) {
        Page<ItemDO> pageResult = itemRepo.findAll(pageable);

        List<ItemVO> mappedContent = pageResult.getContent().stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
        log.info("mappedContent={}", JSON.toJSONString(mappedContent));

        PaginatedResponseVO<ItemVO> response = new PaginatedResponseVO<>(
                pageResult.getNumber(),
                mappedContent,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );

        return ApiResult.success(response);
    }

    private ItemVO convertToItemVO(ItemDO itemDO) {
        String formattedPrice = PriceUtil.formatPrice(itemDO.getPrice());
        return new ItemVO(itemDO.getId(), itemDO.getName(), formattedPrice);
    }

    @Override
    public ApiResult<ItemVO> getItem(Long id) {
        ItemDO item = findItemById(id);
        if (item == null) {
            return ApiResult.error(String.format("Item with id %d not found.", id));
        }
        ItemVO itemVO = convertToItemVO(item);
        return ApiResult.success(itemVO);
    }

    private ItemDO findItemById(Long itemId) {
        Optional<ItemDO> itemOpt = itemRepo.findById(itemId);
        log.info("itemDO={}", JSON.toJSONString(itemOpt));
        return itemOpt.orElse(null);
    }

    @Override
    public ApiResult<ItemDO> saveItem(ItemDO item) {
        if (item.getId() != null && itemRepo.existsById(item.getId())) {
            return ApiResult.error(String.format("Item with id %d already exists.", item.getId()));
        }
        ItemDO savedItem = itemRepo.save(item);

        boolean exists = inventoryRepo.existsByItemDO_Id(savedItem.getId());
        if (!exists) {
            InventoryDO inv = new InventoryDO();
            inv.setItemDO(savedItem);
            inv.setType(InventoryTypeEnum.T);
            inv.setQuantity(0);
            inventoryRepo.save(inv);
        }

        return ApiResult.success(savedItem);
    }

    @Override
    public ApiResult<ItemDO> updateItem(Long id, ItemDO itemData) {
        try {
            ItemDO existingItem = findItemById(id);
            if (existingItem == null) {
                return ApiResult.error(String.format("Item with id %d not found.", id));
            }

            Integer currentStock = inventoryRepo.getCurrentStock(id);
            log.info("current stock={}", currentStock);

            existingItem.setName(itemData.getName());
            existingItem.setPrice(itemData.getPrice());
            ItemDO updatedItem = itemRepo.save(existingItem);

            return ApiResult.success(updatedItem);
        } catch (Exception e) {
            return ApiResult.error(String.format("Error updating item: %s", e.getMessage()));
        }
    }

    @Override
    public boolean deleteItem(Long id) {
        Optional<ItemDO> itemOptional = itemRepo.findById(id);
        if (itemOptional.isPresent()) {
            List<OrderDO> orders = orderRepo.findByItemDO(itemOptional.get());
            if (!orders.isEmpty()) {
                log.warn("Cannot delete item with existing orders: {}", id);
                return false;
            }

            List<InventoryDO> inventories = inventoryRepo.findByItemDO(itemOptional.get());
            for (InventoryDO inventory : inventories) {
                inventoryRepo.delete(inventory);
            }

            itemRepo.deleteById(id);
            log.info("Item with id {} deleted successfully", id);
            return true;
        }
        log.warn("Item with id {} not found", id);
        return false;
    }

    @Override
    public boolean deleteInventory(Long id) {
        Optional<InventoryDO> inventoryOptional = inventoryRepo.findById(id);
        if (inventoryOptional.isPresent()) {
            inventoryRepo.deleteById(id);
            log.info(String.format("Inventory with id %d deleted successfully", id));
            return true;
        }
        log.warn(String.format("Inventory with id %d not found", id));
        return false;
    }

    @Override
    public ApiResult<PaginatedResponseVO<InventoryDO>> getAllInventories(Pageable pageable) {
        Page<InventoryDO> pageResult = inventoryRepo.findAll(pageable);
        PaginatedResponseVO<InventoryDO> response = new PaginatedResponseVO<>(
                pageResult.getNumber(),
                pageResult.getContent(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
        log.info(JSON.toJSONString(ApiResult.success(response)));
        return ApiResult.success(response);
    }

    @Override
    @Transactional
    public ApiResult<InventoryDO> saveInventory(Long itemId, InventoryDO inventory) {
        try {
            ItemDO item = findItemById(itemId);
            if (item == null) {
                return ApiResult.error(String.format("Item with id %d not found.", itemId));
            }
            inventory.setItemDO(item);
            InventoryDO savedInventory = inventoryRepo.save(inventory);

            return ApiResult.success(savedInventory);
        } catch (Exception e) {
            return ApiResult.error(String.format("Error saving inventory: %s", e.getMessage()));
        }
    }

    @Override
    public boolean deleteOrder(Long id) {
        Optional<OrderDO> orderOptional = orderRepo.findById(id);
        if (orderOptional.isPresent()) {
            orderRepo.deleteById(id);
            log.info(String.format("Order with id %d deleted successfully", id));
            return true;
        }
        log.warn(String.format("Order with id %d not found", id));
        return false;
    }

    @Override
    public ApiResult<PaginatedResponseVO<OrderDO>> getAllOrders(Pageable pageable) {
        Page<OrderDO> pageResult = orderRepo.findAll(pageable);
        PaginatedResponseVO<OrderDO> response = new PaginatedResponseVO<>(
                pageResult.getNumber(),
                pageResult.getContent(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
        return ApiResult.success(response);
    }

    @Override
    @Transactional
    public boolean saveOrder(Long itemId, OrderDO order) {
        ItemDO item = findItemById(itemId);

        int currentStock = inventoryRepo.getCurrentStock(itemId);
        if (currentStock < order.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Insufficient stock for item: %s", item.getName()));
        }

        InventoryDO inventory = inventoryRepo.getByItemDO(item);
        inventory.setQuantity(inventory.getQuantity() - order.getQuantity());

        inventoryRepo.save(inventory);

        order.setItemDO(item);
        orderRepo.save(order);
        return true;
    }
}

