package com.example.shoppingcart.model;

import java.util.Map;

public class PricingRule {
    public static final Map<String, Integer> unitPrices = Map.of(
            "Apple",35, "Banana",20,"Melon",50,"Lime",15
    );

    public static boolean isBogof(String item) {
        return "Melon".equals(item);
    }

    public static boolean isThreeForTwo(String item) {
        return "Lime".equals(item);
    }
}
