package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.util.PageCourse;

import java.util.List;

public interface CourseService {

    List<PageCourse> getCourseByStudent(Long memberId);
    List<PageCourse> getCourseByCourseName(String userName,String courseName);
    List<PageCourse> getCourseByInstructor(Long memberId);
    PageCourse createCourse(String userName,CourseRequest courseRequest);
    PageCourse enrollCourse(String userName,int courseId);

}
