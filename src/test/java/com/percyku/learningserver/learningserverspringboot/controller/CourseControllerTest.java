package com.percyku.learningserver.learningserverspringboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {

    }

}