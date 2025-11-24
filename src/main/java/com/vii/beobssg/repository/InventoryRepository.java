package com.vii.beobssg.repository;

import com.vii.beobssg.model.InventoryDO;
import com.vii.beobssg.model.ItemDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryDO, Long> {

    List<InventoryDO> findByItemDO(ItemDO itemDO);

    InventoryDO getByItemDO(ItemDO itemDO);

    @Query("SELECT SUM(i.quantity) FROM InventoryDO i WHERE i.itemDO.id = :itemId")
    Integer getTotalQtyByItemId(@Param("itemId") Long itemId);

    boolean existsByItemDO_Id(Long itemId);

    InventoryDO findFirstByItemDO_Id(Long itemId);

    @Query("""
       SELECT COALESCE(SUM(
           CASE WHEN i.type = 'T' THEN i.quantity
                WHEN i.type = 'W' THEN -i.quantity
           END
       ), 0)
       FROM InventoryDO i
       WHERE i.itemDO.id = :itemId
       """)
    Integer getCurrentStock(@Param("itemId") Long itemId);

}
