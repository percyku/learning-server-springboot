package com.percyku.learningserver.learningserverspringboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {

    }



    @Test
    public void logout() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/logoutSuccess");


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }



}