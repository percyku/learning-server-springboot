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


import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                .andExpect(jsonPath("$.id",notNullValue()))
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
    public void enrollCourse_success_intoRepeatCourse()throws Exception{
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
    public void enrollCourse_success_intoNewCourse()throws Exception{
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
                .andExpect(jsonPath("$.id",notNullValue()))
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


        List<Course> courses = courseDao.findCoursesByCourseName("");
        assertNotNull(courses);

        Course course = courses.get(courses.size()-1);

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
    public void getCourseByName_success()throws Exception{
        String courseName= "Spring";
        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        List<Course> courses = courseDao.findCoursesByCourseName(courseName);
        assertNotNull(courses);

        Course course1 = courses.get(0);
        int finalCourseNum =courses.size()-1;
        Course courseFinal = courses.get(finalCourseNum);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/findByName")
                .param("courseName", courseName)
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("student1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(courses.size())))
                .andExpect(jsonPath("$.[0].id",equalTo(course1.getId())))
                .andExpect(jsonPath("$.[0].title",equalTo(course1.getTitle())))
                .andExpect(jsonPath("$.[0].description",equalTo(course1.getDescription())))


                .andExpect(jsonPath("$.["+finalCourseNum+"].id",equalTo(courseFinal.getId())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].title",equalTo(courseFinal.getTitle())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].description",equalTo(courseFinal.getDescription())))
        ;
    }

    @Test
    public void getCourseByName_success_noContent()throws Exception{
        String courseName= "JSP";
        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        List<Course> courses = courseDao.findCoursesByCourseName(courseName);
        assertEquals(courses.size(),0);



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/findByName")
                .param("courseName", courseName)
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("student1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(courses.size())));

    }

    @Test
    public void getCourseByName_fail_noParam()throws Exception{
        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/findByName")
//                .param("courseName", "Spring")
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("student1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
        ;
    }

    @Test
    public void getCourseByName_fail_noAccount()throws Exception{
        String courseName= "Spring";


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/findByName")
                .param("courseName", courseName)
                .contentType(MediaType.APPLICATION_JSON);
//                .with(httpBasic("instructor1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    @Test
    public void getCourseByName_success_unAuthorized()throws Exception{
        String courseName= "Spring";

        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        List<Course> courses = courseDao.findCoursesByCourseName(courseName);



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/findByName")
                .param("courseName", courseName)
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("instructor1.ku@gmail.com","fun123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    @Transactional
    public void getCoursesByInstructor_success()throws Exception{


        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        List<Course> courses = courseDao.findCoursesByInstructorId(instructor.getId());
        assertNotNull(courses);

        Course course1 = courses.get(0);
        boolean course1Registered =course1.getUsers().size() == 0?false: true;


        int finalCourseNum =courses.size()-1;
        Course courseFinal = courses.get(finalCourseNum);
        boolean courseFinalRegistered =courseFinal.getUsers().size() == 0?false: true;


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}", instructor.getId())
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(courses.size())))
                .andExpect(jsonPath("$.[0].id",equalTo(course1.getId())))
                .andExpect(jsonPath("$.[0].title",equalTo(course1.getTitle())))
                .andExpect(jsonPath("$.[0].description",equalTo(course1.getDescription())))
                .andExpect(jsonPath("$.[0].instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.[0].instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.[0].instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.[0].instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.[0].instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.[0].registered",not(hasItem(course1Registered))))

                .andExpect(jsonPath("$.["+finalCourseNum+"].id",equalTo(courseFinal.getId())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].title",equalTo(courseFinal.getTitle())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].description",equalTo(courseFinal.getDescription())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].registered",not(hasItem(courseFinalRegistered))))
                ;
    }

    @Test
    public void getCoursesByInstructor_success_noCourse()throws Exception{


        User instructor = userDao.getUserByEmail("instructor2.ku@gmail.com");
        assertNotNull(instructor);

        List<Course> courses = courseDao.findCoursesByInstructorId(instructor.getId());
        assertEquals(courses.size(),0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}", instructor.getId())
                .with(httpBasic("instructor2.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(courses.size())))
        ;
    }

    @Test
    public void getCoursesByInstructor_success_getNewCourse()throws Exception{

        User instructor = userDao.getUserByEmail("instructor2.ku@gmail.com");
        assertNotNull(instructor);

        List<Course> courses = courseDao.findCoursesByInstructorId(instructor.getId());
        assertNotNull(courses);

        assertEquals(courses.size(),0);


        CourseRequest newCourse =new CourseRequest();
        newCourse.setTitle("testTitle");
        newCourse.setPrice(1000);
        newCourse.setDescription("testDescription");

        String json = objectMapper.writeValueAsString(newCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/courses")
                .with(httpBasic("instructor2.ku@gmail.com","fun123"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.title",equalTo(newCourse.getTitle())))
                .andExpect(jsonPath("$.price",equalTo(newCourse.getPrice())))
                .andExpect(jsonPath("$.description",equalTo(newCourse.getDescription())))
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




        requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}", instructor.getId())
                .with(httpBasic("instructor2.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(courses.size()+1)))
                .andExpect(jsonPath("$.[0].id",notNullValue()))
                .andExpect(jsonPath("$.[0].title",equalTo(newCourse.getTitle())))
                .andExpect(jsonPath("$.[0].description",equalTo(newCourse.getDescription())))
                .andExpect(jsonPath("$.[0].price",equalTo(newCourse.getPrice())))
                .andExpect(jsonPath("$.[0].instructor.id",equalTo( instructor.getId().intValue())))
                .andExpect(jsonPath("$.[0].instructor.email",equalTo( instructor.getEmail())))
                .andExpect(jsonPath("$.[0].instructor.username",equalTo( instructor.getUserName())))
                .andExpect(jsonPath("$.[0].instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.roles",equalTo( null)))
                ;

    }

    @Test
    public void getCoursesByInstructor_fail_noParam()throws Exception{
        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        List<Course> courses = courseDao.findCoursesByInstructorId(instructor.getId());
        assertNotNull(courses);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}","")
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void getCoursesByInstructor_fail_noAccount()throws Exception{

        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}",instructor.getId().intValue())
//                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    @Test
    public void getCoursesByInstructor_fail_unAuthorized()throws Exception{

        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);

        User student1 = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/instructor/{id}",instructor.getId().intValue())
                .with(httpBasic("student1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    @Transactional
    public void getEnrolledCourses_success()throws Exception{


        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);


        User userWithCourse = courseDao.findCoursesByStudentId(student.getId());
        assertNotNull(userWithCourse);

        Course course1 = userWithCourse.getCourses_student().get(0);
        boolean course1Registered =course1.getUsers().size() == 0?false: true;


        int finalCourseNum =userWithCourse.getCourses_student().size()-1;
        Course courseFinal = userWithCourse.getCourses_student().get(finalCourseNum);
        boolean courseFinalRegistered =courseFinal.getUsers().size() == 0?false: true;


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", student.getId())
                .with(httpBasic("student1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(userWithCourse.getCourses_student().size())))
                .andExpect(jsonPath("$.[0].id",equalTo(course1.getId())))
                .andExpect(jsonPath("$.[0].title",equalTo(course1.getTitle())))
                .andExpect(jsonPath("$.[0].description",equalTo(course1.getDescription())))
                .andExpect(jsonPath("$.[0].instructor.id",equalTo( courseFinal.getUser().getId().intValue())))
                .andExpect(jsonPath("$.[0].instructor.id",equalTo(  courseFinal.getUser().getId().intValue())))
                .andExpect(jsonPath("$.[0].instructor.email",equalTo(  courseFinal.getUser().getEmail())))
                .andExpect(jsonPath("$.[0].instructor.username",equalTo(  courseFinal.getUser().getUserName())))
                .andExpect(jsonPath("$.[0].instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.[0].instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.[0].instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.[0].registered",not(hasItem(course1Registered))))

                .andExpect(jsonPath("$.["+finalCourseNum+"].id",equalTo(courseFinal.getId())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].title",equalTo(courseFinal.getTitle())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].description",equalTo(courseFinal.getDescription())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.id",equalTo( courseFinal.getUser().getId().intValue())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.id",equalTo( courseFinal.getUser().getId().intValue())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.email",equalTo( courseFinal.getUser().getEmail())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.username",equalTo( courseFinal.getUser().getUserName())))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.["+finalCourseNum+"].registered",not(hasItem(courseFinalRegistered))))
        ;
    }

    @Test
    @Transactional
    public void getEnrolledCourses_success_noCourse()throws Exception{

        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);

        User userWithCourse = courseDao.findCoursesByStudentId(student.getId());
        assertNull(userWithCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", student.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(0)))
        ;
    }

    @Test
    @Transactional
    public void getEnrolledCourses_success_enrollNewCourse()throws Exception{


        User student = userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student);

        User userWithCourse = courseDao.findCoursesByStudentId(student.getId());
        assertNull(userWithCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", student.getId())
                .with(httpBasic("student2.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[*]",hasSize(0)))
        ;

        Course course = courseDao.findCourseByCourseId(4);
        assertNotNull(course);
        assertEquals(course.getUsers().size(),0);

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
                .andExpect(jsonPath("$.instructor.id",equalTo( course.getUser().getId().intValue())))
                .andExpect(jsonPath("$.instructor.email",equalTo( course.getUser().getEmail())))
                .andExpect(jsonPath("$.instructor.username",equalTo( course.getUser().getUserName())))
                .andExpect(jsonPath("$.instructor.password",equalTo(null)))
                .andExpect(jsonPath("$.instructor.first_name",equalTo(null)))
                .andExpect(jsonPath("$.instructor.last_name",equalTo( null)))
                .andExpect(jsonPath("$.instructor.role",equalTo( null)))
                .andExpect(jsonPath("$.instructor.roles",equalTo( null)))
                .andExpect(jsonPath("$.students",hasSize(1)))
                .andExpect(jsonPath("$.students[0]",equalTo(student.getId().intValue())))
                .andExpect(jsonPath("$.registered",not(hasItem(true))));

    }

    @Test
    public void getEnrolledCourses_fail_noParam()throws Exception{
        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", "")
                .with(httpBasic("student1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));

    }

    @Test
    public void getEnrolledCourses_fail_noAccount()throws Exception{

        User student = userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", student.getId().intValue())
//                .with(httpBasic("student1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    @Test
    public void getEnrolledCourses_fail_unAuthorized()throws Exception{

        User instructor = userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor);



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courses/student/{id}", instructor.getId().intValue())
                .with(httpBasic("instructor1.ku@gmail.com","fun123"))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }





    @Test
    public void test() throws Exception {


    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("INSERT INTO role (name) VALUES ('ROLE_STUDENT'),('ROLE_INSTRUCTOR')");

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

        jdbc.execute("INSERT INTO course (title,price,description,user_id) VALUES ('SpringSecurity',1000,'welcome to learn',6)");
        jdbc.execute("INSERT INTO course_user (course_id, user_id) VALUES (2,1)");

        jdbc.execute("INSERT INTO course (title,price,description,user_id) VALUES ('Spring',1000,'welcome to learn',6)");
        jdbc.execute("INSERT INTO course_user (course_id, user_id) VALUES (3,1)");


        jdbc.execute("INSERT INTO course (title,price,description,user_id) VALUES ('SpringMVC',1000,'welcome to learn',6)");
    }


    @AfterEach
    public void setupAfterTransaction() {

        jdbc.execute("DELETE FROM course_user");
        jdbc.execute("DELETE FROM course");
        jdbc.execute("ALTER TABLE course AUTO_INCREMENT = 1");

        jdbc.execute("DELETE FROM users_roles");
        jdbc.execute("DELETE FROM user ");
        jdbc.execute("ALTER TABLE user AUTO_INCREMENT = 1");

        jdbc.execute("DELETE FROM role");
        jdbc.execute("ALTER TABLE role AUTO_INCREMENT = 1");
    }

}