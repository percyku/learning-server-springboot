package com.percyku.learningserver.learningserverspringboot.service.imp;

import com.percyku.learningserver.learningserverspringboot.dao.CourseDao;
import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.dto.CourseRequest;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.User;
import com.percyku.learningserver.learningserverspringboot.service.CourseService;
import com.percyku.learningserver.learningserverspringboot.util.Member;
import com.percyku.learningserver.learningserverspringboot.util.PageCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Override
    public List<PageCourse> getCourseByStudent(Long memberId) {

        User tmpUser = courseDao.findCoursesByStudentId(memberId);

        if(tmpUser == null){
            return new ArrayList<PageCourse>();
        }

        List<PageCourse> res=new ArrayList<>();
        for(Course tmpCourse:tmpUser.getCourses_student()){

            List<Long>students= new ArrayList<>();
            for(User tmpStudent:tmpCourse.getUsers()){
                students.add(tmpStudent.getId());
            }

            PageCourse tmpPageCourse = new PageCourse(
                    tmpCourse.getId(),
                    tmpCourse.getTitle(),
                    tmpCourse.getDescription(),
                    tmpCourse.getPrice(),
                    new Member(tmpCourse.getUser().getId(),tmpCourse.getUser().getUserName(),tmpCourse.getUser().getEmail()),
                    students
            );

            res.add(tmpPageCourse);
        }
        return res;
    }

    @Override
    public List<PageCourse> getCourseByCourseName(Long id,String courseName) {
        List<Course> tmpCourses = courseDao.findCoursesByCourseName(courseName);
        List<PageCourse> res=new ArrayList<>();
        System.out.println(tmpCourses.toString());
        if(tmpCourses.size() >0){
            User tmpInstructor=tmpCourses.get(0).getUser();
            Member instructor=new Member(tmpInstructor.getId(),
                    tmpInstructor.getUserName(),
                    tmpInstructor.getEmail() );

            for(Course tmpCourse : tmpCourses){
                List<Long>students= new ArrayList<>();
                boolean registered=false;
                for(User tmpStudent:tmpCourse.getUsers()){
                    if(id == tmpStudent.getId()){
                        registered=true;
                    }
                    students.add(tmpStudent.getId());
                }

                PageCourse tmpPageCourse = new PageCourse(
                        tmpCourse.getId(),
                        tmpCourse.getTitle(),
                        tmpCourse.getDescription(),
                        tmpCourse.getPrice(),
                        instructor,
                        students,
                        registered
                );
                res.add(tmpPageCourse);
            }

        }

        return res;
    }

    @Override
    public List<PageCourse> getCourseByInstructor(Long memberId) {

        List<Course> tmpCourses = courseDao.findCoursesByInstructorId(memberId);
        List<PageCourse> res=new ArrayList<>();
        System.out.println(tmpCourses.toString());
        if(tmpCourses.size() >0){
            User tmpInstructor=tmpCourses.get(0).getUser();
            Member instructor=new Member(tmpInstructor.getId(),
                                         tmpInstructor.getUserName(),
                                         tmpInstructor.getEmail() );

            for(Course tmpCourse : tmpCourses){
                List<Long>students= new ArrayList<>();
                for(User tmpStudent:tmpCourse.getUsers()){
                    students.add(tmpStudent.getId());
                }

                PageCourse tmpPageCourse = new PageCourse(
                        tmpCourse.getId(),
                        tmpCourse.getTitle(),
                        tmpCourse.getDescription(),
                        tmpCourse.getPrice(),
                        instructor,
                        students
                );
                res.add(tmpPageCourse);
            }

        }

        return res;
    }

    @Override
    public PageCourse getCourseById(Long courseId) {
        return null;
    }

    @Transactional
    @Override
    public PageCourse createCourse(CourseRequest courseRequest) {

        User user =userDao.getUserById(courseRequest.getUserId());
//        int courseId=26;
        Course tempCourse =new Course(courseRequest.getTitle(),courseRequest.getDescription());
        tempCourse.setUser(user);
        tempCourse.setPrice(courseRequest.getPrice());
        tempCourse.setReviews(null);
        int courseId =  courseDao.createCourse(tempCourse);


        Course tmp =courseDao.findCourseByCourseId(courseId);
        PageCourse tmpPageCourse = new PageCourse(
                tmp.getId(),
                tmp.getTitle(),
                tmp.getDescription(),
                tmp.getPrice(),
                new Member(user.getId(), user.getUserName(), user.getEmail() ),
                new ArrayList<>()
        );

        return tmpPageCourse;
    }

    @Transactional
    @Override
    public PageCourse enrollCourse(int coursed, String userName) {

        //參考findCourseAndStudentsByCourseId

        User student = userDao.getUserByEmail(userName);
        Course course =courseDao.findCourseByCourseId(coursed);
        System.out.println("student id:"+student.getId());
        System.out.println("email:"+student.getEmail());
        System.out.println("course id:"+course.getId());

        if(course == null){
            return null;
        }

        boolean checkExist=false;
        List<Long>students= new ArrayList<>();
        for(User tmpUser: course.getUsers()){
            if(tmpUser.getId() == student.getId()){
                checkExist =true;
            }
            students.add(tmpUser.getId());
            System.out.println(tmpUser.toString());
        }

        if(checkExist){
            System.out.println(checkExist);

        }else{
            System.out.println(false);
            course.addUsers(student);
            students.add(student.getId());
            List<User> tempUsers = course.getUsers();
            if(tempUsers != null){
                for(User user : tempUsers){
                    System.out.println(user.toString());
                }
            }

        }


        PageCourse tmpPageCourse = new PageCourse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                1000,
                new Member(course.getUser().getId(), course.getUser().getUserName(), course.getUser().getEmail() ),
                students
        );



        return tmpPageCourse;
    }


}
