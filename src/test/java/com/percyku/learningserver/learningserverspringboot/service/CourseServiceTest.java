package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dao.CourseDao;
import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.User;
import com.percyku.learningserver.learningserverspringboot.util.PageCourse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class CourseServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    CourseService courseService;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @Transactional
    public void getCourseByStudent_success(){
        User student1 =userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student1);
        long id = student1.getId();

        student1 =courseDao.findCoursesByStudentId(id);
        assertNotNull(student1);
        List<PageCourse> pageCourses = courseService.getCourseByStudent(id);
        assertNotNull(pageCourses);
        assertEquals(student1.getCourses_student().size(),pageCourses.size());


        List<Course> courses = student1.getCourses_student();
        for(int i =0;i<courses.size();i++){

            Course course = courses.get(i);
            PageCourse pageCourse = pageCourses.get(i);
            assertEquals(course.getId(),pageCourse.getId());
            assertEquals(course.getTitle(),pageCourse.getTitle());
            assertEquals(course.getDescription(),pageCourse.getDescription());
            assertEquals(course.getPrice(),pageCourse.getPrice());

            assertEquals(course.getUsers().size(),pageCourse.getStudents().size());
            assertTrue(pageCourse.getStudents().contains(id));


            assertEquals(course.getUser().getId(),pageCourse.getInstructor().getId());
            assertEquals(course.getUser().getEmail(),pageCourse.getInstructor().getEmail());
            assertEquals(course.getUser().getUserName(),pageCourse.getInstructor().getUsername());
            assertEquals(true,pageCourse.isRegistered());

        }
    }

    @Test
    @Transactional
    public void getCourseByStudent_success_noCourse(){
        User student2 =userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student2);
        long id = student2.getId();

        student2 =courseDao.findCoursesByStudentId(id);
        assertNull(student2);

        List<PageCourse> pageCourses = courseService.getCourseByStudent(id);
        assertNotNull(pageCourses);
        assertEquals(pageCourses.size(),0);

    }


    @Test
    @Transactional
    public void getCourseByInstructor_success(){
        User instructor1 =userDao.getUserByEmail("instructor1.ku@gmail.com");
        assertNotNull(instructor1);
        long id = instructor1.getId();

        List<Course> coursesWithInstructor  =courseDao.findCoursesByInstructorId(id);
        assertNotNull(coursesWithInstructor);

        List<PageCourse> pageCourses = courseService.getCourseByInstructor(id);
        assertNotNull(pageCourses);
        assertEquals(coursesWithInstructor.size(),pageCourses.size());

        for(int i =0;i<coursesWithInstructor.size();i++){

            Course course = coursesWithInstructor.get(i);
            PageCourse pageCourse = pageCourses.get(i);
            assertEquals(course.getId(),pageCourse.getId());
            assertEquals(course.getTitle(),pageCourse.getTitle());
            assertEquals(course.getDescription(),pageCourse.getDescription());
            assertEquals(course.getPrice(),pageCourse.getPrice());

            assertEquals(course.getUsers().size(),pageCourse.getStudents().size());

            assertEquals(id,pageCourse.getInstructor().getId());

            assertEquals(course.getUser().getId(),pageCourse.getInstructor().getId());
            assertEquals(course.getUser().getEmail(),pageCourse.getInstructor().getEmail());
            assertEquals(course.getUser().getUserName(),pageCourse.getInstructor().getUsername());

            assertEquals(course.getUsers().size()!=0?true:false,pageCourse.isRegistered());

        }
    }

    @Test
    @Transactional
    public void getCourseByInstructor_success_noCourse(){
        User instructor2 =userDao.getUserByEmail("instructor2.ku@gmail.com");
        assertNotNull(instructor2);
        long id = instructor2.getId();

        instructor2 =courseDao.findCoursesByStudentId(id);
        assertNull(instructor2);

        List<PageCourse> pageCourses = courseService.getCourseByStudent(id);
        assertNotNull(pageCourses);
        assertEquals(pageCourses.size(),0);

    }



    @Test
    @Transactional
    public void getCourseByCourseName_success(){
        //To check student is exist or not
        User student1 =userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student1);
        User student2 =userDao.getUserByEmail("student2.ku@gmail.com");
        assertNotNull(student1);

        //To check student have course or not
        User tmpUser1 = courseDao.findCoursesByStudentId(student1.getId());
        assertNotNull(tmpUser1);
        User tmpUser2 = courseDao.findCoursesByStudentId(student2.getId());
        assertNull(tmpUser2);



        List<Course> coursesWithCourseName  =courseDao.findCoursesByCourseName("Spring");
        assertNotNull(coursesWithCourseName);

        List<PageCourse> pageCourses = courseService.getCourseByCourseName(student1.getUserName(),"Spring");
        assertNotNull(pageCourses);
        assertEquals(coursesWithCourseName.size(),pageCourses.size());

        for(int i =0;i<coursesWithCourseName.size();i++){

            Course course = coursesWithCourseName.get(i);
            PageCourse pageCourse = pageCourses.get(i);
            assertEquals(course.getId(),pageCourse.getId());
            assertEquals(course.getTitle(),pageCourse.getTitle());
            assertEquals(course.getDescription(),pageCourse.getDescription());
            assertEquals(course.getPrice(),pageCourse.getPrice());

            assertFalse(pageCourse.getStudents().contains(student2.getId()));
            assertEquals(course.getUsers().size(),pageCourse.getStudents().size());
            assertEquals(course.getUser().getId(),pageCourse.getInstructor().getId());
            assertEquals(course.getUser().getEmail(),pageCourse.getInstructor().getEmail());
            assertEquals(course.getUser().getUserName(),pageCourse.getInstructor().getUsername());


        }
    }

    @Test
    @Transactional
    public void getCourseByCourseName_success_noCourse(){
        User student1 =userDao.getUserByEmail("student1.ku@gmail.com");
        assertNotNull(student1);

        List<Course> coursesWithCourseName  =courseDao.findCoursesByCourseName("noCourse");
        assertNotNull(coursesWithCourseName);
        assertEquals(coursesWithCourseName.size(),0);

        List<PageCourse> pageCourses = courseService.getCourseByCourseName(student1.getUserName(),"noCourse");
        assertNotNull(pageCourses);
        assertEquals(pageCourses.size(),0);


        assertEquals(coursesWithCourseName.size(),pageCourses.size(),0);
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
    }


}