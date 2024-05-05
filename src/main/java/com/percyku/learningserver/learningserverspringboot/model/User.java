package com.percyku.learningserver.learningserverspringboot.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;




    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
            CascadeType.DETACH,
//                    CascadeType.MERGE,
                    CascadeType.PERSIST,CascadeType.REFRESH})
     @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    //for role :instructor
    @OneToMany(mappedBy="user",
            fetch=FetchType.LAZY,//by default :FetchType.LAZY
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private List<Course> courses;

    //for role :student
    @OneToMany(mappedBy="user",
            fetch=FetchType.LAZY,//by default :FetchType.LAZY
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,CascadeType.REFRESH})
    private List<Review> reviews;


    @ManyToMany(fetch=FetchType.LAZY,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(name = "course_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses_student;




    public User() {
    }

    public User(String userName, String password, boolean enabled) {
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String userName,
                String firstName,
                String lastName,
                String email,
                String password,
                boolean enabled
                ) {
        this.userName = userName;
        this.firstName=firstName;
        this.lastName = lastName;
        this.email=email;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String userName,
                String firstName,
                String lastName,
                String email,
                String password,
                boolean enabled,
                Collection<Role> roles) {
        this.userName = userName;
        this.firstName=firstName;
        this.lastName = lastName;
        this.email=email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(String userName, String password, boolean enabled,
                Collection<Role> roles) {
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }


    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> course) {
        this.courses = course;
    }

    public void addCourse(Course theCourse){
        if(courses == null){
            courses=new ArrayList<>();
        }

        courses.add(theCourse);
    }

    public List<Course> getCourses_student() {
        return courses_student;
    }

    public void setCourses_student(List<Course> courses_student) {
        this.courses_student = courses_student;
    }


    public void addCourseStudent(Course theCourse){
        if(courses_student == null){
            courses_student=new ArrayList<>();
        }

        courses_student.add(theCourse);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review theReview){
        if(reviews == null){
            reviews=new ArrayList<>();
        }

        reviews.add(theReview);
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}