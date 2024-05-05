package com.percyku.learningserver.learningserverspringboot.dao.imp;

import com.percyku.learningserver.learningserverspringboot.dao.CourseDao;
import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    @Autowired
    private EntityManager entityManager;


    @Override
    public List<Course> findCoursesByInstructorId(Long theId) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course where user.id = :data",Course.class);
        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

    @Override
    public List<Course> findCoursesByCourseName(String name) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course c "
                                                                     +" JOIN FETCH c.user "
                                                                    +" where c.title like '%"+name+"%'",Course.class);
//        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

    @Override
    public Course findCourseByCourseId(int theId) {
        return entityManager.find(Course.class,theId);
    }


    @Override
    public User findCoursesByStudentId(Long theId) {
        //create query
        TypedQuery<User> query = entityManager.createQuery(
                "select c from User c "
                        +"JOIN FETCH c.courses_student "
                        +"where c.id = :data",User.class);
        query.setParameter("data",theId);
        //execute query
        User student= null;

        try{
            student = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

        return student;
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
    public Integer createCourse(Course course) {

        Course tmp=entityManager.merge(course);
        return  tmp.getId();
    }


}
