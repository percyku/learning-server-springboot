package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.model.User;
import com.percyku.learningserver.learningserverspringboot.util.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {



    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserDao userDao;


    @Autowired
    UserService userService;

    @Test
    public void createUserService_success(){
        User createUser = userDao.getUserByEmail("Test.Ku@gmail.com");
        assertNull(createUser);

        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setUsername("Test");
        userRegisterRequest.setEmail("Test.ku@gmail.com");
        userRegisterRequest.setPassword("fun123");
        userRegisterRequest.setFirst_name("Test");
        userRegisterRequest.setLast_name("ku");
        userRegisterRequest.setRoles(Arrays.asList("ROLE_STUDENT"));


        Long id =userService.createUser(userRegisterRequest);
        assertNotEquals(-1,id);

        User newUser = userDao.getUserById(id);
        assertNotNull(newUser);

        assertEquals(newUser.getUserName(),userRegisterRequest.getUsername());
        assertEquals(newUser.getEmail(),userRegisterRequest.getEmail());
        assertEquals(newUser.getFirstName(),userRegisterRequest.getFirst_name());
        assertEquals(newUser.getLastName(),userRegisterRequest.getLast_name());
        assertNotEquals(newUser.getPassword(),userRegisterRequest.getPassword());
    }

    @Test
    public void createUserService_fail_repeatUser(){
        User createUser = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(createUser);

        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setUsername("student1");
        userRegisterRequest.setEmail("student1.ku@gmail.com");
        userRegisterRequest.setPassword("fun123");
        userRegisterRequest.setFirst_name("student1");
        userRegisterRequest.setLast_name("ku");
        userRegisterRequest.setRoles(Arrays.asList("ROLE_STUDENT"));

        Long id =userService.createUser(userRegisterRequest);
        assertEquals(-1,id);

    }

    @Test
    public void getMemberByEmail_success(){
        Member member  = userService.getMemberByEmail("instructor1.ku@gmail.com");
        assertNotNull(member);

        User user = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(member);

        assertEquals(member.getId(),user.getId());
        assertEquals(member.getEmail(),user.getEmail());
        assertEquals(member.getUsername(),user.getUserName());
        assertEquals(member.getFirst_name(),user.getFirstName());
        assertEquals(member.getLast_name(),user.getLastName());
        assertNotEquals(member.getPassword(),user.getPassword());
        assertEquals(member.getRoles().size(),user.getRoles().size());
        assertEquals(member.getRoles().get(0),"ROLE_INSTRUCTOR");


    }

    @Test
    public void getMemberByEmail_fail_noUser(){
        Member member  = userService.getMemberByEmail("fake.ku@gmail.com");
        assertNull(member);

        User user   = userDao.getUserByEmail("fake.ku@gmail.com");
        assertNull(user);

    }


    @Test
    public void getMemberByID_success(){
        Member member  = userService.getMemberById((long)1);
        assertNotNull(member);

        User user   = userDao.getUserById((long)1);
        assertNotNull(member);

        assertEquals(member.getId(),user.getId());
        assertEquals(member.getEmail(),user.getEmail());
        assertEquals(member.getUsername(),user.getUserName());
        assertEquals(member.getFirst_name(),user.getFirstName());
        assertEquals(member.getLast_name(),user.getLastName());
        assertNotEquals(member.getPassword(),user.getPassword());
        assertEquals(member.getRoles().size(),user.getRoles().size());
        assertEquals(member.getRoles().get(0),"ROLE_STUDENT");


    }


    @Test
    public void getMemberByID_fail_noUser(){
        Member member  = userService.getMemberById((long)-1);
        assertNull(member);

        User user   = userDao.getUserById((long)-1);
        assertNull(user);

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