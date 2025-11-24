package com.vii.beobssg.model;

import com.vii.beobssg.enums.InventoryTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventories")
public class InventoryDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDO itemDO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryTypeEnum type;

    @Column(nullable = false)
    private Integer quantity;
}
