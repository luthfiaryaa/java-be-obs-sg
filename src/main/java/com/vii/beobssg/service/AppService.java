package com.vii.beobssg.service;

import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import com.vii.beobssg.model.vo.ItemVO;
import com.vii.beobssg.model.vo.PaginatedResponseVO;
import com.vii.beobssg.model.vo.ApiResult;
import org.springframework.data.domain.Pageable;


public interface AppService {

    ApiResult<PaginatedResponseVO<ItemVO>> getAllItems(Pageable pageable);

    public ApiResult<ItemVO> getItem(Long id);

    ApiResult<ItemDO>  saveItem(ItemDO item);

    ApiResult<ItemDO> updateItem(Long id, ItemDO itemData);

    boolean deleteItem(Long id);

    ApiResult<PaginatedResponseVO<InventoryDO>> getAllInventories(Pageable pageable);

    ApiResult<InventoryDO> saveInventory(Long itemId, InventoryDO inventory);

    boolean deleteInventory(Long id);

    ApiResult<PaginatedResponseVO<OrderDO>> getAllOrders(Pageable pageable);

    boolean saveOrder(Long itemId, OrderDO order);

    boolean deleteOrder(Long id);
}
