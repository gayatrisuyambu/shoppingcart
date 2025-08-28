package com.example.shoppingcart;

import com.example.shoppingcart.controller.PriceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PriceController.class)
public class PriceControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void price_emptyItemsField_returns400() throws Exception {
        // items field missing -> binder gives null; your controller should reject it.
        String badBody = """
          {"items":[]}
        """;

        mvc.perform(post("/api/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().is4xxClientError());
        // If you added a @RestControllerAdvice mapping to 400 with a message, you can assert it:
        // .andExpect(jsonPath("$.error", containsString("items")))
    }

    @Test
    void price_missingItemsField_400() throws Exception {
        String badBodyItemz = """
          {"itemz":["Apple"]}
        """;

        mvc.perform(post("/api/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBodyItemz))
                .andExpect(status().isBadRequest());
    }
}
