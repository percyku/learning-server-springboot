package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.util.PageCourse;

import java.util.List;

public interface CourseService {

    List<PageCourse> getCourseByStudent(Long memberId);
    List<PageCourse> getCourseByCourseName(Long id,String courseName);
    List<PageCourse> getCourseByInstructor(Long memberId);
    PageCourse getCourseById(Long courseId);
    PageCourse createCourse(CourseRequest courseRequest);

    PageCourse enrollCourse(int coursed,String userName);

}
