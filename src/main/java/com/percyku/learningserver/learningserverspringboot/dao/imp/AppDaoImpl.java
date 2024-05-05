package com.percyku.learningserver.learningserverspringboot.dao.imp;

import com.percyku.learningserver.learningserverspringboot.dao.AppDao;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.Review;
import com.percyku.learningserver.learningserverspringboot.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Repository
public class AppDaoImpl implements AppDao {

    private EntityManager entityManager;

    public AppDaoImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }


    @Override
    public List<User> findUsers() {


        TypedQuery<User> query =entityManager.createQuery(
                "select i from User i ",User.class);

        //execute query
        List<User> userList = query.getResultList();

        return userList;
    }

    @Override
    public User findUserById(int theId) {
        return entityManager.find(User.class,theId);
    }


    @Override
    public User findStudentAndCourseByStudentId(int theId) {

        //create query
        TypedQuery<User> query = entityManager.createQuery(
                "select c from User c "
                        +"JOIN FETCH c.courses_student "
                        +"where c.id = :data",User.class);
        query.setParameter("data",theId);
        //execute query

        User student = query.getSingleResult();

        return student;
    }

    @Transactional
    @Override
    public void saveAndUpdate(User theUser) {
        // create the user ... finally LOL
        entityManager.merge(theUser);
//        entityManager.persist(theUser);
    }
    @Transactional
    @Override
    public void delete(int theId) {
        User tempUser =this.findUserById(theId);

        List<Course> tempCourses = tempUser.getCourses();
        for(Course tempCourse : tempCourses){
            tempCourse.setUser(null);
        }

        List<Review> tempReviews = tempUser.getReviews();
        for(Review tempReview : tempReviews){
            tempReview.setUser(null);
        }

        entityManager.remove(tempUser);
    }

    @Transactional
    @Override
    public void update(User theUser) {
//        entityManager.contains(theUser);
        entityManager.merge(theUser);
    }

    @Override
    @Transactional
    public void save(Course theCourse) {
        entityManager.merge(theCourse);
//        entityManager.persist(theCourse);
    }

    @Override
    public Course findCourseByCourseId(int theId) {
        return entityManager.find(Course.class,theId);
    }

    @Override
    public List<Course> findCourses() {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course ",Course.class);
        List<Course> courses = query.getResultList();
        return courses;
    }

    @Override
    public List<Course> findCoursesByInstructorId(int theId) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course where user.id = :data",Course.class);
        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }


    @Override
    public Course findCourseAndStudentsByCourseId(int theId) {
        //create query
        TypedQuery<Course> query = entityManager.createQuery(
                "select c from Course c "
                        +"JOIN FETCH c.students "
                        +"where c.id = :data",Course.class);
        query.setParameter("data",theId);
        //execute query
        List<Course> tempCourse = query.getResultList();
        if(tempCourse.size()>0){
            return tempCourse.get(0);
        }else{
            return null;
        }

    }


    @Override
    @Transactional
    public void deleteCourseById(int theId) {
        //retrieve the course
        Course tempCourse = entityManager.find(Course.class,theId);


//        List<Review> reviewList = tempCourse.getReviews();
//        for(Review tempReview : reviewList) {
//            tempReview.setCourse(null);
//        }


        //delete the course
        entityManager.remove(tempCourse);
    }

    @Override
    @Transactional
    public void save(Review review) {
        entityManager.merge(review);
//        entityManager.persist(review);

    }

    @Override
    public Review findReviewById(int theId) {
        return entityManager.find(Review.class,theId);
    }

    @Override
    public List<Review> findReviews() {
        //create query
        TypedQuery<Review> query =entityManager.createQuery("from Review ",Review.class);
        List<Review> reviews = query.getResultList();
        return reviews;
    }



    @Override
    @Transactional
    public void deleteReviewById(int theId) {
        Review tempReview = entityManager.find(Review.class,theId);
        entityManager.remove(tempReview);

    }


}
