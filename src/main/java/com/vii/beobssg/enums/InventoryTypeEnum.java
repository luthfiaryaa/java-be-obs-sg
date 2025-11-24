package com.vii.beobssg.enums;

import lombok.Getter;

/**
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description
 */
@Getter
public enum InventoryTypeEnum {

    T("T", "Top Up"),

    W("W", "Withdrawal");


    private final String code;

    private final String message;

    InventoryTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
