# Learning Service SpringBoot

This program is for RESTful API practice


# Programming Languages
- Java(1.7)
- SQL

# Technologies

- Spring boot background (3.1.5)
- Spring Security
- JUnit5
- JPA/Hibernate
- MySQL(8.0.22) -> Using [create-schema.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-schema.sql) / [create-tables.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-tables.sql) /[create-users-data.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-users-data.sql) to create schema / tables /users data

  [<img src="images/relative-table.png">](https://github.com/percyku/learning-server-springboot/blob/master/images/relative-table.png)

# Function

- Member Registration -> Role:Instrutor,Student
- Member Login/Logout
- Member Update Profile
- Instructor Create class
- Student Search class
- Student Enroll class


# Unit Test

First thing ,you need to create [create-schema-unit-test.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-schema-unit-test.sql) in MYSQL for unit test,
then you can use spring.jpa.hibernate.ddl-auto=create (folder:/test/resources/application.properties) or [create-tables-unit-test.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-tables-unit-test.sql) to create tables

- Controller
  - UserControllerTest
  - CourseControllerTest
- Service
  - UserServiceTest
  - CourseServiceTest
- Security
  - MySecurityConfigTest

# RESTful API

You can use postman to test those APIs.
(notes: import this file to postman [restful-api-file](https://github.com/percyku/learning-server-springboot/blob/master/learning-restful-api.postman_collection.json))

Please follow this [detail](https://github.com/percyku/learning-server-springboot/blob/master/restful-api-operation.md) to operate


# Ref

[[NEW] Spring Boot 3, Spring 6 & Hibernate for Beginners](https://www.udemy.com/course/spring-hibernate-tutorial/?couponCode=BFCPSALE24)

[Spring Boot Unit Testing with JUnit, Mockito and MockMvc](https://www.udemy.com/course/spring-boot-unit-testing/?couponCode=BFCPSALE24)

[資安一把罩！Spring Security 零基礎入門](https://hahow.in/courses/64d4acbf018e4b0acfaac3a3)

