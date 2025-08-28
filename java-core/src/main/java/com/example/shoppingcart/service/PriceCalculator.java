package com.example.shoppingcart.service;


import com.example.shoppingcart.model.PriceBreakDown;
import com.example.shoppingcart.model.PriceRequest;
import com.example.shoppingcart.model.PricingRule;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PriceCalculator {
    public PriceBreakDown calculatePriceBreakDown(PriceRequest priceRequests) {
        Map<String,Long> itemCount = priceRequests.items().stream().peek(this::validate)
                .collect(Collectors.groupingBy(i->i,Collectors.counting()));

        Map<String,Integer> itemQty = new HashMap<>();
        Map<String,Integer> totalPerItem = new HashMap<>();
        int totalPrice = 0;

        for(var e : itemCount.entrySet()){
              String item = e.getKey();
              int count = e.getValue().intValue();
              int unitPrice = PricingRule.unitPrices.get(item);
              int chargeCount = 0;
              if(PricingRule.isBogof(item)){
                  chargeCount = (count /2) + (count %2); //BOGOF
              } else if (PricingRule.isThreeForTwo(item)) {
                  chargeCount = ((count/3)*2) + (count %3); // 3 for price of 2
              }else{
                  chargeCount = count;
              }
              int itemTotalPrice = unitPrice * chargeCount; //total price calculated per item
              itemQty.put(item,count);
              totalPerItem.put(item,itemTotalPrice);
              totalPrice += itemTotalPrice; // total price combined of all items
        }

        return new PriceBreakDown(itemQty,totalPerItem,totalPrice);
    }

    private void validate(String item){
        if(!PricingRule.unitPrices.containsKey(item))
            throw new IllegalArgumentException("Invalid item" + item);
    }
}
