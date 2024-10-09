package com.percyku.learningserver.learningserverspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate jdbc;



    @Test
    public void  register_success() throws Exception {
        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setUsername("percy");
        userRegisterRequest.setEmail("percy.ku@gmail.com");
        userRegisterRequest.setPassword("fun123");
        userRegisterRequest.setFirst_name("percy");
        userRegisterRequest.setLast_name("ku");
        userRegisterRequest.setRoles(Arrays.asList("ROLE_STUDENT"));

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.email",equalTo("percy.ku@gmail.com")))
                .andExpect(jsonPath("$.username",equalTo("percy")))
                .andExpect(jsonPath("$.first_name",equalTo("percy")))
                .andExpect(jsonPath("$.last_name",equalTo("ku")))
                .andExpect(jsonPath("$.roles[0]",equalTo("ROLE_STUDENT")));

        User user = userDao.getUserByEmail("percy.ku@gmail.com");
        assertNotEquals(userRegisterRequest.getPassword(),user.getPassword());

    }

    @Test
    public void register_invalidEmailFormat() throws Exception {
        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setUsername("percy");
        userRegisterRequest.setEmail("percy.kugmail.com");
        userRegisterRequest.setPassword("fun123");
        userRegisterRequest.setFirst_name("percy");
        userRegisterRequest.setLast_name("ku");
        userRegisterRequest.setRoles(Arrays.asList("ROLE_STUDENT"));

        String json = objectMapper.writeValueAsString(userRegisterRequest);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo("please check your email is correct or not,")));
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setUsername("percy");
        userRegisterRequest.setEmail("student1.ku@gmail.com");
        userRegisterRequest.setPassword("fun123");
        userRegisterRequest.setFirst_name("percy");
        userRegisterRequest.setLast_name("ku");
        userRegisterRequest.setRoles(Arrays.asList("ROLE_STUDENT"));

        String json = objectMapper.writeValueAsString(userRegisterRequest);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message", equalTo("This user had been exist: "+userRegisterRequest.getEmail())));
    }

    @Test
    public void login_success_student() throws Exception{

        User user = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_STUDENT")
                .with(httpBasic("student1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id",equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$.email",equalTo(user.getEmail())))
                .andExpect(jsonPath("$.username",equalTo(user.getUserName())))
                .andExpect(jsonPath("$.first_name",equalTo(user.getFirstName())))
                .andExpect(jsonPath("$.last_name",equalTo(user.getLastName())))
                .andExpect(jsonPath("$.roles[0]",equalTo("ROLE_STUDENT")));
    }

    @Test
    public void login_success_instructor() throws Exception{

        User user = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_INSTRUCTOR")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id",equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$.email",equalTo(user.getEmail())))
                .andExpect(jsonPath("$.username",equalTo(user.getUserName())))
                .andExpect(jsonPath("$.first_name",equalTo(user.getFirstName())))
                .andExpect(jsonPath("$.last_name",equalTo(user.getLastName())))
                .andExpect(jsonPath("$.roles[0]",equalTo("ROLE_INSTRUCTOR")));
    }

    @Test
    public void login_lackOfParams() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys")
                .with(httpBasic("student1.ku@gmail.com","fun123"));


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
    @Test
    public void login_emailNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_STUDENT")
                .with(httpBasic("fake@gmail.com","fun123"));


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    @Test
    public void login_wrongPassword()throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/loginforlearnsys?role=ROLE_STUDENT")
                .with(httpBasic("student1.ku@gmail.com","fun321"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }


    @Test
    public void logout() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/logoutSuccess");


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }


    @Test
    public void test() throws Exception {

    }

    @BeforeEach
    public void setupDatabase() {
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
    }





}