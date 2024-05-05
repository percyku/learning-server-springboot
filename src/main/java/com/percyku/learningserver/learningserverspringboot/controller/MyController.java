package com.percyku.learningserver.learningserverspringboot.controller;

import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
@RestController
public class MyController {

    @Autowired
    UserDao userDao;


    @GetMapping("/hello")
    public String hello(){
//        User user = userDao.getUserByEmail("percyku19@gmail.com");
//
//        System.out.println(user.toString());
        return "Hello";
    }

    @GetMapping("/hello2")
    public String hello2(@RequestHeader String title){
//
        System.out.println(title.toString());
        return "Hello2";
    }



    @GetMapping("/welcome")
    public String welcome(Authentication authentication,
                          @RequestParam(value="role", required=false)  String role){
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("role:"+role);

        return "welcome " + username + "!";
    }


    @PostMapping("/welcome2")
    public String welcome2(Authentication authentication){
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return "welcome2 " + username + "!";
    }





}
