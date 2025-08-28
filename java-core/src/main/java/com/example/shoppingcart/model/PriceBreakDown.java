package com.example.shoppingcart.model;

import java.util.Map;

public record PriceBreakDown(Map<String,Integer> itemQty, Map<String,Integer> totalPerItem, int totalPrice) {
}
