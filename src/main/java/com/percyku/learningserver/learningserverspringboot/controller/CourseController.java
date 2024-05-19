package com.percyku.learningserver.learningserverspringboot.controller;

import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.service.CourseService;
import com.percyku.learningserver.learningserverspringboot.service.UserService;
import com.percyku.learningserver.learningserverspringboot.util.Member;
import com.percyku.learningserver.learningserverspringboot.util.PageCourse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Validated
@RestController
public class CourseController {


    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;

    @GetMapping("/api/courses/student/{id}")
    public ResponseEntity<List<PageCourse>> getEnrolledCourses(Authentication authentication,
                                                     @PathVariable Long id){

        List<PageCourse> courses = courseService.getCourseByStudent(id);

        for(PageCourse course : courses) {
            System.out.println(course.toString());
        }
        return  ResponseEntity.status(HttpStatus.OK).body(courses);

    }

    @GetMapping("/api/courses/instructor/{id}")
    public ResponseEntity<List<PageCourse>> getCoursesByInstructor(Authentication authentication,
                                                               @PathVariable Long id){

        System.out.println("UserId:"+id);
        List<PageCourse> courses = courseService.getCourseByInstructor(id);

        return  ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping("/api/courses/findByName/{id}")
    public ResponseEntity<List<PageCourse>>getCourseByName(Authentication authentication,
                                                           @PathVariable Long id,
                                                           @RequestParam(value="courseName", required=true) String courseName){
        List<PageCourse> courses = courseService.getCourseByCourseName(id,courseName);
        return  ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping("/api/courses")
    public ResponseEntity<PageCourse> createCourse(Authentication authentication,@RequestBody @Valid CourseRequest courseRequest){

        PageCourse pageCourse =courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pageCourse);
    }

    @PutMapping("/api/courses/enroll/{courseId}")
    public ResponseEntity<PageCourse> enrollCourse(Authentication authentication,
                                                                   @PathVariable int courseId) {


        PageCourse pageCourse =courseService.enrollCourse(courseId,authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(pageCourse);
    }

}
