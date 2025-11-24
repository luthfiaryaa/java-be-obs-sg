package com.vii.beobssg.repository;

import com.vii.beobssg.model.ItemDO;
import com.vii.beobssg.model.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderDO, Long> {

    List<OrderDO> findByItemDO(ItemDO itemDO);

}
