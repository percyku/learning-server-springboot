package com.percyku.learningserver.learningserverspringboot.dao;

import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.Review;
import com.percyku.learningserver.learningserverspringboot.model.User;

import java.util.List;

public interface AppDao {

    List<User> findUsers();
    User findUserById(int theId);

    User findStudentAndCourseByStudentId(int theId);
    void saveAndUpdate(User theUser);

    void delete(int theId);

    void update(User theUser);

    void save(Course theCourse);


    Course findCourseByCourseId(int theId);
    List<Course> findCourses();

    List<Course> findCoursesByInstructorId(int theId);

    Course findCourseAndStudentsByCourseId(int theId);

    void deleteCourseById(int theId);


    void save(Review review);

    Review findReviewById(int theId);

    List<Review> findReviews();
    void deleteReviewById(int theId);






}
