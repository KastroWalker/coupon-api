package dev.kastro.coupon_api.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeOpenApiDocsWithCouponPaths() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Coupon API"))
                .andExpect(jsonPath("$.paths['/coupon'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/coupon'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/coupon/{id}'].get.responses['200']").exists())
                .andExpect(jsonPath("$.paths['/coupon/{id}'].get.responses['404']").exists())
                .andExpect(jsonPath("$.paths['/coupon/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/coupon/{id}'].delete.responses['409']").exists());
    }

    @Test
    void shouldExposeSwaggerUi() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
