package com.example.shoppingcart;

import com.example.shoppingcart.model.PriceRequest;
import com.example.shoppingcart.service.PriceCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {
    private final PriceCalculator calc = new PriceCalculator();

    @Test
    void emptyBasket_totalZero() {
        var out = calc.calculatePriceBreakDown(new PriceRequest(List.of()));
        assertEquals(0, out.totalPrice());
        assertTrue(out.itemQty().isEmpty());
        assertTrue(out.totalPerItem().isEmpty());
    }

    @Test
    void mixedBasket_exampleFromDocs() {
        var out = calc.calculatePriceBreakDown(new PriceRequest(List.of("Apple","Melon","Melon","Lime","Lime","Lime")));
        // Apple: 35, Melon: 50 (BOGO -> pay 1), Lime: 30 (3-for-2 -> pay 2)
        assertEquals(115, out.totalPrice());
        assertEquals(35, out.totalPerItem().get("Apple"));
        assertEquals(50, out.totalPerItem().get("Melon"));
        assertEquals(30, out.totalPerItem().get("Lime"));
    }

    @Test
    void unknownItem_throws() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> calc.calculatePriceBreakDown(new PriceRequest(List.of("Apple", "Dragonfruit"))));
        assertTrue(ex.getMessage().contains("Invalid item"));
    }
}
