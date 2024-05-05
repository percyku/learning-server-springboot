package com.percyku.learningserver.learningserverspringboot.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {

    //define the fields


    //create constructors

    //generate getter/setter methods

    //generate toString() method

    //annotate fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;


    //for role :instructor
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;


    //for role :student
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(name = "course_user",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> students;


    @OneToMany(mappedBy = "course",
            fetch = FetchType.LAZY,//by default :FetchType.LAZY
            cascade = {CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Review> reviews;


    public Course() {

    }

    public Course(String title) {
        this.title = title;
    }


    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<User> getUsers() {
        return students;
    }

    public void setUsers(List<User> students) {
        this.students = students;
    }


    //add convenience methods
    public void addUsers(User theUser) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(theUser);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public void addReview(Review theReview) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }

        reviews.add(theReview);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}