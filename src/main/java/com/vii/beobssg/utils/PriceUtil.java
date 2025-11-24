package com.vii.beobssg.utils;

public class PriceUtil {

    public static String formatPrice(Double price) {
        if (price == null) {
            return "0";
        }
        if (price % 1 == 0) {
            return String.format("%.0f", price);
        } else {
            return String.format("%.2f", price);
        }
    }
}
