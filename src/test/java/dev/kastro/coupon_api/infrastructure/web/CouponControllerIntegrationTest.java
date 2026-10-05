package dev.kastro.coupon_api.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String body(String code, String discount, Instant expiration, String published) {
        return body(code, "10%% off".formatted(), discount, expiration, published);
    }

    private String body(String code, String description, String discount, Instant expiration, String published) {
        return """
                {"code":"%s","description":"%s","discountValue":%s,"expirationDate":"%s"%s}
                """.formatted(code, description, discount, expiration, published);
    }

    private org.springframework.test.web.servlet.ResultActions perform(String json) throws Exception {
        return mockMvc.perform(post("/coupon").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void shouldCreateCouponAndReturnSanitizedCode() throws Exception {
        perform(body("AB#-C12!3", "0.8", Instant.now().plusSeconds(3600), ""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.discountValue").value(0.8))
                .andExpect(jsonPath("$.published").value(false))
                .andExpect(jsonPath("$.redeemed").value(false))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void shouldCreatePublishedCoupon() throws Exception {
        perform(body("ABC123", "1", Instant.now().plusSeconds(3600), ",\"published\":true"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.published").value(true))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldRejectCodeWithWrongLengthAfterSanitization() throws Exception {
        perform(body("AB#C12", "1", Instant.now().plusSeconds(3600), ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldRejectDiscountValueBelowMinimum() throws Exception {
        perform(body("ABC123", "0.4", Instant.now().plusSeconds(3600), ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectExpirationDateInThePast() throws Exception {
        perform(body("ABC123", "1", Instant.now().minusSeconds(3600), ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateCouponWith1000CharactersDescription() throws Exception {
        perform(body("ABC123", "a".repeat(1000), "1", Instant.now().plusSeconds(3600), ""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("a".repeat(1000)));
    }

    @Test
    void shouldRejectDescriptionLongerThan1000CharactersAndInformLimit() throws Exception {
        perform(body("ABC123", "a".repeat(1001), "1", Instant.now().plusSeconds(3600), ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("description must have at most 1000 characters"));
    }

    @Test
    void shouldRejectMissingRequiredFields() throws Exception {
        perform("{}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages").isNotEmpty());
    }

    @Test
    void shouldRejectMalformedBody() throws Exception {
        perform("{not json")
                .andExpect(status().isBadRequest());
    }
}
