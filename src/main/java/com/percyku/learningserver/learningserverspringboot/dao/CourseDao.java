package com.percyku.learningserver.learningserverspringboot.dao;

import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.User;

import java.util.List;

public interface CourseDao {


    List<Course> findCoursesByInstructorId(Long theId);

    List<Course> findCoursesByCourseName(String name);

    Course findCourseByCourseId(int theId);

    Course findCourseAndStudentsByCourseId(int theId);
    User findCoursesByStudentId(Long theId);

    Integer createCourse(Course course);



}
