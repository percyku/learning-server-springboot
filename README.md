# Learning Service SpringBoot

This program is for RESTful API practice

# Technology

- Spring boot background (3.1.5)
- Spring Security
- Java(1.7)
- JUnit5
- JPA/Hibernate
- MySQL(8.0.22) -> Using [create-schema.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-schema.sql) / [create-tables.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-tables.sql) /[create-users-data.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-users-data.sql) to create schema / tables /users data

  [<img src="images/relative-table.png">](https://github.com/percyku/learning-server-springboot/blob/master/images/relative-table.png)

# Function

- Member Registration -> Role:Instrutor,Student
- Member Login/Logout
- Instrutor Create class
- Studnet Register class
- Studnet Search class

# Unit Test

First thing ,you need to create [create-schema-unit-test.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-schema-unit-test.sql) for unit test,
then you can use spring.jpa.hibernate.ddl-auto=create (fold:/test/resources/application.properties) or [create-tables-unit-test.sql](https://github.com/percyku/learning-server-springboot/blob/master/create-tables-unit-test.sql) to create tables

- Controller
  - UserControllerTest
  - CourseControllerTest
- Service
  - UserServiceTest
  - CourseServiceTest
- Security
  - MySecurityConfigTest

# RESTful API

You can use postman to test below API

- Register(Json body roles: ["ROLE_INTRUTOR"]/["ROLE_STUDNET"])
  [<img src="images/user-register.png">](https://github.com/percyku/learning-server-springboot/blob/master/images/user-register.png)
- Login (URL Param: role=ROLE_INTRUTOR/ role=ROLE_STUDENT)
  [<img src="images/user-login.png">](https://github.com/percyku/learning-server-springboot/blob/master/images/user-login.png)
- Logout(Please type correct account/password in Basic Auth)
  [<img src="images/user-logout.png">](https://github.com/percyku/learning-server-springboot/blob/master/images/user-logout.png)
- Create Course (Only for Instructor,please type Instructor account/password in Basic Auth )
  [<img src="images/create-course.png">](https://github.com/percyku/learning-server-springboot/blob/master/imags/create-course.png)
- Enroll Course (Only for student,please type student account/password in Basic Auth / URL Param 14 which is Course ID )
  [<img src="images/eroll-course.png">](https://github.com/percyku/learning-server-springboot/blob/master/imags/eroll-course.png)
- Search create course by Instructor (Only for Instructor,please type Instructor account/password in Basic Auth ,URL Param 8 which is Instructor ID)
  [<img src="images/search-create-course.png">](https://github.com/percyku/learning-server-springboot/blob/master/imags/search-create-course.png)
- Search enroll course by Student (Only for Student,please type Student account/password in Basic Auth ,URL Param 5 which is Student ID )
  [<img src="images/search-enroll-course.png">](https://github.com/percyku/learning-server-springboot/blob/master/imags/search-enroll-course.png)
- Search any course by Student (Only for Student,please type Student account/password in Basic Auth )
  [<img src="images/search-any-course.png">](https://github.com/percyku/learning-server-springboot/blob/master/imags/search-any-course.png)
