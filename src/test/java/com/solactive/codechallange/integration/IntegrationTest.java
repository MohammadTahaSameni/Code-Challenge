package com.solactive.codechallange.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallange.model.Ticks;
import com.solactive.codechallange.service.ProviderServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProviderServiceImpl service;

    @After
    public void init() {
        service.init();
    }

    @Test
    public void Test_Single_Statistics() throws Exception {
        final var currentTime = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/ticks")
                .content(asJsonString(new Ticks("TESLA", 250.d, currentTime)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics/TESLA")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("avg").value(250.d))
                .andExpect(MockMvcResultMatchers.jsonPath("min").value(250.d))
                .andExpect(MockMvcResultMatchers.jsonPath("max").value(250.d))
                .andExpect(MockMvcResultMatchers.jsonPath("count").value(1));
    }

    @Test
    public void Test_Aggregator() throws Exception {
        final var currentTime = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/ticks")
                .content(asJsonString(new Ticks("APPLE", 40.d, currentTime)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/ticks")
                .content(asJsonString(new Ticks("NETFLIX", 60.d, currentTime)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("avg").value(50.d))
                .andExpect(MockMvcResultMatchers.jsonPath("min").value(40.d))
                .andExpect(MockMvcResultMatchers.jsonPath("max").value(60.d))
                .andExpect(MockMvcResultMatchers.jsonPath("count").value(2));
    }

    @Test
    public void Test_Stream() throws Exception {
        final var currentTime = System.currentTimeMillis();
        final var T = currentTime + 10 * 1000;
        final var random = new Random();

        var sum = 0.;
        var count = 0;
        var min = Double.MAX_VALUE;
        var max = 0.d;

        for (var u = currentTime; u < T; u += random.nextInt(50)) {
            final var prc = 99.d + 2.d * random.nextFloat();
            sum += prc;
            count++;
            min = Math.min(min, prc);
            max = Math.max(max, prc);

            mockMvc.perform(MockMvcRequestBuilders
                    .post("/ticks")
                    .content(asJsonString(new Ticks("BTC", prc, currentTime)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics/BTC")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("avg").value((double) (sum/count)))
                .andExpect(MockMvcResultMatchers.jsonPath("min").value(min))
                .andExpect(MockMvcResultMatchers.jsonPath("max").value(max))
                .andExpect(MockMvcResultMatchers.jsonPath("count").value(count));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
