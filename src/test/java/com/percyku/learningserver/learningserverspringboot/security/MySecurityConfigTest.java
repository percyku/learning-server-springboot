package com.percyku.learningserver.learningserverspringboot.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MySecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;




    @Test
    public void test_loginSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_STUDENT")
                .with(httpBasic("student1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    public void test_fakeAccountloginUnauthorized() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_STUDENT")
                .with(httpBasic("fake@gmail.com","123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }



    @Test
    public void testCors() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .options("/loginforlearnsys?role=ROLE_STUDENT")
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", "http://localhost:3000");

        mockMvc.perform(requestBuilder)
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET"))
                .andExpect(status().is(200));
    }


    @Test
    public void test_noCsrfToken() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com", "fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void test_withCsrfToken() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com", "fun123"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void testRegister_noCsrfToken() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }


    @Test
    public void test(){

    }


    @BeforeEach
    public void setupDatabase() {

        jdbc.execute("INSERT INTO role (name) VALUES ('ROLE_STUDENT'),('ROLE_INSTRUCTOR')");

        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student1', 'ku', 'student1.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (1, 1)");

        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('instructor1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'instructor1', 'ku', 'instructor1.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (2, 2)");
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM users_roles");
        jdbc.execute("DELETE FROM user ");
        jdbc.execute("ALTER TABLE user AUTO_INCREMENT = 1");

        jdbc.execute("DELETE FROM role");
        jdbc.execute("ALTER TABLE role AUTO_INCREMENT = 1");
    }



}