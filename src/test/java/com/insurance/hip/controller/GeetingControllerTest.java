package com.insurance.hip.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Executable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Annotations
 * @SpringBootTest → Loads the full Spring ApplicationContext (integration test).
 * @WebMvcTest(ControllerClass.class) → Loads only web layer (faster, for controller tests).
 * @AutoConfigureMockMvc → Enables MockMvc for simulating API calls.
 * @MockBean → Creates a mock bean in Spring context (when testing with dependencies).
 * @DataJpaTest → For repository tests (loads only JPA + H2).
 */

@SpringBootTest
@AutoConfigureMockMvc
public class GeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGreetDefault() throws Exception {
        mockMvc.perform(get("/api/greet"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world!"));
    }
    @Test
    void testGreetWithName() throws Exception{
        mockMvc.perform(get("/api/greet").param("name","Sadik"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Sadik!"));

    }
}
