package com.percyku.learningserver.learningserverspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.percyku.learningserver.learningserverspringboot.dao.CourseDao;
import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.model.Course;
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
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CourseControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate jdbc;




    @Test
    public void createCourse_success()throws Exception{

        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id",equalTo(2)))
                .andExpect(jsonPath("$.title",equalTo("testTitle")))
                .andExpect(jsonPath("$.price",equalTo(1000)))
                .andExpect(jsonPath("$.description",equalTo("testDescription")))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(0)))
                .andExpect(jsonPath("$.registered",not(hasItem(false))));

    }

    @Test
    public void createCourse_fail_unCompleteCourseRequest()throws Exception{


        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        CourseRequest newCourse =new CourseRequest();
//        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message",equalTo("title cannot empty,")))
        ;

    }

    @Test
    public void createCourse_fail_noCsrfToken()throws Exception{


        User user = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(user);

        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));

    }

    @Test
    public void createCourse_fail_unAuthorized()throws Exception{
        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("student1.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void createCourse_fail_noAccount()throws Exception{

        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }


    @Test
    @Transactional
    public void enrollCourse_success()throws Exception{
        Course course = courseDao.findCourseByCourseId(1);
        assertNotNull(course);

        User instructor = userDao.getUserByEmail(course.getUser().getEmail());
        assertNotNull(instructor);

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);

        int students = course.getUsers().size();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200)).andExpect(jsonPath("$.id",equalTo(course.getId())))
                .andExpect(jsonPath("$.title",equalTo(course.getTitle())))
                .andExpect(jsonPath("$.price",equalTo(course.getPrice())))
                .andExpect(jsonPath("$.description",equalTo(course.getDescription())))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(students+1)))
                .andExpect(jsonPath("$.registered",not(hasItem(true))));
    }


    @Test
    @Transactional
    public void enrollCourse_success_repeat()throws Exception{
        Course course = courseDao.findCourseByCourseId(1);
        assertNotNull(course);

        User instructor = userDao.getUserByEmail(course.getUser().getEmail());
        assertNotNull(instructor);

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);

        int students = course.getUsers().size();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200)).andExpect(jsonPath("$.id",equalTo(course.getId())))
                .andExpect(jsonPath("$.title",equalTo(course.getTitle())))
                .andExpect(jsonPath("$.price",equalTo(course.getPrice())))
                .andExpect(jsonPath("$.description",equalTo(course.getDescription())))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(students+1)))
                .andExpect(jsonPath("$.registered",not(hasItem(true))));


        requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200)).andExpect(jsonPath("$.id",equalTo(course.getId())))
                .andExpect(jsonPath("$.title",equalTo(course.getTitle())))
                .andExpect(jsonPath("$.price",equalTo(course.getPrice())))
                .andExpect(jsonPath("$.description",equalTo(course.getDescription())))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(students+1)))
                .andExpect(jsonPath("$.registered",not(hasItem(true))));
    }

    @Test
    public void enrollCourse_success_newCourse()throws Exception{
        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id",equalTo(2)))
                .andExpect(jsonPath("$.title",equalTo("testTitle")))
                .andExpect(jsonPath("$.price",equalTo(1000)))
                .andExpect(jsonPath("$.description",equalTo("testDescription")))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(0)))
                .andExpect(jsonPath("$.registered",not(hasItem(false))));


        Course course = courseDao.findCourseByCourseId(2);
        assertNotNull(course);

        instructor = userDao.getUserByEmail(course.getUser().getEmail());
        assertNotNull(instructor);

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);


        requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);



        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200)).andExpect(jsonPath("$.id",equalTo(course.getId())))
                .andExpect(jsonPath("$.title",equalTo(course.getTitle())))
                .andExpect(jsonPath("$.price",equalTo(course.getPrice())))
                .andExpect(jsonPath("$.description",equalTo(course.getDescription())))
                .andExpect(jsonPath("$.instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(1)))
                .andExpect(jsonPath("$.registered",not(hasItem(true))));


    }


    @Test
    public void enrollCourse_fail_noCourse()throws Exception{
        int courseId =-1;
        Course course = courseDao.findCourseByCourseId(courseId);
        assertNull(course);


        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", courseId)
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message",equalTo("This course wasn't exist,please check this course id:"+courseId)));
    }

    @Test
    public void enrollCourse_fail_noCsrfToken()throws Exception{
        Course course = courseDao.findCourseByCourseId(1);
        assertNotNull(course);

        User instructor = userDao.getUserByEmail(course.getUser().getEmail());
        assertNotNull(instructor);

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void enrollCourse_fail_unAuthorized()throws Exception{
        Course course = courseDao.findCourseByCourseId(1);
        assertNotNull(course);

        User instructor = userDao.getUserByEmail("instructor2.ku@gmail.com");
        assertNotNull(instructor);

        assertNotEquals(instructor.getEmail(),course.getUser().getEmail());


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(httpBasic(instructor.getEmail(),"fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }


    @Test
    public void enrollCourse_fail_noAccount()throws Exception{
        Course course = courseDao.findCourseByCourseId(1);
        assertNotNull(course);

        User instructor = userDao.getUserByEmail(course.getUser().getEmail());
        assertNotNull(instructor);

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courses/enroll/{courseId}", course.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }



    @Test
    public void test() throws Exception {


    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student1', 'ku', 'student1.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (1, 1)");
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student2','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student2', 'ku', 'student2.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (2, 1)");
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student3','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student3', 'ku', 'student3.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (3, 1)");
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student4','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student4', 'ku', 'student4.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (4, 1)");
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('student5','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'student5', 'ku', 'student5.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (5, 1)");

        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('instructor1','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'instructor1', 'ku', 'instructor1.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (6, 2)");
        jdbc.execute("INSERT INTO user (username,password,enabled, first_name, last_name, email) VALUES ('instructor2','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1,'instructor2', 'ku', 'instructor2.ku@gmail.com')");
        jdbc.execute("INSERT INTO users_roles (user_id,role_id) VALUES (7, 2)");

        jdbc.execute("INSERT INTO course (title,price,description,user_id) VALUES ('SpringBoot2024',1000,'welcome to learn',6)");
        jdbc.execute("INSERT INTO course_user (course_id, user_id) VALUES (1,1)");
    }

    @AfterEach
    public void setupAfterTransaction() {

        jdbc.execute("DELETE FROM course_user");
        jdbc.execute("DELETE FROM course");
        jdbc.execute("ALTER TABLE course AUTO_INCREMENT = 1");

        jdbc.execute("DELETE FROM users_roles");
        jdbc.execute("DELETE FROM user ");
        jdbc.execute("ALTER TABLE user AUTO_INCREMENT = 1");
    }














}