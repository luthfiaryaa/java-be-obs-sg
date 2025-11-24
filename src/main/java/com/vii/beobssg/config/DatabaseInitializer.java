package com.vii.beobssg.config;

import com.vii.beobssg.enums.InventoryTypeEnum;
import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import com.vii.beobssg.repository.InventoryRepository;
import com.vii.beobssg.repository.ItemRepository;
import com.vii.beobssg.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description Init data record
 */
@Component
@Slf4j
public class DatabaseInitializer {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void initData() {
        if (itemRepository.count() > 0) {
            log.info("the-data-already-exists");
            return;
        }

        ItemDO item1 = saveItem("Pen", 5.0);
        ItemDO item2 = saveItem("Book", 10.0);
        ItemDO item3 = saveItem("Bag", 30.0);
        ItemDO item4 = saveItem("Pencil", 3.0);
        ItemDO item5 = saveItem("Shoe", 45.0);
        ItemDO item6 = saveItem("Box", 5.0);
        ItemDO item7 = saveItem("Cap", 25.0);

        log.info("7 Items created.");

        saveInventory(item1, 5, InventoryTypeEnum.T);
        saveInventory(item2, 10, InventoryTypeEnum.T);
        saveInventory(item3, 30, InventoryTypeEnum.T);
        saveInventory(item4, 3, InventoryTypeEnum.T);
        saveInventory(item5, 45, InventoryTypeEnum.T);
        saveInventory(item6, 5, InventoryTypeEnum.T);
        saveInventory(item7, 25, InventoryTypeEnum.T);
        saveInventory(item4, 7, InventoryTypeEnum.T);
        saveInventory(item5, 10, InventoryTypeEnum.W);

        log.info("-> Inventory transactions processed.");
        saveOrder(item1, "O1", 2);
        saveOrder(item2, "O2", 3);
        saveOrder(item5, "O3", 4);
        saveOrder(item4, "O4", 1);
        saveOrder(item5, "O5", 2);
        saveOrder(item6, "O6", 3);
        saveOrder(item1, "O7", 5);
        saveOrder(item2, "O8", 4);
        saveOrder(item3, "O9", 2);
        saveOrder(item4, "O10", 3);

        log.info("-> 10 Orders processed.");
    }

    private ItemDO saveItem(String name, Double price) {
        ItemDO item = new ItemDO();
        item.setName(name);
        item.setPrice(price);
        return itemRepository.save(item);
    }

    private void saveInventory(ItemDO item, Integer qty, InventoryTypeEnum type) {
        InventoryDO inv = new InventoryDO();
        inv.setItemDO(item);
        inv.setType(type);
        inv.setQuantity(qty);
        inventoryRepository.save(inv);
    }

    private void saveOrder(ItemDO item, String orderNo, Integer qty) {
        OrderDO order = new OrderDO();
        order.setItemDO(item);
        order.setOrderNumber(orderNo);
        order.setQuantity(qty);
        orderRepository.save(order);
    }
}